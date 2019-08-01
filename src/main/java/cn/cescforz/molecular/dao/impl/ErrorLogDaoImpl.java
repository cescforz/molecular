package cn.cescforz.molecular.dao.impl;

import cn.cescforz.molecular.bean.domain.ErrorLogDO;
import cn.cescforz.molecular.dao.ErrorLogDao;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Repository;

/**
 * <p>©2019 Cesc. All Rights Reserved.</p>
 * <p>Description: </p>
 *
 * @author cesc
 * @version v1.0
 * @date Create in 2019-01-14 00:36
 */
@Repository
public class ErrorLogDaoImpl extends BaseMongoDaoImpl<ErrorLogDO> implements ErrorLogDao {

    public ErrorLogDaoImpl(MongoTemplate mongoTemplate) {
        super(mongoTemplate);
    }
}
