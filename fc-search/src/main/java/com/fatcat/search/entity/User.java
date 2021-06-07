package com.fatcat.search.entity;


import com.fatcat.elastic.annotation.EsDocField;
import com.fatcat.elastic.annotation.EsDocId;
import com.fatcat.elastic.annotation.EsIndex;
import com.fatcat.elastic.enums.AnalyzerEnum;
import com.fatcat.elastic.enums.FieldEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 用户表
 *
 * @author fatcat
 * @create 2021-04-06
 */
@Data
@EqualsAndHashCode(callSuper = false)
@EsIndex("fc_user")
public class User {

    /**
     * 用户ID
     */
    @EsDocId
    private Long userId;

    /**
     * 用户名
     */
    @EsDocField
    private String userName;

    /**
     * 用户账号
     */
    @EsDocField
    private String userPhone;

    /**
     * 用户密码
     */
    @EsDocField
    private String userPassword;

    /**
     * 创建时间
     */
    @EsDocField(type = FieldEnum.DATE)
    private LocalDateTime createTime;

    /**
     * 创建者UID
     */
    @EsDocField(type = FieldEnum.KEYWORD, analyzer = AnalyzerEnum.NO)
    private Long createUser;

    /**
     * 更新时间
     */
    @EsDocField(type = FieldEnum.DATE)
    private LocalDateTime updateTime;

    /**
     * 更新人UID
     */
    @EsDocField(type = FieldEnum.KEYWORD, analyzer = AnalyzerEnum.NO)
    private Long updateUser;

    /**
     * 是否已删除[0:否,1:是]
     */
    @EsDocField(type = FieldEnum.BOOLEAN)
    private Boolean isDeleted;
}
