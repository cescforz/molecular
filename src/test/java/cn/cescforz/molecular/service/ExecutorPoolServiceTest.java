package cn.cescforz.molecular.service;

import cn.cescforz.commons.lang.constant.SystemConstants;
import cn.cescforz.molecular.MolecularApplicationTests;
import cn.cescforz.molecular.bean.domain.ExecutorDO;
import cn.cescforz.molecular.factory.ExecutorFactory;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;

import java.util.List;
import java.util.concurrent.ExecutorService;


/**
 * <p>Description: </p>
 *
 * @author developer developer@midea.com.cn
 * @version 1.00.00
 * @date Create in 2019/7/31 15:31
 */
@Slf4j
@Rollback(value = false)
public class ExecutorPoolServiceTest extends MolecularApplicationTests {

    @Autowired
    private ExecutorPoolService executorPoolService;


    @Test
    public void test() throws Exception{


        QueryWrapper<ExecutorDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("effect_status", SystemConstants.CONFIRM);
        List<ExecutorDO> executorList = executorPoolService.list(queryWrapper);

    }
}
