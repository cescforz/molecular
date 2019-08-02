package cn.cescforz.molecular.biz;

import java.io.Serializable;

/**
 * <p>Description: </p>
 *
 * @author cesc
 * @version 1.00.00
 * @date Create in 2019/7/31 11:36
 */
public interface Command<T> {


    Object executeSave(T t);

    Object executeDelete(Serializable id);

    Object executeUpdate(T t);
}
