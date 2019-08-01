package cn.cescforz.molecular.config;

import cn.cescforz.molecular.bean.model.FastJsonRedisSerializer;
import cn.cescforz.molecular.listener.MessageReceiver;
import cn.cescforz.molecular.properties.SystemProperties;
import com.alibaba.fastjson.parser.ParserConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.data.redis.serializer.RedisSerializationContext;

import java.lang.reflect.Method;
import java.time.Duration;

/**
 * <p>©2019 Cesc. All Rights Reserved.</p>
 * <p>Description: springboot 2.x redis 配置 </p>
 *
 * @author cesc
 * @version v1.0
 * @date Create in 2019-01-07 09:00
 */
@Slf4j
@Configuration
@EnableCaching // 开启缓存支持
public class RedisConfig extends CachingConfigurerSupport {

    private SystemProperties systemProperties;

    @Autowired
    public void setSystemProperties(SystemProperties systemProperties) {
        this.systemProperties = systemProperties;
    }

    @Bean
    @Primary //当有多个管理器的时候，必须使用该注解在一个管理器上注释：表示该管理器为默认的管理器
    public CacheManager cacheManager(LettuceConnectionFactory lettuceConnectionFactory) {
        // 初始化一个RedisCacheWriter
        RedisCacheWriter redisCacheWriter = RedisCacheWriter.nonLockingRedisCacheWriter(lettuceConnectionFactory);
        // 序列化方式1
        // 设置CacheManager的值序列化方式为JdkSerializationRedisSerializer,但其实RedisCacheConfiguration默认就是使用StringRedisSerializer序列化key，JdkSerializationRedisSerializer序列化value,所以以下(4行)注释代码为默认实现
        //ClassLoader loader = this.getClass().getClassLoader();
        //JdkSerializationRedisSerializer jdkSerializer = new JdkSerializationRedisSerializer(loader);
        //RedisSerializationContext.SerializationPair<Object> pair = RedisSerializationContext.SerializationPair.fromSerializer(jdkSerializer);
        //RedisCacheConfiguration defaultCacheConfig=RedisCacheConfiguration.defaultCacheConfig().serializeValuesWith(pair);
        //序列化方式1---另一种实现方式
        //RedisCacheConfiguration defaultCacheConfig = RedisCacheConfiguration.defaultCacheConfig();//该语句相当于序列化方式1

        // 序列化方式2(JSONObject)
        FastJsonRedisSerializer<Object> fastJsonRedisSerializer = new FastJsonRedisSerializer<>(Object.class);
        RedisSerializationContext.SerializationPair<Object> pair = RedisSerializationContext.SerializationPair.fromSerializer(fastJsonRedisSerializer);
        RedisCacheConfiguration defaultCacheConfig= RedisCacheConfiguration.defaultCacheConfig().serializeValuesWith(pair);

        // 序列化方式3
        //Jackson2JsonRedisSerializer serializer=new Jackson2JsonRedisSerializer(Object.class);
        //RedisSerializationContext.SerializationPair<Object> pair = RedisSerializationContext.SerializationPair.fromSerializer(serializer);
        //RedisCacheConfiguration defaultCacheConfig=RedisCacheConfiguration.defaultCacheConfig().serializeValuesWith(pair);

        // 设置过期时间(100秒)
        defaultCacheConfig = defaultCacheConfig.entryTtl(Duration.ofSeconds(100));

        // 初始化RedisCacheManager
        RedisCacheManager cacheManager = new RedisCacheManager(redisCacheWriter, defaultCacheConfig);

        // 设置白名单---非常重要(说明见下面注释) ParserConfig.getGlobalInstance().addAccept("cn.cescforz.bar.bean.");
        String methodName = "addAccept";
        String args = "cn.cescforz.molecular.bean.";
        boolean executed = false;
        ParserConfig parserConfig = ParserConfig.getGlobalInstance();
        for(Method m : ParserConfig.class.getDeclaredMethods()){
            if(m.getName().equals(methodName)){
                try {
                    m.invoke(parserConfig, args);
                    executed = true;
                } catch (Exception e) {
                    log.error("fastjson白名单添加出现异常:", e);
                }

            }
        }
        log.info("fastjson白名单添加结果:{}",executed);
        return cacheManager;
    }

    @Bean(name = "redisTemplate")
    @SuppressWarnings("unchecked")
    @ConditionalOnMissingBean(name = "redisTemplate")
    public RedisTemplate<Object, Object> redisTemplate(LettuceConnectionFactory lettuceConnectionFactory) {
        RedisTemplate<Object, Object> template = new RedisTemplate<>();
        //使用fastjson序列化
        FastJsonRedisSerializer fastJsonRedisSerializer = new FastJsonRedisSerializer(Object.class);
        // value值的序列化采用fastJsonRedisSerializer
        template.setValueSerializer(fastJsonRedisSerializer);
        template.setHashValueSerializer(fastJsonRedisSerializer);
        // key的序列化采用fastJsonRedisSerializer(StringRedisSerializer)
        template.setKeySerializer(fastJsonRedisSerializer);
        template.setHashKeySerializer(fastJsonRedisSerializer);
        template.setConnectionFactory(lettuceConnectionFactory);
        return template;
    }

