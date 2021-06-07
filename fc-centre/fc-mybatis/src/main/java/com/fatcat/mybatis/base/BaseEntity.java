package com.fatcat.mybatis.base;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author fatcat
 * @description 通用属性值
 * @create 2021/5/6
 **/
@Data
public class BaseEntity implements Serializable {
    private static final long serialVersionUID = 2698577164797748107L;

    public static final String CREATE_USER = "create_user";
    public static final String CREATE_TIME = "create_time";
    public static final String UPDATE_USER = "update_user";
    public static final String UPDATE_TIME = "update_time";
    public static final String IS_DELETED = "is_deleted";

    /**
     * 创建人id
     */
    private Long createUser;

    /**
     * 创建时间
     */
    @TableField(value = CREATE_TIME, fill = FieldFill.INSERT)
    private Date createTime;

    /**
     * 更新人id
     */
    private Long updateUser;

    /**
     * 更新时间
     */
    @TableField(value = UPDATE_TIME, fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;

    /**
     * 逻辑删除字段，含有当前字段的实体调用删除接口时不会物理删除数据
     * 默认值即 value = 0, delval = 1; 可不用配置
     */
    @TableLogic(value = "0", delval = "1")
    private Boolean isDeleted;

}
