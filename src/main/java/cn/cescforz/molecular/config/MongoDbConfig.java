package cn.cescforz.molecular.config;

import com.mongodb.MongoClientOptions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * <p>Description: mongodb配置</p>
 *
 * @author cesc
 * @version 1.00.00
 * @date Create in 2019/8/23 16:44
 */
@Configuration
public class MongoDbConfig {


    @Bean
    public MongoClientOptions mongoOptions() {
        return MongoClientOptions.builder().maxConnectionIdleTime(60000).build();
    }
}
