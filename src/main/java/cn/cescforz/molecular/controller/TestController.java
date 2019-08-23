package cn.cescforz.molecular.controller;

import cn.cescforz.molecular.bean.domain.ApiLogDO;
import cn.cescforz.molecular.bean.domain.ExecutorDO;
import cn.cescforz.molecular.biz.ApiLogCommand;
import cn.cescforz.molecular.biz.handler.CommandHandler;
import cn.cescforz.molecular.service.ExecutorPoolService;
import cn.cescforz.molecular.toolkit.util.KeyUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

/**
 * <p>Description: </p>
 *
 * @author developer developer@midea.com.cn
 * @version 1.00.00
 * @date Create in 2019/8/23 16:11
 */
@Slf4j(topic = "TestController")
@RestController
@RequestMapping(value = "/test")
public class TestController {

    private ApiLogCommand apiLogCommand;

    private CommandHandler commandHandler;

    private ExecutorPoolService executorPoolService;

    @GetMapping("/t")
    public Object test3() {
        Long id = KeyUtils.generateId();
        ExecutorDO executorDO = new ExecutorDO();
        executorDO.setId(id);
        executorDO.setExcutorName(id.toString());
        executorDO.setKeepAliveTime(id);
        executorDO.setCreateDate(new Date());
        return executorPoolService.save(executorDO);
    }

    @Autowired
    public void setApiLogCommand(ApiLogCommand apiLogCommand) {
        this.apiLogCommand = apiLogCommand;
    }
    @Autowired
    public void setCommandHandler(CommandHandler commandHandler) {
        this.commandHandler = commandHandler;
    }
    @Autowired
    public void setExecutorPoolService(ExecutorPoolService executorPoolService) {
        this.executorPoolService = executorPoolService;
    }
}
