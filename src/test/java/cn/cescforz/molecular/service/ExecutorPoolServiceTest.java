package cn.cescforz.molecular.service;

import cn.cescforz.molecular.MolecularApplicationTests;
import cn.cescforz.molecular.bean.domain.ApiLogDO;
import cn.cescforz.molecular.biz.ApiLogCommand;
import cn.cescforz.molecular.biz.handler.CommandHandler;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;


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
        ApiLogDO apiLogDO = new ApiLogDO();
        System.out.println(apiLogDO);


        //Object o = commandHandler.dispatchSave(apiLogCommand, apiLog);
    }
}
