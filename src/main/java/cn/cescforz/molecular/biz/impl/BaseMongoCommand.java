package cn.cescforz.molecular.biz.impl;

import cn.cescforz.commons.lang.bean.model.Page;
import cn.cescforz.commons.lang.enums.ResponseEnum;
import cn.cescforz.commons.lang.exception.CustomRtException;
import cn.cescforz.molecular.bean.model.BaseEntity;
import cn.cescforz.molecular.bean.model.BaseUUIDGenModel;
import cn.cescforz.molecular.biz.Command;
import cn.cescforz.molecular.biz.QueryCommand;
import cn.cescforz.molecular.constant.MongoConstants;
import cn.cescforz.molecular.dao.BaseMongoDao;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.io.Serializable;
import java.util.Optional;

/**
 * <p>Description: MongoDB操作数据库实现类</p>
 *
 * @author developer developer@midea.com.cn
 * @version 1.00.00
 * @date Create in 2019/7/31 16:54
 */
@Slf4j
public class BaseMongoCommand<T extends BaseEntity> implements Command<T>, QueryCommand<T> {

    private BaseMongoDao<T> baseMongoDao;

    public BaseMongoCommand(BaseMongoDao<T> baseMongoDao) {
        this.baseMongoDao = baseMongoDao;
    }

    @Override
    public Object executeSave(T t) {
       return Optional.ofNullable(t).map(o -> baseMongoDao.insert(o)).orElseThrow(() -> new CustomRtException(ResponseEnum.RECORD_NOT_EXIST));
    }

    @Override
    public Object executeDelete(Serializable id) {
        return Optional.ofNullable(id).map(o -> {
            Query query = new Query((Criteria.where(BaseEntity.MONGO_ID).is(id)));
            long deletedCount = baseMongoDao.delete(query);
            return deletedCount != 0;
        }).orElseThrow(() -> new CustomRtException(ResponseEnum.RECORD_NOT_EXIST));
    }

    @Override
    public Object executeUpdate(T t) {
        return Optional.ofNullable(t).map(o -> baseMongoDao.updateAll(o)).orElseThrow(() -> new CustomRtException(ResponseEnum.RECORD_NOT_EXIST));
    }

    @Override
    public T getById(Serializable id) {
        return Optional.ofNullable(id).map(o -> {
            Query query = new Query((Criteria.where(BaseEntity.MONGO_ID).is(id)));
            return baseMongoDao.findOne(query);
        }).orElseThrow(() -> new CustomRtException(ResponseEnum.RECORD_NOT_EXIST));
    }

    @Override
    public Page<T> listByPage(Query query, Integer page, Integer size) {
        Sort sort = new Sort(Sort.Direction.DESC, MongoConstants.MONGO_CREATE_DATE);
        Pageable pageable = PageRequest.of(page,size,sort);
        return baseMongoDao.find(query, pageable);
    }
}
