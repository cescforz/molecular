package cn.cescforz.molecular.toolkit.util;

import cn.cescforz.molecular.bean.dto.CallableTask;
import cn.cescforz.molecular.bean.dto.RunnableTask;
import cn.cescforz.molecular.factory.ExecutorFactory;
import cn.cescforz.molecular.service.ICallableService;
import cn.cescforz.molecular.service.IThreadService;

import java.util.concurrent.*;

/**
 * <p>Description: 线程池工具类</p>
 *
 * @author developer developer@midea.com.cn
 * @version 1.00.00
 * @date Create in 2019/5/27 16:06
 */
public final class ExecutorUtils {

    private ExecutorUtils(){throw new AssertionError();}

    /**
     * 执行runnable接口的任务
     */
    public static void addThreadToPool(String executorName, IThreadService threadService, Object param) {
        RunnableTask task = new RunnableTask();
        task.setExecutorName(executorName);
        task.setThreadService(threadService);
        task.setParam(param);
        ExecutorFactory.getExecutorByName(executorName).execute(task);
    }


    /**
     * 执行callable接口的任务
     */
    public static Future addCallableTaskToPool(String executorName, ICallableService callableService, Object param) {
        CallableTask task = new CallableTask();
        task.setExecutorName(executorName);
        task.setCallableService(callableService);
        task.setParam(param);
        return ExecutorFactory.getExecutorByName(executorName).submit(task);
    }


    /**
     * 2中情况下进行饱和策略（1.线程池关闭的过程中提交任务，2.有界任务队列已经满了，线程数已经达到最大线程数）,已经有了4种饱和策略实现
     * {@link ThreadPoolExecutor.AbortPolicy}
     * {@link ThreadPoolExecutor.CallerRunsPolicy}
     * {@link ThreadPoolExecutor.DiscardOldestPolicy}
     * {@link ThreadPoolExecutor.DiscardPolicy}
     * 默认的是AbortPolicy,直接抛出{@link RejectedExecutionException}，以防止提交任务线程终止
     * 捕获异常处理，或者实现自己的饱和策略
     */
    public static void setRejectedExecutionHandler(String executorName, RejectedExecutionHandler handler) {
        //获取对应的线程池执行任务
        ExecutorService exec = ExecutorFactory.getExecutorByName(executorName);
        if (exec instanceof ThreadPoolExecutor) {
            ((ThreadPoolExecutor) exec).setRejectedExecutionHandler(handler);
        }
    }


    /**
     * 获取任务队列的大小
     */
    public static int getTaskQueueSize(String executorName) {
        //获取对应的线程池执行任务
        ExecutorService exec = ExecutorFactory.getExecutorByName(executorName);
        return ((ThreadPoolExecutor) exec).getQueue().size();
    }


    /**
     * 关闭线程池的方法，此方法等待当前正在执行的任务和队列中等待的任务全部执行完毕后关闭
     */
    public static void shutDownExecutor(String executorName) {
        ExecutorFactory.getExecutorByName(executorName).shutdown();
    }

    public static int getAliveThread(String executorName) {
        //获取对应的线程池执行任务
        ExecutorService exec = ExecutorFactory.getExecutorByName(executorName);
        return ((ThreadPoolExecutor) exec).getActiveCount();
    }

}
