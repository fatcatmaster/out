package com.fatcat.elastic.template;

import cn.hutool.core.util.StrUtil;
import com.fatcat.elastic.annotation.EsDocField;
import com.fatcat.elastic.enums.FieldEnum;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.support.IndicesOptions;
import org.elasticsearch.action.support.master.AcknowledgedResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.CreateIndexResponse;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.client.indices.PutMappingRequest;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.core.annotation.AnnotatedElementUtils;

import java.io.IOException;
import java.lang.reflect.Field;

import static com.fatcat.elastic.utils.ElasticUtil.createIndexNameByClass;

/**
 * @author fatcat
 * @description es 索引操作工具类
 * @create 2021/4/20
 **/
@SpringBootConfiguration
@Slf4j
public class ElasticIndexTemplate {

    @Autowired
    private RestHighLevelClient restHighLevelClient;

    /**
     * 默认分片数
     */
    private static int indexNumberOfShards = 2;

    /**
     * 默认副本数  单节点
     */
    private static int indexNumberOfReplicas = 1;

    /**
     * 自定义分片数
     */
    public void setIndexNumber(int indexNumberOfShards, int indexNumberOfReplicas) {
        ElasticIndexTemplate.indexNumberOfShards = indexNumberOfShards;
        ElasticIndexTemplate.indexNumberOfReplicas = indexNumberOfReplicas;
    }

    /**
     * 创建索引(默认分片数为5和副本数为1)
     *
     * @param clazz 根据实体自动映射es索引
     * @return boolean 表示创建失败与否
     */
    public boolean createIndex(Class clazz) throws IOException {
        // 获取索引名
        String indexName = createIndexNameByClass(clazz);
        // 判断当前索引是否存在
        if (isIndexExists(indexName)) {
            // 存在返回失败标志
            return Boolean.FALSE;
        }
        // 如果不存在就创建
        return createRootIndex(indexName, clazz);
    }

    /**
     * 更新索引(默认分片数为2和副本数为1)：
     * 只能给索引上添加一些不存在的字段
     * 已经存在的映射不能改
     *
     * @param clazz 根据实体自动映射es索引
     * @return 是否删除成功
     */
    public boolean updateIndex(Class clazz) throws IOException {
        // 获取索引名
        String indexName = createIndexNameByClass(clazz);
        PutMappingRequest request = new PutMappingRequest(indexName);
        // 开始组装json
        XContentBuilder builder = XContentFactory.jsonBuilder();
        request.source(generateBuilder(builder, clazz, null));
        AcknowledgedResponse response = restHighLevelClient.indices().putMapping(request, RequestOptions.DEFAULT);
        // 指示是否所有节点都已确认请求
        return response.isAcknowledged();
    }

    /**
     * 删除索引
     *
     * @param clazz 类
     * @return 是否删除成功
     */
    public boolean delIndex(Class clazz) throws IOException {
        String indexName = createIndexNameByClass(clazz);
        return delIndex(indexName);
    }

    /**
     * 删除索引
     *
     * @param indexName 索引名称
     * @return 是否删除成功
     */
    public boolean delIndex(String indexName) throws IOException {
        DeleteIndexRequest deleteIndexRequest = new DeleteIndexRequest(indexName);
        deleteIndexRequest.indicesOptions(IndicesOptions.LENIENT_EXPAND_OPEN);
        AcknowledgedResponse delete = restHighLevelClient.indices().delete(deleteIndexRequest, RequestOptions.DEFAULT);
        return delete.isAcknowledged();
    }

    /**
     * 创建一个索引
     *
     * @param indexName 索引名称
     * @param clazz     用来映射的类
     * @return 是否成功
     */
    private boolean createRootIndex(String indexName, Class clazz) throws IOException {
        CreateIndexRequest request = new CreateIndexRequest(indexName);
        request.settings(Settings.builder()
                // 设置分片数， 副本数
                .put("index.number_of_shards", indexNumberOfShards)
                .put("index.number_of_replicas", indexNumberOfReplicas)
        );
        // 组装json
        XContentBuilder builder = XContentFactory.jsonBuilder();
        // 创建根节点，根节点没有名称
        request.mapping(generateBuilder(builder, clazz, null));
        CreateIndexResponse response = restHighLevelClient.indices().create(request, RequestOptions.DEFAULT);
        // 指示是否所有节点都已确认请求
        boolean acknowledged = response.isAcknowledged();
        // 指示是否在超时之前为索引中的每个分片启动了必需的分片副本数
        boolean shardsAcknowledged = response.isShardsAcknowledged();
        return acknowledged || shardsAcknowledged;
    }

    /**
     * 判断索引是否存在
     *
     * @param indexName 索引名称
     * @return true：存在，false：不存在
     */
    private boolean isIndexExists(String indexName) {
        boolean exists = Boolean.FALSE;
        try {
            GetIndexRequest getIndexRequest = new GetIndexRequest(indexName);
            getIndexRequest.humanReadable(Boolean.TRUE);
            exists = restHighLevelClient.indices().exists(getIndexRequest, RequestOptions.DEFAULT);
        } catch (IOException e) {
            log.error("es 访问失败", e);
        }
        return exists;
    }

    /**
     * 构建更新的json
     *
     * @param builder 组装器
     * @param clazz   用于映射的类
     * @return 组装后的json
     */
    private XContentBuilder generateBuilder(XContentBuilder builder, Class clazz, String nodeName) throws IOException {
        // 节点名称，根节点为空
        if (StrUtil.isBlank(nodeName)) {
            builder.startObject();
        } else {
            builder.startObject(nodeName);
        }
        // 创建 properties 节点
        builder.startObject("properties");
        // 利用反射获取当前类的所有字段，getFields() 只能获取公共字段，但是也可以获取继承的公共字段
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            if (!AnnotatedElementUtils.isAnnotated(field, EsDocField.class)) {
                // 当前字段不含有指定注解，直接忽略
                continue;
            }
            // 通过 ElementUtils 工具类可以获取 AliasFor 标注的注解
            EsDocField docField = AnnotatedElementUtils.getMergedAnnotation(field, EsDocField.class);
            builder.startObject(field.getName());
            builder.field("type", docField.type().getType());
            if (FieldEnum.TEXT.equals(docField.type())) {
                // 文本类型写上分词器
                builder.field("analyzer", docField.analyzer().getType());
//              TODO 暂时不对时间格式的字段格式化，时间格式字段序列化目前主流工具都是转换成时间戳
//            } else if (FieldEnum.DATE.equals(docField.type())) {
//                // 日期类型写上日期格式
//                builder.field("format", "yyyy-MM-dd HH:mm:ss");
            } else if (FieldEnum.OBJECT.equals(docField.type())) {
                // 对象类型（嵌套类型）递归组装，除根节点外的子节点含有名称
                generateBuilder(builder, field.getType(), field.getName());
            }
            builder.endObject();
        }
        // 关闭 properties 节点
        builder.endObject();
        // 关闭当前节点
        builder.endObject();
        return builder;
    }

}
