package cn.cescforz.molecular.bean.domain;

import cn.cescforz.molecular.bean.model.BaseUUIDGenModel;
import cn.cescforz.molecular.constant.MongoConstants;
import com.alibaba.fastjson.JSON;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Map;

/**
 * <p>©2019 Cesc. All Rights Reserved.</p>
 * <p>Description: 接口调用日志数据库DO实体</p>
 *
 * @author cesc
 * @version v1.0
 * @date Create in 2019-03-08 11:11
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Document(collection = MongoConstants.API_LOG)
public class ApiLogDO extends BaseUUIDGenModel<ApiLogDO> {

    private static final long serialVersionUID = 557525806742068700L;

    private String requestUrl;
    private String requestUri;
    private String queryString;
    private String remoteAddr;
    private String remoteHost;
    private Integer remotePort;
    private String localAddr;
    private String localName;
    private String method;
    private String classMethod;
    private String args;
    private Map headers;
    private Map parameters;
    private Long consumeTime;

    @Override
    public String toString() {
        return JSON.toJSONString(this, true);
    }
}
