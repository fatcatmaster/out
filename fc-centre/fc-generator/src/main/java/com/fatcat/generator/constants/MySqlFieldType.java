package com.fatcat.generator.constants;

import lombok.AllArgsConstructor;

/**
 * @author zhouhc
 * @description 数据库字段类型
 * @create 2021/5/21
 **/
@AllArgsConstructor
public enum MySqlFieldType implements BaseFieldType {
    /**
     * mysql 数据库字段类型
     */
    DATETIME("datetime"),
    ;
    private String type;

    @Override
    public String getType() {
        return this.type;
    }
}
