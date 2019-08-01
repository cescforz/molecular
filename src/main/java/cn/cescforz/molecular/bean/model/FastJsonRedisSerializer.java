package cn.cescforz.molecular.bean.model;

import cn.cescforz.commons.lang.constant.SystemConstants;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;

/**
 * <p>©2019 Cesc. All Rights Reserved.</p>
 * <p>Description: 自定义的fastjson序列化反序列化器</p>
 *
 * @author cesc
 * @version v1.0
 * @date Create in 2019-01-07 09:13
 */
public class FastJsonRedisSerializer<T> implements RedisSerializer<T> {

    private Class<T> clazz;
    public FastJsonRedisSerializer(Class<T> clazz) {
        super();
        this.clazz = clazz;
    }

    /**
     * <p>Description: 序列化</p>
     * @param t 参数对象
     * @return byte[]
     */
    @Override
    public byte[] serialize(T t) throws SerializationException {
        if (null == t) {
            return new byte[0];
        }
        return JSON.toJSONString(t, SerializerFeature.WriteClassName).getBytes(SystemConstants.CHARSET_UTF_8);
    }

    /**
     * <p>Description: 反序列化</p>
     * @param bytes 字节数组
     * @return T
     */
    @Override
    public T deserialize(byte[] bytes) throws SerializationException {
        if (null == bytes || bytes.length <= 0) {
            return null;
        }
        String str = new String(bytes, SystemConstants.CHARSET_UTF_8);
        return JSON.parseObject(str, clazz);
    }
}
