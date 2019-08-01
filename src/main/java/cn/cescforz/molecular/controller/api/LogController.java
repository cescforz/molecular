package cn.cescforz.molecular.controller.api;

import cn.cescforz.commons.lang.constant.SystemConstants;
import cn.cescforz.commons.lang.version.annotation.ApiVersion;
import cn.cescforz.molecular.bean.domain.ApiLogDO;
import cn.cescforz.molecular.biz.ApiLogCommand;
import cn.cescforz.molecular.biz.CommandHandler;
import cn.cescforz.molecular.controller.BaseController;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

/**
 * <p>©2019 Cesc. All Rights Reserved.</p>
 * <p>Description: </p>
 *
 * @author cesc
 * @version v1.0
 * @date Create in 2019-03-08 10:43
 */
@Slf4j(topic = "LogController")
@RestController
@RequestMapping(value = "/logs")
public class LogController extends BaseController {


    private ApiLogCommand apiLogCommand;

    private CommandHandler commandHandler;


    @ApiVersion(2)
    @ResponseBody
    @GetMapping("/{version}/add")
    public Object add(@PathVariable(value = "version") String version){
        ApiLogDO o = new ApiLogDO();
        o.setArgs(version);
        o.setCreateDate(new Date());
        o.setUpdateDate(new Date());
        o.setConsumeTime(System.currentTimeMillis());
        return commandHandler.dispatchSave(apiLogCommand,o);
    }

    @RequestMapping("/{id}")
    @ResponseBody
    public Object test(@PathVariable Integer id){
        return 1/id;
    }

    @Autowired
    public void setApiLogCommand(ApiLogCommand apiLogCommand) {
        this.apiLogCommand = apiLogCommand;
    }
    @Autowired
    public void setCommandHandler(CommandHandler commandHandler) {
        this.commandHandler = commandHandler;
    }
}
