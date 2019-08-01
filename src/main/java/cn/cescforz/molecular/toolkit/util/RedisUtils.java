package cn.cescforz.molecular.toolkit.util;

import cn.cescforz.commons.lang.toolkit.util.StringTools;
import cn.cescforz.molecular.toolkit.tool.UniKeyGenerator;

import java.util.UUID;

/**
 * <p>Description: redis主键生成策略工具类</p>
 *
 * @author developer developer@midea.com.cn
 * @version 1.00.00
 * @date Create in 2019/7/2 10:44
 */
public final class RedisUtils {

    private RedisUtils() {
        throw new AssertionError();
    }

    /**
     * 生成redis key
     *
     * @param moduleName : 模块名
     * @param tableName  : 表名
     * @param column     : 列名
     * @param variable   : 变量
     * @return java.lang.String
     */
    public static String getRedisKey(String moduleName, String tableName, String column, String variable) {
        return StringTools.assembleStr(moduleName, ":", tableName, ":", column, ":", variable);
    }

    /**
     * 生成redis key
     *
     * @param moduleName : 模块名
     * @param tableName  : 表名
     * @param variable   : 变量
     * @return java.lang.String
     */
    public static String getRedisKey(String moduleName, String tableName, String variable) {
        return StringTools.assembleStr(moduleName, ":", tableName, ":", variable);
    }

    /**
     * uuid生成redis requestId（非纯数字）
     *
     * @return java.lang.String
     */
    public static String getRedisRequestId() {
        return UUID.randomUUID().toString().replaceAll("-", "").toLowerCase();
    }

    /**
     * 利用snowflake算法生成redis requestId（纯数字）
     *
     * @return java.lang.String
     */
    public static String getRedisRequestNumId() {
        return UniKeyGenerator.getInstance().nextId().toString();
    }
}
