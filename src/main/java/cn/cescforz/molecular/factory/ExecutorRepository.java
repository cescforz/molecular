package cn.cescforz.molecular.factory;

import com.google.common.collect.Maps;

import java.util.Map;
import java.util.concurrent.ExecutorService;

/**
 * <p>©2019 Cesc. All Rights Reserved.</p>
 * <p>Description: 线程仓库类的作用：初始化一个 Map,key 为线程池名，name 为线程池，提供 put 线程池，remove 线程池，get 线程池的方法</p>
 *
 * @author cesc
 * @version v1.0
 * @date Create in 2019-05-14 22:37
 */
public class ExecutorRepository {

    private Map<String, ExecutorService> executors;

    private ExecutorRepository() {
        executors = Maps.newHashMap();
    }

    private static class CreateExecutorRepository {
        private static final ExecutorRepository REPOSITORY = new ExecutorRepository();
    }

    public static ExecutorRepository getInstance() {
        return CreateExecutorRepository.REPOSITORY;
    }

    /**
     * <p>Description: put线程池的方法(同步方法)</p>
     *
     * @param executorName 线程名字
     * @param exec         线程池
     */
    public synchronized void put(String executorName, ExecutorService exec) {
        executors.put(executorName, exec);
    }

    /**
     * <p>Description: remove线程池的方法(同步方法)</p>
     *
     * @param executorName 线程名字
     */
    public synchronized void remove(String executorName) {
        executors.remove(executorName);
    }


    /**
     * <p>Description: 查找线程池的方法(普通方法)</p>
     *
     * @param executorName 线程名字
     * @return java.util.concurrent.ExecutorPoolService
     */
    public ExecutorService get(String executorName) {
        return executors.get(executorName);
    }
}
