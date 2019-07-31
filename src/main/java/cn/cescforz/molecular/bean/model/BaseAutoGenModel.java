package cn.cescforz.molecular.bean.model;

import cn.cescforz.commons.lang.constant.SystemConstants;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>Description: </p>
 *
 * @author developer cesc
 * @version 1.00.00
 * @date Create in 2019/7/31 10:05
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class BaseAutoGenModel<T extends Model<T>> extends Model<T> {

    @TableId(value = "id", type = IdType.AUTO) // 阿里规约: 其中 id 必为主键，类型为 unsigned bigint、单表时自增、步长为 1
    private Long id;

    @TableField(value = "gmt_create")
    @JsonProperty(value = "createDate")
    @DateTimeFormat(pattern = SystemConstants.PATTERN_DATETIME_24) // 入参格式化
    @JsonFormat(pattern = SystemConstants.PATTERN_DATETIME_24,timezone = SystemConstants.DEFAULT_TIME_ZONE) // 出参格式化
    private Date createDate;

    @TableField(value = "gmt_modified")
    @JsonProperty(value = "updateDate")
    @DateTimeFormat(pattern = SystemConstants.PATTERN_DATETIME_24)
    @JsonFormat(pattern = SystemConstants.PATTERN_DATETIME_24,timezone = SystemConstants.DEFAULT_TIME_ZONE)
    private Date updateDate;

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    /*
        AUTO (0, "数据库 ID 自增"),
        INPUT (1, "用户输入 ID"),
        ID_WORKER (2, "全局唯一 ID"),
        UUID (3, "全局唯一 ID"),
        NONE (4, "该类型为未设置主键类型"),
        ID_WORKER_STR (5, "字符串全局唯一 ID");
     */

}
