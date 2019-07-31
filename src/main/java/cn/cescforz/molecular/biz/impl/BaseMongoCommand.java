package cn.cescforz.molecular.biz.impl;

import cn.cescforz.commons.lang.enums.ResponseEnum;
import cn.cescforz.commons.lang.exception.CustomRtException;
import cn.cescforz.molecular.bean.model.BaseUUIDGenModel;
import cn.cescforz.molecular.biz.Command;
import cn.cescforz.molecular.constant.MongoConstants;
import cn.cescforz.molecular.dao.BaseMongoDao;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.util.Optional;

/**
 * <p>Description: </p>
 *
 * @author developer developer@midea.com.cn
 * @version 1.00.00
 * @date Create in 2019/7/31 16:54
 */
@Slf4j
public class BaseMongoCommand<T extends BaseUUIDGenModel<T>> implements Command<T> {

    private BaseMongoDao<T> baseMongoDao;

    public BaseMongoCommand(BaseMongoDao<T> baseMongoDao) {
        this.baseMongoDao = baseMongoDao;
    }

    @Override
    public Object executeSave(T t) {
       return Optional.ofNullable(t).map(o -> baseMongoDao.insert(o)).orElseThrow(() -> new CustomRtException(ResponseEnum.RECORD_NOT_EXIST));
    }

    @Override
    public Object executeDelete(Object id) {
        return Optional.ofNullable(id).map(o -> {
            Query query = new Query((Criteria.where(MongoConstants.MONGO_ID).is(id)));
            baseMongoDao.delete(query);
            return true;
        }).orElseThrow(() -> new CustomRtException(ResponseEnum.RECORD_NOT_EXIST));
    }

    @Override
    public Object executeUpdate(T t) {
        return Optional.ofNullable(t).map(o -> baseMongoDao.updateAll(o)).orElseThrow(() -> new CustomRtException(ResponseEnum.RECORD_NOT_EXIST));
    }
}
