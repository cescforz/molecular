package cn.cescforz.molecular.service;

import cn.cescforz.commons.lang.constant.SystemConstants;
import cn.cescforz.molecular.MolecularApplicationTests;
import cn.cescforz.molecular.bean.domain.ApiLogDO;
import cn.cescforz.molecular.bean.domain.ExecutorDO;
import cn.cescforz.molecular.biz.ApiLogCommand;
import cn.cescforz.molecular.biz.Command;
import cn.cescforz.molecular.biz.CommandHandler;
import cn.cescforz.molecular.factory.ExecutorFactory;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;

import java.util.Date;
import java.util.List;
import java.util.UUID;
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

    @Autowired
    private ApiLogCommand apiLogCommand;

    @Autowired
    private CommandHandler commandHandler;


    @Test
    public void test() throws Exception{



        //Object o = commandHandler.dispatchSave(apiLogCommand, apiLog);
    }
}
