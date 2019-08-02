package cn.cescforz.molecular.biz;

import cn.cescforz.commons.lang.bean.model.Page;
import org.springframework.data.mongodb.core.query.Query;

import java.io.Serializable;

/**
 * <p>Description: </p>
 *
 * @author cesc
 * @version 1.00.00
 * @date Create in 2019/7/31 11:45
 */
public interface QueryCommand<T> {

    /**
     * 通过id获取实体
     * @param id :
     * @return T
     */
    T getById(Serializable id);

    /**
     * 根据查询条件分页查询
     * @param query :
     * @param page :
     * @param size :
     * @return cn.cescforz.commons.lang.bean.model.Page<T>
     */
    Page<T> listByPage(Query query, Integer page, Integer size);

}
