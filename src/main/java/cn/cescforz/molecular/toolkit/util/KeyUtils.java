package cn.cescforz.molecular.toolkit.util;

import cn.cescforz.commons.encrypt.util.MD5EncryptUtil;
import cn.cescforz.molecular.toolkit.tool.UniKeyGenerator;
import com.alibaba.fastjson.JSON;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Optional;

/**
 * <p>Description: 生成key值工具类</p>
 *
 * @author cesc
 * @version 1.00.00
 * @date Create in 2019/8/2 11:37
 */
public final class KeyUtils {

    private KeyUtils() {
        throw new AssertionError();
    }

    /**
     * 主键生成
     * @return java.lang.Long
     */
    public static Long generateId() {
        return UniKeyGenerator.getInstance().nextId();
    }

    /**
     * 对接口的参数进行处理生成固定key
     * @param method :
     * @param args :
     * @return java.lang.String
     */
    public static String generateApiKey(Method method, Object... args) {
        StringBuffer sb = new StringBuffer(method.toString());
        Arrays.stream(args).forEach(o -> sb.append(Optional.ofNullable(o).map(JSON::toJSONString).orElse("-")));
        // 进行md5等长加密
        return MD5EncryptUtil.encrypt(sb.toString());
    }


}
