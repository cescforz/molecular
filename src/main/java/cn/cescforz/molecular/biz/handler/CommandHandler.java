package cn.cescforz.molecular.biz.handler;

import cn.cescforz.molecular.biz.Command;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.Serializable;

/**
 * <p>Description: CQRS 读写分离思想的架构</p>
 *
 * @author cesc
 * @version 1.00.00
 * @date Create in 2019/7/31 11:37
 */
@Slf4j
@Component
public class CommandHandler {


    public <T> Object dispatchSave(Command<T> cmd, T t) {
        return cmd.executeSave(t);
    }

    public <T> Object dispatchDelete(Command<T> cmd, Serializable id) {
        return cmd.executeDelete(id);
    }

    public <T> Object dispatchUpdate(Command<T> cmd, T t) {
        return cmd.executeUpdate(t);
    }
}
