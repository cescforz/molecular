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

import java.util.Date;

/**
 * <p>Description: </p>
 *
 * @author developer cesc
 * @version 1.00.00
 * @date Create in 2019/7/31 11:30
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class BaseUUIDGenModel<T extends Model<T>> extends Model<T> {

    @TableId(value = "id", type = IdType.UUID)
    private String id;

    @TableField(value = "gmt_create")
    @JsonProperty(value = "createDate")
    @DateTimeFormat(pattern = SystemConstants.PATTERN_DATETIME_24)
    @JsonFormat(pattern = SystemConstants.PATTERN_DATETIME_24, timezone = SystemConstants.DEFAULT_TIME_ZONE)
    private Date createDate;

    @TableField(value = "gmt_modified")
    @JsonProperty(value = "updateDate")
    @DateTimeFormat(pattern = SystemConstants.PATTERN_DATETIME_24)
    @JsonFormat(pattern = SystemConstants.PATTERN_DATETIME_24, timezone = SystemConstants.DEFAULT_TIME_ZONE)
    private Date updateDate;

}
