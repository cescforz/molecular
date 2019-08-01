package cn.cescforz.molecular.config.mq.consumer;

import cn.cescforz.molecular.bean.model.ConsumerEvent;
import cn.cescforz.molecular.toolkit.util.ThreadPoolUtils;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.ConsumeOrderlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.consumer.listener.MessageListenerOrderly;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.common.protocol.heartbeat.MessageModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>©2019 Cesc. All Rights Reserved.</p>
 * <p>Description: RocketMQ消费者配置类</p>
 * @author cesc
 * @version v1.0
 * @date Create in 2019-01-03 09:21
 */
@Slf4j
@Configuration
@ConditionalOnProperty(prefix = "rocketmq.consumer",value = "namesrvAddr")
public class ConsumerConfigure {

    /**消费者参数实体*/
    private ConsumerConfig consumerConfig;
    /**发布事件监听器*/
    private ApplicationEventPublisher publisher;

    @Autowired
    public ConsumerConfigure(ConsumerConfig consumerConfig, ApplicationEventPublisher publisher) {
        this.consumerConfig = consumerConfig;
        this.publisher = publisher;
    }

    private static boolean isFirstSub = true;
    private static long startTime = System.currentTimeMillis();

    @PostConstruct
    public void init(){
        log.info("消费者参数初始化:{}", JSON.toJSONString(consumerConfig,true));
    }

    @Bean
    @ConditionalOnProperty(prefix = "rocketmq.consumer",value = "consumerGroup")
    @ConditionalOnClass(DefaultMQPushConsumer.class)
    @ConditionalOnMissingBean(DefaultMQPushConsumer.class)
    public DefaultMQPushConsumer pushConsumer() throws MQClientException {
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer(consumerConfig.getConsumerGroup());
        consumer.setNamesrvAddr(consumerConfig.getNamesrvAddr());
        consumer.setInstanceName(consumerConfig.getConsumerInstanceName());
        //RocketMQ默认为MessageModel.CLUSTERING-->判断是否是广播模式
        if (consumerConfig.getConsumerBroadcasting()) {
            consumer.setMessageModel(MessageModel.BROADCASTING);
        }
        //设置批量消费
        consumer.setConsumeMessageBatchMaxSize(consumerConfig.getConsumeMessageBatchMaxSize() == 0 ? 1 : consumerConfig
                .getConsumeMessageBatchMaxSize());

        //获取topic和tag
        String subscribe = consumerConfig.getSubscribe();
        String[] topicAndTag = subscribe.split("~");
        consumer.subscribe(topicAndTag[0], topicAndTag[1]);

        //设置消费超时时间(分钟)
        consumer.setConsumeTimeout(consumerConfig.getConsumeTimeout());
        consumer.setMaxReconsumeTimes(consumerConfig.getReConsumerTimes());
        // 顺序消费
        if (consumerConfig.getEnableOrderConsumer()) {
            consumer.registerMessageListener((MessageListenerOrderly) (msgs, context) -> {
                try {
                    context.setAutoCommit(true);
                    msgs = filterMessage(msgs);
                    if (msgs.isEmpty()) {
                        return ConsumeOrderlyStatus.SUCCESS;
                    }
                    publisher.publishEvent(new ConsumerEvent(msgs, consumer));
                } catch (Exception e) {
                    log.error("顺序消费出错:",e);
                    return ConsumeOrderlyStatus.SUSPEND_CURRENT_QUEUE_A_MOMENT;
                }
                return ConsumeOrderlyStatus.SUCCESS;
            });
        }
        // 并发消费
        else {
            consumer.registerMessageListener((MessageListenerConcurrently) (msgs, context) -> {
                try {
                    //过滤消息
                    msgs = filterMessage(msgs);
                    if (msgs.isEmpty()) {
                        return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
                    }
                    publisher.publishEvent(new ConsumerEvent(msgs, consumer));
                } catch (Exception e) {
                    log.error("并发消费出错:",e);
                    return ConsumeConcurrentlyStatus.RECONSUME_LATER;
                }
                return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
            });
        }
        ThreadPoolUtils.getInstance().execute(() -> {
            try {
                consumer.start();
                log.info("rocketmq consumer server is starting....");
            } catch (Exception e) {
                log.error("rocketmq consumer server start error:", e);
            }
        });
        return consumer;
    }


    /**
     * <p>Description: 消息过滤</p>
     * @param msgs 消息集合
     * @return java.util.List<org.apache.rocketmq.common.message.MessageExt>
     */
    private List<MessageExt> filterMessage(List<MessageExt> msgs) {
        if (isFirstSub && !consumerConfig.getEnableHistoryConsumer()) {
            msgs = msgs.stream().filter(item -> startTime - item.getBornTimestamp() < 0).collect(Collectors.toList());
        }
        if (isFirstSub && !msgs.isEmpty()) {
            isFirstSub = false;
        }
        return msgs;
    }
}
