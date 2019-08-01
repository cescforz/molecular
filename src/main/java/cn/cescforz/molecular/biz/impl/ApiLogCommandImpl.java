package cn.cescforz.molecular.biz.impl;

import cn.cescforz.molecular.bean.domain.ApiLogDO;
import cn.cescforz.molecular.biz.ApiLogCommand;
import cn.cescforz.molecular.dao.ApiLogDao;
import org.springframework.stereotype.Component;

/**
 * <p>Description: 接口调用记录处理</p>
 *
 * @author cesc
 * @version 1.00.00
 * @date Create in 2019/8/1 9:30
 */
@Component
public class ApiLogCommandImpl extends BaseMongoCommand<ApiLogDO> implements ApiLogCommand {

    private ApiLogDao apiLogDao;

    public ApiLogCommandImpl(ApiLogDao apiLogDao) {
        super(apiLogDao);
        this.apiLogDao = apiLogDao;
    }
}
