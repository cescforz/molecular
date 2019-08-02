package cn.cescforz.molecular.bean.domain;

import cn.cescforz.molecular.bean.model.BaseEntity;
import cn.cescforz.molecular.constant.MongoConstants;
import com.alibaba.fastjson.JSON;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * <p>©2018 Cesc. All Rights Reserved.</p>
 * <p>Description: 异常记录类</p>
 * @author cesc
 * @version v1.0
 * @date Create in 2018-12-26 16:18
 */
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@Data
@Document(collection = MongoConstants.ERROR_LOG)
public class ErrorLogDO extends BaseEntity {

    private static final long serialVersionUID = 3345680287356589611L;

    private String interfaceName;
    private String requestParam;
    private Long consumeTime;
    private String logInfo;
    private Integer moduleType;

    @Override
    public String toString() {
        return JSON.toJSONString(this,true);
    }
}
