package com.fatcat.core.logger.entity;

import cn.hutool.core.date.LocalDateTimeUtil;
import cn.hutool.core.util.IdUtil;
import com.fatcat.elastic.annotation.EsDocField;
import com.fatcat.elastic.annotation.EsDocId;
import com.fatcat.elastic.annotation.EsIndex;
import com.fatcat.elastic.enums.AnalyzerEnum;
import com.fatcat.elastic.enums.FieldEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * elastic 中的日志实体类
 *
 * @author fatcat
 * @description 全局统一日志实体
 * @create 2021/5/10
 **/
@EsIndex("fc_log")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoggerEntity {

    /**
     * 日志的时间，字符串形式，便于用户查看
     */
    @EsDocField(type = FieldEnum.DATE, analyzer = AnalyzerEnum.NO)
    private LocalDateTime time = LocalDateTimeUtil.now();

    /**
     * 日志等级
     */
    @EsDocField(type = FieldEnum.KEYWORD, analyzer = AnalyzerEnum.NO)
    private String level;

    /**
     * 线程名称
     */
    @EsDocField
    private String threadName;

    /**
     * 日志对应的包名
     */
    @EsDocField
    private String packageName;

    /**
     * 日志的方法名称
     */
    @EsDocField(type = FieldEnum.KEYWORD, analyzer = AnalyzerEnum.NO)
    private String methodName;

    /**
     * 日志打印时所在的代码行数
     */
    @EsDocField(type = FieldEnum.INTEGER, analyzer = AnalyzerEnum.NO)
    private int lineNumber;

    /**
     * 同一个请求的id一致
     */
    @EsDocField(type = FieldEnum.KEYWORD, analyzer = AnalyzerEnum.NO)
    private String requestId = IdUtil.fastSimpleUUID();

    /**
     * 请求参数
     */
    @EsDocField
    private String requestParam;

    /**
     * 响应结果
     */
    @EsDocField
    private String responseResult;

    /**
     * 日志的详细内容
     */
    @EsDocField
    private String logMessage;

    /**
     * 如有更加详细的信息使用此字段
     */
    @EsDocField
    private String logDetail;

    /**
     * 是否属于系统日志，譬如切面日志等
     */
    @EsDocField(type = FieldEnum.BOOLEAN, analyzer = AnalyzerEnum.NO)
    private Boolean systemLog = Boolean.FALSE;

    /**
     * 自定义构造方法
     */
    public LoggerEntity(String packageName, String methodName, int lineNumber) {
        this.packageName = packageName;
        this.methodName = methodName;
        this.lineNumber = lineNumber;
    }
}
