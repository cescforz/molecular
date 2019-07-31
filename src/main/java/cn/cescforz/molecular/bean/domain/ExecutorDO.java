package cn.cescforz.molecular.bean.domain;

import cn.cescforz.molecular.bean.model.BaseAutoGenModel;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * <p>Description: 线程池数据库实体</p>
 *
 * @author cesc
 * @version 1.00.00
 * @date Create in 2019/7/31 11:32
 */
@Data
@NoArgsConstructor
@TableName("t_thread_pool_executor")
@EqualsAndHashCode(callSuper = true)
public class ExecutorDO extends BaseAutoGenModel<ExecutorDO> {

    private static final long serialVersionUID = -7519569220931159695L;
    /** 线程池名 */
    @TableField("excutor_name")
    private String excutorName;
    /** 核心线程数 */
    @TableField("core_pool_size")
    private Integer corePoolSize;
    /** 最大线程数 */
    @TableField("max_pool_size")
    private Integer maxPoolSize;
    /** 任务队列大小 */
    @TableField("max_queue_size")
    private Integer maxQueueSize;
    /** 线程空闲时间 */
    @TableField("keep_alive_time")
    private Long keepAliveTime;
    /** 时间类型 */
    @TableField("time_type")
    private String timeType;
    /** 队列的类型 */
    @TableField("queue_type")
    private String queueType;
    /** 生效状态 */
    @TableField("effect_status")
    private String effectStatus;

}
