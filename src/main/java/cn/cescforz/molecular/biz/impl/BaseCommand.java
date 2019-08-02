package cn.cescforz.molecular.biz.impl;

import cn.cescforz.molecular.bean.model.BaseAutoGenModel;
import cn.cescforz.molecular.biz.Command;

import java.io.Serializable;

/**
 * <p>Description: </p>
 *
 * @author cesc
 * @version 1.00.00
 * @date Create in 2019/7/31 16:53
 */
public class BaseCommand<T extends BaseAutoGenModel<T>> implements Command<T> {

    @Override
    public Object executeSave(T t) {
        return null;
    }

    @Override
    public Object executeDelete(Serializable id) {
        return null;
    }

    @Override
    public Object executeUpdate(T t) {
        return null;
    }
}
