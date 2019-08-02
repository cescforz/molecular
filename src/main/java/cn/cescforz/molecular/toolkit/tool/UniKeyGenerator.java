package cn.cescforz.molecular.toolkit.tool;

import cn.cescforz.commons.lang.toolkit.tool.KeyGenerator;
import cn.cescforz.molecular.constant.ModuleConstants;

/**
 * <p>Description: 本模块唯一键生成器</p>
 *
 * @author cesc
 * @version 1.00.00
 * @date Create in 2019/8/1 16:06
 */
public final class UniKeyGenerator {

    private UniKeyGenerator() {
       throw new AssertionError();
    }

    private static class CreateKeyGenerator {
        private static KeyGenerator keyGenerator = new KeyGenerator(ModuleConstants.WORKER_ID, ModuleConstants.DATACENTER_ID);
    }

    public static KeyGenerator getInstance() {
        return CreateKeyGenerator.keyGenerator;
    }
}
