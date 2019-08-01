package cn.cescforz.molecular.config.mq.producer;


/**
 * <p>©2019 Cesc. All Rights Reserved.</p>
 * <p>Description: RocketMQ生产者消息处理抽象类</p>
 *
 * @author cesc
 * @version v1.0
 * @date Create in 2019-01-01 12:42
 */
public abstract class Producer<T> {

    /**
     * 发送普通消息
     *
     * @param t     : 消息体
     * @param topic : 主题
     * @param tag   : 目标
     * @param key   : 唯一键
     */
    public abstract void sendMsg(T t, String topic, String tag, String key);

    /**
     * 发送事物消息
     *
     * @param t     : 消息体
     * @param topic : 主题
     * @param tag   : 目标
     * @param key   : 唯一键
     */
    public abstract void sendTxMsg(T t, String topic, String tag, String key, Object arg);

}

