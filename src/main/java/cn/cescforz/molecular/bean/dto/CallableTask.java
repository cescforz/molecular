package cn.cescforz.molecular.bean.dto;

import cn.cescforz.molecular.service.ICallableService;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;
import java.util.concurrent.Callable;

/**
 * <p>Description: 实现Callable接口的任务包装DTO</p>
 *
 * @author developer developer@midea.com.cn
 * @version 1.00.00
 * @date Create in 2019/5/27 16:03
 */
@Data
@Slf4j
public class CallableTask implements Callable<Object>, Serializable {


    private static final long serialVersionUID = 8082307687960931986L;

    private String executorName;
    private ICallableService callableService;
    private Object param;

    @Override
    public Object call() {
        try {
            return callableService.call(param);
        } catch (Exception e) {
            log.error("执行任务出错:",e);
        }
        return null;
    }
}
