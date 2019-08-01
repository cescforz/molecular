package cn.cescforz.molecular.dao.impl;

import cn.cescforz.molecular.bean.domain.ApiLogDO;
import cn.cescforz.molecular.dao.ApiLogDao;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Repository;

/**
 * <p>©2019 Cesc. All Rights Reserved.</p>
 * <p>Description: </p>
 *
 * @author cesc
 * @version v1.0
 * @date Create in 2019-03-08 11:31
 */
@Repository
public class ApiLogDaoImpl extends BaseMongoDaoImpl<ApiLogDO> implements ApiLogDao {

    public ApiLogDaoImpl(MongoTemplate mongoTemplate) {
        super(mongoTemplate);
    }
}
