package com.fatcat.elastic.utils;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.fatcat.common.constants.enums.ExceptionEnum;
import com.fatcat.common.exception.FatCatException;
import com.fatcat.elastic.annotation.EsIndex;
import org.springframework.core.annotation.AnnotationUtils;

import java.lang.annotation.Annotation;

/**
 * @author fatcat
 * @description es 的工具类
 * @create 2021/4/21
 **/
public class ElasticUtil {

    /**
     * 通过类获取索引名
     *
     * @param clazz 类
     * @return 索引名
     */
    public static String createIndexNameByClass(Class clazz) {
        // 通过工具类获取注解，可以获取注解别名的值
        EsIndex esIndex = AnnotationUtils.getAnnotation(clazz, EsIndex.class);
        if (ObjectUtil.isNull(esIndex)) {
            throw new FatCatException(ExceptionEnum.MISS_INDEX_ANNOTATION);
        }
        // 获取索引名
        String indexName = esIndex.name();
        // 如果没有指定索引名，类名即为索引名
        if (StrUtil.isBlank(indexName)) {
            throw new FatCatException(ExceptionEnum.MISS_INDEX_NAME);
        }
        return indexName;
    }
}
