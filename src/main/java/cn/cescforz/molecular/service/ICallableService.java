package cn.cescforz.molecular.service;

/**
 * <p>Description: 实现Callable接口的任务接口
 *  任务只要实现这个接口即可，即是实现了Callable接口的任务</p>
 *
 * @author cesc
 * @version 1.00.00
 * @date Create in 2019/5/27 15:58
 */
public interface ICallableService {

    Object call(Object o);
}
