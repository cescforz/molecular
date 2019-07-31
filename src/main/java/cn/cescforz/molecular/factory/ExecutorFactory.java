package cn.cescforz.molecular.factory;

import cn.cescforz.commons.lang.constant.SystemConstants;
import cn.cescforz.commons.lang.constant.UnitConstants;
import cn.cescforz.commons.lang.enums.ResponseEnum;
import cn.cescforz.commons.lang.exception.CustomRtException;
import cn.cescforz.molecular.bean.domain.ExecutorDO;
import cn.cescforz.molecular.service.ExecutorPoolService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.*;

/**
 * <p>Description: 线程工厂类的作用：根据数据库初始化所有线程池</p>
 *
 * @author cesc
 * @version 1.00.00
 * @date Create in 2019/7/31 13:52
 */
@Slf4j
@Component
public class ExecutorFactory {

    private ExecutorPoolService executorPoolService;


    @PostConstruct
    private void initExecutors(){
        QueryWrapper<ExecutorDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("effect_status", SystemConstants.CONFIRM);
        List<ExecutorDO> executorList = executorPoolService.list(queryWrapper);
        ExecutorRepository repository = ExecutorRepository.getInstance();
        executorList.forEach(executor -> repository.put(executor.getExcutorName(),createThreadPoolExecutor(executor)));
    }


    @SuppressWarnings("unchecked")
    private ExecutorService createThreadPoolExecutor(ExecutorDO executor) {
        String excutorName = executor.getExcutorName();
        ExecutorService exec;
        String queueType = executor.getQueueType();
        ThreadFactory threadFactory = new ThreadFactoryBuilder().setNameFormat(excutorName).build();
        String timeType = executor.getTimeType();
        TimeUnit timeUnit;
        switch (timeType) {
            case UnitConstants.NANOSECONDS:
                timeUnit = TimeUnit.NANOSECONDS;
                break;
            case UnitConstants.MICROSECONDS:
                timeUnit = TimeUnit.MICROSECONDS;
                break;
            case UnitConstants.SECONDS:
                timeUnit = TimeUnit.SECONDS;
                break;
            case UnitConstants.MINUTES:
                timeUnit = TimeUnit.MINUTES;
                break;
            case UnitConstants.HOURS:
                timeUnit = TimeUnit.HOURS;
                break;
            case UnitConstants.DAYS:
                timeUnit = TimeUnit.DAYS;
                break;
            default:
                timeUnit = TimeUnit.MILLISECONDS;
        }
        if (StringUtils.equals(SystemConstants.QUEUE_TYPE_LIMITED, queueType)) {
            exec = new ThreadPoolExecutor(executor.getCorePoolSize(),
                    executor.getMaxPoolSize(),
                    executor.getKeepAliveTime(),
                    timeUnit,
                    new ArrayBlockingQueue(executor.getMaxQueueSize()),
                    threadFactory);
            log.info("初始化线程池-{}成功",excutorName);
        } else {
            exec = new ThreadPoolExecutor(executor.getCorePoolSize(),
                    executor.getMaxPoolSize(),
                    executor.getKeepAliveTime(),
                    timeUnit,
                    new LinkedBlockingQueue(),
                    threadFactory);
            log.info("初始化线程池-{}成功",excutorName);
        }
        return exec;
    }



    public static ExecutorService getExecutorByName(String executorName){
        ExecutorService executorService = ExecutorRepository.getInstance().get(executorName);
        Optional<ExecutorService> optional = Optional.ofNullable(executorService);
        return optional.orElseThrow(() -> new CustomRtException(ResponseEnum.RECORD_NOT_EXIST));
    }



    @Autowired
    public void setExecutorPoolService(ExecutorPoolService executorPoolService) {
        this.executorPoolService = executorPoolService;
    }
}
