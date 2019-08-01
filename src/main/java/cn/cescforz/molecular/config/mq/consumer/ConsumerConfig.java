package cn.cescforz.molecular.config.mq.consumer;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.io.Serializable;

/**
 * <p>©2019 Cesc. All Rights Reserved.</p>
 * <p>Description: RocketMQ消费者参数实体</p>
 * @author cesc
 * @version v1.0
 * @date Create in 2019-01-01 21:32
 */
@Data
@Component
@ConfigurationProperties(prefix = "rocketmq.consumer")
public class ConsumerConfig implements Serializable {

    private static final long serialVersionUID = -4903743823814194323L;
    private String consumerGroup;
    private String namesrvAddr;
    private String consumerInstanceName;
    private Integer consumeThreadMin;
    private Integer consumeThreadMax;
    private Integer consumeTimeout;
    private Integer consumeMessageBatchMaxSize;
    private Integer reConsumerTimes;
    private Boolean consumerBroadcasting;
    private Boolean enableHistoryConsumer;
    private Boolean enableOrderConsumer;
    private String subscribe;
}
