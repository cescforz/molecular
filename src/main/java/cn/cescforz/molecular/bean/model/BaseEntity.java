package cn.cescforz.molecular.bean.model;

import cn.cescforz.commons.lang.annotation.Fixed;
import cn.cescforz.molecular.toolkit.util.KeyUtils;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>Description: mongoDB基础实体</p>
 *
 * @author cesc
 * @version 1.00.00
 * @date Create in 2019/8/2 11:11
 */
@Data
public class BaseEntity implements Serializable {

    public static final String MONGO_ID = "_id";
    public static final String DES = "description";
    public static final String DELETE_FLAG = "deleteFlag";
    public static final String CREATED_DT = "createDate";
    public static final String UPDATED_DT = "updateDate";
    public static final String CREATED_BY = "createdBy";
    public static final String UPDATED_BY = "updatedBy";

    private static final long serialVersionUID = -6897983804455430452L;

    @Fixed
    private Serializable id;
    private String description;
    private boolean deleteFlag;
    @Fixed
    private Date createDate;
    private Date updateDate;
    @Fixed
    private String createdBy;
    private String updatedBy;

    public BaseEntity() {
        this.id = pkVal();
        this.deleteFlag = false;
        this.createDate = new Date();
    }

    protected Serializable pkVal() {
        return KeyUtils.generateId().toString();
    }
}
