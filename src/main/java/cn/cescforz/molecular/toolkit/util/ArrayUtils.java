package cn.cescforz.molecular.toolkit.util;

/**
 * <p>Description: </p>
 *
 * @author developer developer@midea.com.cn
 * @version 1.00.00
 * @date Create in 2019/8/23 14:22
 */
public final class ArrayUtils {

    private ArrayUtils() {
        throw new AssertionError();
    }


    /**
     * 判断数据是否为空
     *
     * @param array 长度
     * @return 数组对象为null或者长度为 0 时，返回 false
     */
    public static boolean isEmpty(Object[] array) {
        return array == null || array.length == 0;
    }

    /**
     * 判断数组是否不为空
     *
     * @param array 数组
     * @return 数组对象内含有任意对象时返回 true
     * @see ArrayUtils#isEmpty(Object[])
     */
    public static boolean isNotEmpty(Object[] array) {
        return !isEmpty(array);
    }
}