    /**
     * <p>Description: Redis 的 String 数据结构 （推荐使用 StringRedisTemplate）</p>
     * @param lettuceConnectionFactory
     * @return org.springframework.data.redis.core.StringRedisTemplate
     */
    @Bean
    @ConditionalOnMissingBean(StringRedisTemplate.class)
    public StringRedisTemplate stringRedisTemplate(LettuceConnectionFactory lettuceConnectionFactory) {
        StringRedisTemplate template = new StringRedisTemplate();
        template.setConnectionFactory(lettuceConnectionFactory);
        return template;
    }


    /**
     * <p>Description: redis主键生成策略工具类</p>
     * @param
     * @return org.springframework.cache.interceptor.KeyGenerator
     */
    @Bean
    @Override
    @ConditionalOnMissingBean
    public KeyGenerator keyGenerator() {
        return (target, method, params) -> {
            StringBuffer sb = new StringBuffer();
            sb.append(target.getClass().getName());
            sb.append(method.getName());
            for (Object obj : params) {
                sb.append(obj.toString());
            }
            return sb.toString();
        };
    }

    /**
     * <p>Description: redis消息监听器容器
     * 可以添加多个监听不同话题的redis监听器，只需要把消息监听器和相应的消息订阅处理器绑定，该消息监听器
     * 通过反射技术调用消息订阅处理器的相关方法进行一些业务处理</p>
     * @param lettuceConnectionFactory 1
     * @param adapter 2
     * @return org.springframework.data.redis.listener.RedisMessageListenerContainer
     */
    @Bean
    public RedisMessageListenerContainer container(LettuceConnectionFactory lettuceConnectionFactory, MessageListenerAdapter adapter) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(lettuceConnectionFactory);
        String redisAisle = systemProperties.getRedisAisle();
        //配置监听通道
        container.addMessageListener(adapter, new PatternTopic(redisAisle));
        log.info(String.format("redis消息队列初始化监听成功，正在监听通道:%s",redisAisle));
        return container;
    }

    /**
     * <p>Description: 消息监听器适配器，绑定消息处理器，利用反射技术调用消息处理器的业务方法</p>
     * @param receiver
     * @return org.springframework.data.redis.listener.adapter.MessageListenerAdapter
     */
    @Bean
    MessageListenerAdapter listenerAdapter(MessageReceiver receiver) {
        //这个地方 是给messageListenerAdapter 传入一个消息接受的处理器，利用反射的方法调用“receiveMessage”
        return new MessageListenerAdapter(receiver, "receiveMessage");
    }




    /*
        Jedis 是直连模式，在多个线程间共享一个 Jedis 实例时是线程不安全的，
        如果想要在多线程环境下使用 Jedis，需要使用连接池，
        每个线程都去拿自己的 Jedis 实例，当连接数量增多时，物理连接成本就较高了。
        Lettuce的连接是基于Netty的，连接实例可以在多个线程间共享，
        所以，一个多线程的应用可以使用同一个连接实例，而不用担心并发线程的数量。
        当然这个也是可伸缩的设计，一个连接实例不够的情况也可以按需增加连接实例。

        通过异步的方式可以让我们更好的利用系统资源，而不用浪费线程等待网络或磁盘I/O。
        Lettuce 是基于 netty 的，netty 是一个多线程、事件驱动的 I/O 框架，
        所以 Lettuce 可以帮助我们充分利用异步的优势。
     */

    /*
     * <p>Description: 设置 redis 数据默认过期时间;设置@cacheable 序列化方式</p>
     * @return org.springframework.data.redis.cache.RedisCacheConfiguration

    @Bean
    public RedisCacheConfiguration redisCacheConfiguration(){
        FastJsonRedisSerializer<Object> fastJsonRedisSerializer = new FastJsonRedisSerializer<>(Object.class);
        RedisCacheConfiguration configuration = RedisCacheConfiguration.defaultCacheConfig();
        configuration = configuration.serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(fastJsonRedisSerializer)).entryTtl(Duration.ofDays(30));
        return configuration;
    }

    */


        /*
        使用fastjson的时候：序列化时将class信息写入，反解析的时候，
        fastjson默认情况下会开启autoType的检查，相当于一个白名单检查，
        如果序列化信息中的类路径不在autoType中，
        反解析就会报com.alibaba.fastjson.JSONException: autoType is not support的异常
        可参考 https://blog.csdn.net/u012240455/article/details/80538540
         */
}
