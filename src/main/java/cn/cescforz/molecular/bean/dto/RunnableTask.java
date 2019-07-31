package cn.cescforz.molecular.bean.dto;

import cn.cescforz.molecular.service.IThreadService;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;

/**
 * <p>Description: 实现Runnable接口的任务包装DTO</p>
 *
 * @author developer developer@midea.com.cn
 * @version 1.00.00
 * @date Create in 2019/5/27 15:59
 */
@Data
@Slf4j
public class RunnableTask implements Runnable, Serializable {

    private static final long serialVersionUID = 2083433562784549664L;

    private String executorName;
    private IThreadService threadService;
    private Object param;


    @Override
    public void run() {
        try {
            threadService.run(param);
        } catch (Exception e) {
            log.info("执行任务出错:",e);
        }
    }
}
