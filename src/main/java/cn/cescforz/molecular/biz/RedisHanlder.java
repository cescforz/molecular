package cn.cescforz.molecular.biz;

import cn.cescforz.commons.lang.constant.SystemConstants;
import cn.cescforz.commons.lang.toolkit.util.StringTools;
import cn.cescforz.molecular.properties.SystemProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.RedisStringCommands;
import org.springframework.data.redis.connection.ReturnType;
import org.springframework.data.redis.core.*;
import org.springframework.data.redis.core.types.Expiration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.nio.charset.Charset;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * <p>©2019 Cesc. All Rights Reserved.</p>
 * <p>Description: redis处理类</p>
 *
 * @author cesc
 * @version v1.0
 * @date Create in 2019-01-09 08:55
 */
@Slf4j
@Component
@EnableScheduling //开启定时器功能
public class RedisHanlder<K,V> {


    @Resource
    private RedisTemplate<K, V> redisTemplate;
    @Resource
    private SystemProperties systemProperties;

    private static final String UNLOCK_LUA;
    private static final String UTF_8 = SystemConstants.CHARSET_STR_UTF_8;


    /**
     * 释放锁脚本，原子操作
     */
    static {
        UNLOCK_LUA = StringTools.assembleStr("if redis.call(\"get\",KEYS[1]) == ARGV[1] ","then ","    return redis.call(\"del\",KEYS[1]) ","else ","    return 0 ","end ");
    }

    /**
     * 获取分布式锁，原子操作
     * @param lockKey :
     * @param requestId :
     * @param expire :
     * @param timeUnit :
     * @return boolean
     */
    public boolean tryLock(String lockKey, String requestId, long expire, TimeUnit timeUnit) {
        try{
            RedisCallback<Boolean> callback = connection -> connection.set(lockKey.getBytes(Charset.forName(UTF_8)), requestId.getBytes(Charset.forName(UTF_8)), Expiration.seconds(timeUnit.toSeconds(expire)), RedisStringCommands.SetOption.SET_IF_ABSENT);
            Boolean flag = redisTemplate.execute(callback);
            return Optional.ofNullable(flag).orElse(false);
        } catch (Exception e) {
            log.error("redis lock error.", e);
        }
        return false;
    }

    /**
     * 释放锁
     * @param lockKey :
     * @param requestId :
     * @return boolean
     */
    public boolean releaseLock(String lockKey, String requestId) {
        RedisCallback<Boolean> callback = connection -> connection.eval(UNLOCK_LUA.getBytes(), ReturnType.BOOLEAN ,1, lockKey.getBytes(Charset.forName(UTF_8)), requestId.getBytes(Charset.forName(UTF_8)));
        Boolean flag = redisTemplate.execute(callback);
        return Optional.ofNullable(flag).orElse(false);
    }

    /**
     * 获取Redis锁的value值
     * @param lockKey
     * @return
     */
    public String get(String lockKey) {
        try {
            return Optional.ofNullable(lockKey).map(u -> {
                RedisCallback<String> callback = connection -> new String(connection.get(u.getBytes()), Charset.forName(UTF_8));
                return redisTemplate.execute(callback);
            }).orElse(null);
        } catch (Exception e) {
            log.error("get redis occurred an exception", e);
        }
        return null;
    }


    /**
     * 默认过期时长，单位：秒
     */
    public static final long DEFAULT_EXPIRE = 60 * 60;

    /**
     * 不设置过期时长
     */
    public static final long NOT_EXPIRE = -1;




    public boolean existsKey(K key) {
        return Optional.ofNullable(key).map(k -> redisTemplate.hasKey(k)).orElse(false);
    }

    /**
     * 重名名key，如果newKey已经存在，则newKey的原值被覆盖
     * @param oldKey
     * @param newKey
     */
    public void renameKey(K oldKey, K newKey) {
        redisTemplate.rename(oldKey, newKey);
    }

    /**
     * newKey不存在时才重命名
     * @param oldKey
     * @param newKey
     * @return 修改成功返回true
     */
    public boolean renameKeyNotExist(K oldKey, K newKey) {
        return redisTemplate.renameIfAbsent(oldKey, newKey);
    }

    /**
     * 删除key
     * @param key
     */
    public void deleteKey(K key) {
        redisTemplate.delete(key);
    }

    /**
     * 删除多个key
     * @param keys
     */
    public void deleteKey(K... keys) {
        Set<K> kSet = Stream.of(keys).collect(Collectors.toSet());
        redisTemplate.delete(kSet);
    }

    /**
     * 删除Key的集合
     * @param keys
     */
    public void deleteKey(Collection<K> keys) {
        Set<K> kSet = new HashSet<>(keys);
        redisTemplate.delete(kSet);
    }

    /**
     * 设置key的生命周期
     * @param key
     * @param time
     * @param timeUnit
     */
    public void expireKey(K key, long time, TimeUnit timeUnit) {
        redisTemplate.expire(key, time, timeUnit);
    }

    /**
     * 指定key在指定的日期过期
     * @param key
     * @param date
     */
    public void expireKeyAt(K key, Date date) {
        redisTemplate.expireAt(key, date);
    }

    /**
     * 查询key的生命周期
     * @param key
     * @param timeUnit
     * @return
     */
    public long getKeyExpire(K key, TimeUnit timeUnit) {
        return redisTemplate.getExpire(key, timeUnit);
    }

    /**
     * 将key设置为永久有效
     * @param key
     */
    public void persistKey(K key) {
        redisTemplate.persist(key);
    }


    /**
     * <p>Description: 向redis消息队列频道发布消息</p>
     * @param message 消息
     */
    public void sendMessage(Object message){
        redisTemplate.convertAndSend(systemProperties.getRedisAisle(),message);
    }



    /**
     * 对hash类型的数据操作
     * @return HashOperations<K, HK, HV>
     */
    public <HK, HV> HashOperations<K, HK, HV> handleHash() {
        return redisTemplate.opsForHash();
    }

    /**
     * 对redis字符串类型数据操作
     * @return ValueOperations<K, V>
     */
    public ValueOperations<K, V> handleStr() {
        return redisTemplate.opsForValue();
    }

    /**
     * 对链表类型的数据操作
     * @return ListOperations<K, V>
     */
    public ListOperations<K, V> handleList() {
        return redisTemplate.opsForList();
    }

    /**
     * 对无序集合类型的数据操作
     * @return SetOperations<K, V>
     */
    public SetOperations<K, V> handleSet() {
        return redisTemplate.opsForSet();
    }

    /**
     * 对有序集合类型的数据操作
     * @return ZSetOperations<K, V>
     */
    public ZSetOperations<K, V> handleZSet() {
        return redisTemplate.opsForZSet();
    }
}
