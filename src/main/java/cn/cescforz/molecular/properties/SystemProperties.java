package cn.cescforz.molecular.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.io.Serializable;

/**
 * <p>©2019 Cesc. All Rights Reserved.</p>
 * <p>Description: </p>
 *
 * @author cesc
 * @version v1.0
 * @date Create in 2019-01-11 17:15
 */
@Data
@Component
@ConfigurationProperties(prefix = "system-config")
public class SystemProperties implements Serializable {

    private static final long serialVersionUID = -5316713521500298232L;

    private String redisAisle;
}
