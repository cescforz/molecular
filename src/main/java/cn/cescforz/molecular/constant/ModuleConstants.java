package cn.cescforz.molecular.constant;

import com.mchange.util.AssertException;

/**
 * <p>Description: 模块常数</p>
 *
 * @author cesc
 * @version 1.00.00
 * @date Create in 2019/8/1 14:46
 */
public final class ModuleConstants {

    public ModuleConstants() {
        throw new AssertException();
    }

    /**系统模块类型*/
    public static final Integer FOO_MODULE_TYPE = 1;
    public static final Long WORKER_ID = 1L;
    public static final Long DATACENTER_ID = 1L;

    /**
     * 默认过期时长，单位：秒
     */
    public static final Long DEFAULT_EXPIRE = 60L * 60L;

    /**接口调用切点表达式*/
    public static final String API_POINT_CUT = "execution(* cn.cescforz.molecular.controller.api.*.*(..))";
}
