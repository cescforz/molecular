package cn.cescforz.molecular.dao.impl;

import cn.cescforz.commons.lang.annotation.Fixed;
import cn.cescforz.commons.lang.bean.model.Page;
import cn.cescforz.molecular.bean.model.BaseUUIDGenModel;
import cn.cescforz.molecular.constant.MongoConstants;
import cn.cescforz.molecular.dao.BaseMongoDao;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.util.List;

/**
 * <p>©2019 Cesc. All Rights Reserved.</p>
 * <p>Description: </p>
 *
 * @author cesc
 * @version v1.0
 * @date Create in 2019-01-13 23:40
 */
@Slf4j
public class BaseMongoDaoImpl<T extends BaseUUIDGenModel<T>> implements BaseMongoDao<T> {

    protected MongoTemplate mongoTemplate;
    @SuppressWarnings("unchecked")
    private Class<T> entityClass = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    private String collectionName;

    public BaseMongoDaoImpl(MongoTemplate mongoTemplate) {
        Document doc = entityClass.getAnnotation(Document.class);
        if (null != doc) {
            this.collectionName = doc.collection();
        } else {
            this.collectionName = entityClass.getSimpleName().toLowerCase();
        }
        this.mongoTemplate = mongoTemplate;
    }


    @Override
    public T findOne(Query query) {
        return mongoTemplate.findOne(query, entityClass, collectionName);
    }

    @Override
    public List<T> find(Query query) {
        return mongoTemplate.find(query, entityClass, collectionName);
    }

    @Override
    public Page<T> find(Query query, Pageable pageable) {
        Page<T> pageList = new Page<>();
        if (pageable != null) {
            long totalCount = count(query);
            int pageCount = (int) (totalCount / pageable.getPageSize());
            if (totalCount % pageable.getPageSize() != 0) {
                pageCount += 1;
            }
            query.with(pageable);
            pageList.makePageList(null, pageable.getPageSize(), totalCount, pageable.getPageNumber(), pageCount);
        }
        pageList.setData(mongoTemplate.find(query, entityClass, collectionName));
        return pageList;
    }

    @Override
    public boolean updateAll(T t) {
        boolean flag = true;
        try {
            Update update = new Update();
            setClassFieldToUpdate(t, entityClass, update);
            long modifiedCount = mongoTemplate.updateFirst(Query.query(Criteria.where(MongoConstants.MONGO_ID).is(t.getId())), update, entityClass, collectionName).getModifiedCount();
            log.info("成功修改条数:{}", modifiedCount);
        } catch (Exception e) {
            log.error("更新出错:", e);
            flag = false;
        }
        return flag;
    }

    @Override
    public long update(Query query, Update update) {
        return mongoTemplate.updateFirst(query, update, entityClass, collectionName).getModifiedCount();
    }

    @Override
    public long updateMulti(Query query, Update update) {
        return mongoTemplate.updateMulti(query, update, entityClass, collectionName).getModifiedCount();
    }

    @Override
    public T findAndModify(Query query, Update update, boolean isNew) {
        FindAndModifyOptions findAndModifyOptions = new FindAndModifyOptions();
        findAndModifyOptions.returnNew(isNew);
        findAndModifyOptions.upsert(true);
        return mongoTemplate.findAndModify(query, update, findAndModifyOptions, entityClass, collectionName);
    }

    @Override
    public boolean insert(T t) {
        boolean flag = true;
        try {
            mongoTemplate.insert(t, collectionName);
        } catch (Exception e) {
            log.error("新增记录出错:", e);
            flag = false;
        }
        return flag;
    }

    @Override
    public long delete(Query query) {
        return mongoTemplate.remove(query, entityClass, collectionName).getDeletedCount();
    }

    @Override
    public void insert(List<T> ts) {
        mongoTemplate.insertAll(ts);
    }

    @Override
    public long count(Query query) {
        return mongoTemplate.count(query, entityClass, collectionName);
    }

    @Override
    public boolean isExist(Query query) {
        return mongoTemplate.exists(query, entityClass, collectionName);
    }


    /**
     * <p>Description: 通过反射将对象的值设置到 update 中</p>
     *
     * @param obj
     * @param curClass
     * @param update
     */
    private void setClassFieldToUpdate(Object obj, Class curClass, Update update) {
        Field[] objFields = curClass.getDeclaredFields();
        try {
            for (Field field : objFields) {
                if (Modifier.isFinal(field.getModifiers()) || Modifier.isPublic(field.getModifiers()) || Modifier.isStatic(field.getModifiers())) {
                    continue;
                }
                field.setAccessible(true);
                boolean isFixed = false;
                Annotation[] annotations = field.getAnnotations();
                for (Annotation annotation : annotations) {
                    if (annotation.getClass() == Fixed.class) {
                        isFixed = true;
                    }
                }
                if (isFixed) {
                    // 如果字段是不变的则不添加在 update 中
                    continue;
                }
                update.set(field.getName(), field.get(obj));
            }
            if (curClass.getSuperclass() != null) {
                // 递归获取父类的字段值
                setClassFieldToUpdate(obj, curClass.getSuperclass(), update);
            }
        } catch (Exception e) {
            log.error("setClassFieldToUpdate:", e);
        }
    }
}
