package cn.cescforz.molecular.controller.api;

import cn.cescforz.commons.lang.enums.ResponseEnum;
import cn.cescforz.commons.lang.exception.CustomRtException;
import cn.cescforz.commons.lang.version.annotation.ApiVersion;
import cn.cescforz.molecular.bean.model.BaseEntity;
import cn.cescforz.molecular.biz.ApiLogCommand;
import cn.cescforz.molecular.biz.handler.CommandHandler;
import cn.cescforz.molecular.biz.handler.QueryCommandHandler;
import cn.cescforz.molecular.controller.BaseController;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
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

    private QueryCommandHandler queryCommandHandler;

    /**
     * v2
     * @return java.lang.Object
     * @throws
     */
    @ApiVersion(2)
    @GetMapping("/{version}/add/{id}")
    public Object add(@PathVariable String id){


        return queryCommandHandler.dispatchGetById(apiLogCommand,id);
    }

    @RequestMapping("/{id}")
    public Object test(@PathVariable String id){
        return commandHandler.dispatchDelete(apiLogCommand,id);
    }

    @GetMapping("/test1")
    public Object test1() {
        Query query = new Query(Criteria.where(BaseEntity.CREATED_DT).lte(new Date()));
        return queryCommandHandler.dispatchListByPage(apiLogCommand, query, 0, 10);
    }

    @GetMapping("/test2")
    public Object test2() {
        throw new CustomRtException(ResponseEnum.SYSTEM_ERROR);
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
    public void setQueryCommandHandler(QueryCommandHandler queryCommandHandler) {
        this.queryCommandHandler = queryCommandHandler;
    }
}
