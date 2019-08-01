package cn.cescforz.molecular.config.mq.consumer;

import cn.cescforz.molecular.bean.model.ConsumerEvent;


/**
 * <p>©2019 Cesc. All Rights Reserved.</p>
 * <p>Description: RocketMQ消费者处理抽象类</p>
 *
 * @author cesc
 * @version v1.0
 * @date Create in 2019-01-01 12:45
 */
public abstract class Consumer {

    /**
     * 处理异常的消费者
     * @param event :
     */
    public abstract void handleExceptionListener(ConsumerEvent event);

    /**
     * 处理接口调用的消费者
     * @param event :
     */
    public abstract void handleLogListener(ConsumerEvent event);
}

