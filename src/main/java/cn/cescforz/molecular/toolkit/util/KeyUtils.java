package cn.cescforz.molecular.toolkit.util;

import cn.cescforz.molecular.toolkit.tool.UniKeyGenerator;

/**
 * <p>Description: 主键生成工具类</p>
 *
 * @author cesc
 * @version 1.00.00
 * @date Create in 2019/8/2 11:37
 */
public final class KeyUtils {

    private KeyUtils() {
        throw new AssertionError();
    }


    public static Long generateId() {
        return UniKeyGenerator.getInstance().nextId();
    }
}
