package cn.cescforz.molecular.config.mq.producer;

import cn.cescforz.molecular.toolkit.util.ThreadPoolUtils;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.TransactionListener;
import org.apache.rocketmq.client.producer.TransactionMQProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.util.concurrent.ExecutorService;

/**
 * <p>©2019 Cesc. All Rights Reserved.</p>
 * <p>Description: RocketMQ生产者配置类</p>
 * @author cesc
 * @version v1.0
 * @date Create in 2019-01-01 21:52
 */
@Slf4j
@Configuration
@ConditionalOnProperty(prefix = "rocketmq.producer",value = "namesrvAddr")
public class ProducerConfigure {

    /** 生产者参数实体 */
    private ProducerConfig producerConfig;
    /** 事务消息监听器 */
    private TransactionListener transactionListener;

    @Autowired
    public ProducerConfigure(ProducerConfig producerConfig, TransactionListener transactionListener) {
        this.producerConfig = producerConfig;
        this.transactionListener = transactionListener;
    }

    @PostConstruct
    public void init(){
        log.info("生产者参数初始化:{}", JSON.toJSONString(producerConfig,true));
    }

    /**
     * <p>Description: 创建支持消息事务发送的实例</p>
     * @return org.apache.rocketmq.client.producer.TransactionMQProducer
     * @throws MQClientException MQClientException
     */
    @Bean
    @ConditionalOnClass(TransactionMQProducer.class)
    @ConditionalOnMissingBean(TransactionMQProducer.class)
    @ConditionalOnProperty(prefix = "rocketmq.producer",value = "transactionProducerGroup")
    public TransactionMQProducer transactionProducer() throws MQClientException {
        TransactionMQProducer producer = new TransactionMQProducer(producerConfig.getTransactionProducerGroup());
        producer.setNamesrvAddr(producerConfig.getNamesrvAddr());
        producer.setInstanceName(producerConfig.getProducerTranInstanceName());
        producer.setMaxMessageSize(producerConfig.getMaxMessageSize());
        producer.setSendMsgTimeout(producerConfig.getSendMsgTimeout());
        producer.setRetryTimesWhenSendAsyncFailed(producerConfig.getRetryTimesWhenSendFailed());
        ExecutorService executorService = ThreadPoolUtils.getInstance();
        producer.setExecutorService(executorService);
        // 设置事务监听器
        producer.setTransactionListener(transactionListener);
        producer.start();
        log.info("rocketmq transaction producer server is starting....");
        return producer;
    }

    /**
     * <p>Description: 创建普通消息发送者实例</p>
     * @return org.apache.rocketmq.client.producer.DefaultMQProducer
     * @throws MQClientException MQClientException
     */
    @Bean
    @ConditionalOnClass(DefaultMQProducer.class)
    @ConditionalOnMissingBean(DefaultMQProducer.class)
    @ConditionalOnProperty(prefix = "rocketmq.producer",value = "producerGroup")
    public DefaultMQProducer defaultProducer() throws MQClientException {
        DefaultMQProducer producer = new DefaultMQProducer(producerConfig.getProducerGroup());
        producer.setNamesrvAddr(producerConfig.getNamesrvAddr());
        producer.setInstanceName(producerConfig.getProducerInstanceName());
        producer.setVipChannelEnabled(false);
        producer.setMaxMessageSize(producerConfig.getMaxMessageSize());
        producer.setSendMsgTimeout(producerConfig.getSendMsgTimeout());
        producer.setRetryTimesWhenSendAsyncFailed(producerConfig.getRetryTimesWhenSendFailed());
        producer.start();
        log.info("rocketmq producer server is starting....");
        return producer;
    }
}
