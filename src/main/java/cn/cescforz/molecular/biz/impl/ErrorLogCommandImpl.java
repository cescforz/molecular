package cn.cescforz.molecular.biz.impl;

import cn.cescforz.molecular.bean.domain.ErrorLogDO;
import cn.cescforz.molecular.biz.ErrorLogCommand;
import cn.cescforz.molecular.dao.ErrorLogDao;
import org.springframework.stereotype.Component;

/**
 * <p>Description: 异常日志处理类</p>
 *
 * @author cesc
 * @version 1.00.00
 * @date Create in 2019/8/1 10:29
 */
@Component
public class ErrorLogCommandImpl extends BaseMongoCommand<ErrorLogDO> implements ErrorLogCommand {

    private ErrorLogDao errorLogDao;

    public ErrorLogCommandImpl(ErrorLogDao errorLogDao) {
        super(errorLogDao);
        this.errorLogDao = errorLogDao;
    }
}
