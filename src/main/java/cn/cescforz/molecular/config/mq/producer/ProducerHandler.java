package cn.cescforz.molecular.config.mq.producer;

import cn.cescforz.commons.lang.exception.CustomRtException;
import cn.cescforz.molecular.toolkit.util.KeyUtils;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.rocketmq.client.producer.*;
import org.apache.rocketmq.common.message.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


/**
 * <p>©2019 Cesc. All Rights Reserved.</p>
 * <p>Description: RocketMQ生产者消息处理类</p>
 *
 * @author cesc
 * @version v1.0
 * @date Create in 2019-01-03 14:58
 */
@Slf4j
@Component
public class ProducerHandler<T> extends Producer<T> {

    private DefaultMQProducer defaultProducer;
    private TransactionMQProducer transactionProducer;

    @Autowired
    public ProducerHandler(DefaultMQProducer defaultProducer, TransactionMQProducer transactionProducer) {
        this.defaultProducer = defaultProducer;
        this.transactionProducer = transactionProducer;
    }

    /**
     * <p>Description: 发送普通消息</p>
     *
     * @param t     obj
     * @param topic topic
     * @param tag   tag
     * @param key   key
     */
    @Override
    public void sendMsg(T t, String topic, String tag, String key) {
        Message msg = assemble(t, topic, tag, key);
        try {
            defaultProducer.send(msg, new SendCallback() {
                @Override
                public void onSuccess(SendResult result) {
                    log.info(String.format("消息id:%s,普通消息发送状态:%s", result.getMsgId(), result.getSendStatus()));
                }

                @Override
                public void onException(Throwable e) {
                    throw new CustomRtException(e.getMessage());
                }
            });
        } catch (Exception e) {
            log.error("发送普通消息出现异常:", e);
        }
    }

    /**
     * 发送带事务的消息
     *
     * @param t     : obj
     * @param topic : topic
     * @param tag   : tag
     * @param key   : 唯一键
     * @param arg   : 发送附属对象
     */
    @Override
    public void sendTxMsg(T t, String topic, String tag, String key, Object arg) {
        Message msg = assemble(t, topic, tag, key);
        try {
            TransactionSendResult result = transactionProducer.sendMessageInTransaction(msg, arg);
            log.info(String.format("消息id:%s,事务消息发送状态:%s", result.getMsgId(), result.getLocalTransactionState()));
        } catch (Exception e) {
            log.error("发送事务消息出现异常:", e);
        }
    }


    private Message assemble(T t, String topic, String tag, String key){
        String json = JSON.toJSONString(t);
        if(StringUtils.isBlank(key)){
            key = KeyUtils.generateId().toString();
        }
        return new Message(topic,tag,key,json.getBytes());
    }
}
