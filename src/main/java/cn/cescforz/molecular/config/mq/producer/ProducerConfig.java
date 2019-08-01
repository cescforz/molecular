package cn.cescforz.molecular.config.mq.producer;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.io.Serializable;

/**
 * <p>©2019 Cesc. All Rights Reserved.</p>
 * <p>Description: RocketMQ生产者参数实体</p>
 * @author cesc
 * @version v1.0
 * @date Create in 2019-01-01 21:28
 */
@Data
@Component
@ConfigurationProperties(prefix = "rocketmq.producer")
public class ProducerConfig implements Serializable {

    private static final long serialVersionUID = -5683372880020389942L;
    private String producerGroup;
    private String producerInstanceName;
    private String transactionProducerGroup;
    private String producerTranInstanceName;
    private String namesrvAddr;
    private Integer maxMessageSize;
    private Integer sendMsgTimeout;
    private Integer retryTimesWhenSendFailed;

}
