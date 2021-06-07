package com.fatcat.mybatis.base;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import lombok.Data;

import java.util.List;

/**
 * @author fatcat
 * @description 分页插件
 * @create 2021/5/6
 **/
@Data
public class Page<T> implements IPage<T> {
    private static final long serialVersionUID = -214369537500701830L;
    /**
     * 当前页码（从 1 开始）
     */
    private long current = 1;
    /**
     * 每页大小
     */
    private long size = 10;
    /**
     * 结果总数目
     */
    private long total;
    /**
     * 结果详情条目
     */
    private List<T> records;
    /**
     * 排序字段和规则
     */
    private List<OrderItem> orders;

    @Override
    public List<OrderItem> orders() {
        return orders;
    }

    @Override
    public Page<T> setRecords(List<T> records) {
        this.records = records;
        return this;
    }

    @Override
    public IPage<T> setTotal(long total) {
        this.total = total;
        return this;
    }

    @Override
    public IPage<T> setSize(long size) {
        this.size = size;
        return this;
    }

    @Override
    public IPage<T> setCurrent(long current) {
        this.current = current;
        return this;
    }
}
