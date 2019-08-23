package cn.cescforz.molecular.constant;


/**
 * <p>Description: redis常量类</p>
 *
 * @author developer developer@midea.com.cn
 * @version 1.00.00
 * @date Create in 2019/8/23 14:36
 */
public final class RedisConstants {


    private RedisConstants() {
        throw new AssertionError();
    }

    /** 幂等 */
    public static final String IDEMPOTENT = "idempotent";

}
