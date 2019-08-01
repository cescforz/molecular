package cn.cescforz.molecular.toolkit.util;


import com.google.common.util.concurrent.ThreadFactoryBuilder;

import java.util.concurrent.*;

/**
 * <p>©2019 Cesc. All Rights Reserved.</p>
 * <p>Description: 线程池工具类</p>
 *
 * @author cesc
 * @version v1.0
 * @date Create in 2019-01-01 22:04
 */
public final class ThreadPoolUtils {

    private ThreadPoolUtils() {}

    /**
     * corePoolSize：核心线程数
     * 核心线程会一直存活，及时没有任务需要执行
     * 当线程数小于核心线程数时，即使有线程空闲，线程池也会优先创建新线程处理
     * 设置allowCoreThreadTimeout=true（默认false）时，核心线程会超时关闭
     */
    private static final Integer COREPOOLSIZE = 50;

    /**
     * maxPoolSize：最大线程数
     * 当线程数>=corePoolSize，且任务队列已满时。线程池会创建新线程来处理任务
     * 当线程数=maxPoolSize，且任务队列已满时，线程池会拒绝处理任务而抛出异常
     */
    private static final Integer MAXIMUMPOOLSIZE = 100;

    /**
     * keepAliveTime：线程空闲时间
     * 当线程空闲时间达到keepAliveTime时，线程会退出，直到线程数量=corePoolSize
     * 如果allowCoreThreadTimeout=true，则会直到线程数量=0
     */
    private static final Long KEEPALIVETIME = 100000L;

    private static final TimeUnit UNIT = TimeUnit.MILLISECONDS;

    /**
     * workQueue:一个阻塞队列，用来存储等待执行的任务，这个参数的选择也很重要，会对线程池的运行过程产生重大影响，一般来说，这里的阻塞队列有以下几种选择：
     * ArrayBlockingQueue;
     * LinkedBlockingQueue;
     * SynchronousQueue;
     * PriorityBlockingQueue
     * ArrayBlockingQueue和PriorityBlockingQueue使用较少，一般使用LinkedBlockingQueue和Synchronous。线程池的排队策略与BlockingQueue有关。
     *
     */
    private static final BlockingQueue<Runnable> WORKQUEUE = new LinkedBlockingQueue<>();

    /**
     * threadFactory：线程工厂，主要用来创建线程
     */
    private static ThreadFactory THREADFACTORY = new ThreadFactoryBuilder().setNameFormat("mq-task-%d").build();

    /**
     * queueCapacity：任务队列容量（阻塞队列）
     * 当核心线程数达到最大时，新任务会放在队列中排队等待执行
     *
     * allowCoreThreadTimeout：允许核心线程超时
     *
     * rejectedExecutionHandler：任务拒绝处理器
     * 两种情况会拒绝处理任务：
     * 当线程数已经达到maxPoolSize，切队列已满，会拒绝新任务
     * 当线程池被调用shutdown()后，会等待线程池里的任务执行完毕，再shutdown。如果在调用shutdown()和线程池真正shutdown之间提交任务，会拒绝新任务
     * 线程池会调用rejectedExecutionHandler来处理这个任务。如果没有设置默认是AbortPolicy，会抛出异常
     * ThreadPoolExecutor类有几个内部实现类来处理这类情况：
     *      AbortPolicy 丢弃任务，抛运行时异常
     *      CallerRunsPolicy 执行任务
     *      DiscardPolicy 忽视，什么都不会发生
     *      DiscardOldestPolicy 从队列中踢出最先进入队列（最后一个执行）的任务
     * 实现RejectedExecutionHandler接口，可自定义处理器
     */

    private static class CreateExecutorService{
        private static ExecutorService executorService = new ThreadPoolExecutor(COREPOOLSIZE, MAXIMUMPOOLSIZE, KEEPALIVETIME, UNIT, WORKQUEUE, THREADFACTORY);
    }

    public static ExecutorService getInstance() {
        return CreateExecutorService.executorService;
    }
}
