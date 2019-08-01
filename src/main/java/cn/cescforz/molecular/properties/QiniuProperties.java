package cn.cescforz.molecular.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.io.Serializable;

/**
 * <p>Description: 七牛云配置</p>
 *
 * @author cescforz@gmail.com
 * @version 1.00.00
 * @date Create in 2019/6/11 11:29
 */
@Data
@Component
@ConfigurationProperties(prefix = "qiniu")
public class QiniuProperties implements Serializable {

    private static final long serialVersionUID = 1712448374968510424L;

    private String accessKey;
    private String secretKey;
    private String bucket;
    private String path;
}
