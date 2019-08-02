package cn.cescforz.molecular.biz.handler;

import cn.cescforz.commons.lang.bean.model.Page;
import cn.cescforz.molecular.biz.QueryCommand;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import java.io.Serializable;

/**
 * <p>Description: </p>
 *
 * @author developer developer@midea.com.cn
 * @version 1.00.00
 * @date Create in 2019/8/2 9:24
 */
@Slf4j
@Component
public class QueryCommandHandler {

    /**
     * 通过主键获取对象
     * <p>
     *     <T> 不是返回值，表示传入参数有泛型
     *     泛型的声明：1. 必须在方法的修饰符（public,static,final,abstract等）之后，返回值声明之前
     *               2. 泛型类一样，可以声明多个泛型，用逗号隔开
     * </p>
     * @param cmd :
     * @param id :
     * @return T
     */
    public <T> T dispatchGetById(QueryCommand<T> cmd, Serializable id) {
        return cmd.getById(id);
    }

    public <T> Page<T> dispatchListByPage(QueryCommand<T> cmd, Query q, Integer page, Integer size) {
        return cmd.listByPage(q, page, size);
    }
}
