package com.fatcat.elastic.template;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.json.JSONUtil;
import com.fatcat.elastic.annotation.EsDocField;
import com.fatcat.elastic.annotation.EsDocId;
import com.fatcat.elastic.base.EsOrder;
import com.fatcat.elastic.base.EsPage;
import com.fatcat.elastic.enums.AnalyzerEnum;
import com.fatcat.elastic.enums.FieldEnum;
import com.fatcat.elastic.utils.ElasticUtil;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.ActionListener;
import org.elasticsearch.action.bulk.BulkItemResponse;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.rest.RestStatus;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.core.annotation.AnnotatedElementUtils;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import static com.fatcat.elastic.utils.ElasticUtil.createIndexNameByClass;

/**
 * @author fatcat
 * @description es 文档操作工具类
 * @create 2021/4/20
 **/
@SpringBootConfiguration
@Slf4j
public class ElasticDocTemplate {

    /**
     * 从文档中读取到的最大数量，控制在 100
     */
    @Value("${spring.elastic.max-return-record:100}")
    private int maxReturnRecord = 100;

    @Autowired
    private RestHighLevelClient restHighLevelClient;

    /**
     * 查询所有符合条件的数据
     *
     * @param query 用于过滤的参数集合
     * @param <T>   索引类型
     * @return 结果集
     */
    public <T> EsPage<T> page(EsPage<T> query, Class<T> clazz) throws Exception {
        // 获取查询过滤条件
        T search = query.getSearch();
        // 获取索引名称
        String indexName = ElasticUtil.createIndexNameByClass(clazz);
        SearchSourceBuilder queryBuilder = new SearchSourceBuilder();
        // 构建查询体
        buildQueryBuilder(queryBuilder, search);
        // 设置分页
        queryBuilder.from(query.getCurrent());
        queryBuilder.size(Math.min(query.getSize(), maxReturnRecord));
        // 构建排序条件
        buildSortOrder(queryBuilder, query.getOrders());
        SearchRequest searchRequest = new SearchRequest(indexName);
        searchRequest.source(queryBuilder);
        SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
        List<T> list = getHitResult(searchResponse, clazz);
        query.setRecords(list);
        query.setTotal(searchResponse.getHits().getTotalHits().value);
        return query;
    }

    /**
     * 查询所有符合条件的数据
     *
     * @param object 用于过滤的参数，null 字段不参与过滤条件
     * @param <T>    索引类型
     * @return 结果集
     */
    public <T> List<T> list(T object, Class<T> clazz) throws Exception {
        SearchSourceBuilder queryBuilder = new SearchSourceBuilder();
        // 构建查询体
        buildQueryBuilder(queryBuilder, object);
        // 默认返回100条结果
        queryBuilder.size(maxReturnRecord);
        String indexName = ElasticUtil.createIndexNameByClass(object.getClass());
        SearchRequest searchRequest = new SearchRequest(indexName);
        searchRequest.source(queryBuilder);
        SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
        return getHitResult(searchResponse, clazz);
    }

    /**
     * 插入或更新一条文本
     *
     * @param object es 索引映射的实体
     * @return 插入是否成功
     */
    public <T> boolean saveOrUpdate(T object) throws Exception {
        // 获取请求封装体
        IndexRequest request = getIndexRequest(object);
        // 执行请求
        IndexResponse response = restHighLevelClient.index(request, RequestOptions.DEFAULT);
        RestStatus status = response.status();
        return RestStatus.CREATED.equals(status) || RestStatus.OK.equals(status);
    }

    /**
     * 插入或更新一条文本
     *
     * @param indexName 索引名称
     * @param id        数据id，不提供无自定义id的插入接口
     * @param json      序列化后的插入数据
     * @return 插入是否成功
     */
    public boolean saveOrUpdate(String indexName, String id, String json) throws IOException {
        // 获取请求封装体
        IndexRequest request = getIndexRequest(indexName, id, json);
        // 执行请求
        IndexResponse response = restHighLevelClient.index(request, RequestOptions.DEFAULT);
        RestStatus status = response.status();
        return RestStatus.CREATED.equals(status) || RestStatus.OK.equals(status);
    }

    /**
     * 批量请求体-同步执行
     *
     * @param list 数据集
     * @return 执行是否全部成功
     */
    public <T> boolean saveOrUpdateBatch(List<T> list) throws Exception {
        // 组装批量请求体
        BulkRequest bulkRequest = new BulkRequest();
        for (T object : list) {
            bulkRequest.add(getIndexRequest(object));
        }
        // 执行批量请求
        BulkResponse bulkResponse = restHighLevelClient.bulk(bulkRequest, RequestOptions.DEFAULT);
        RestStatus status = bulkResponse.status();
        return RestStatus.CREATED.equals(status) || RestStatus.OK.equals(status);
    }

    /**
     * 批量请求体-异步执行
     *
     * @param list 数据集
     */
    public <T> void saveOrUpdateBatchAsync(List<T> list) throws Exception {
        // 组装批量请求体
        BulkRequest bulkRequest = new BulkRequest();
        for (T object : list) {
            bulkRequest.add(getIndexRequest(object));
        }
        // 执行批量请求
        restHighLevelClient.bulkAsync(bulkRequest, RequestOptions.DEFAULT, createAsyncListener());
    }

    /**
     * 获取指定id的文档
     *
     * @param indexName 文档所在索引名称
     * @param id        文档id
     * @return 查询的数据
     */
    public String getDocById(String indexName, String id) throws Exception {
        GetRequest request = new GetRequest(indexName, id);
        GetResponse response = restHighLevelClient.get(request, RequestOptions.DEFAULT);
        return response.getSourceAsString();
    }

    /**
     * 获取指定id的文档
     *
     * @param indexName 文档所在索引名称
     * @param clazz     具体的类对象
     * @param id        文档id
     * @return 查询的数据
     */
    public <T> T getDocById(String indexName, Class<T> clazz, String id) throws Exception {
        GetRequest request = new GetRequest(indexName, id);
        GetResponse response = restHighLevelClient.get(request, RequestOptions.DEFAULT);
        String json = response.getSourceAsString();
        return JSONUtil.toBean(json, clazz);
    }

    /**
     * 删除指定id的文档
     *
     * @param indexName 索引名
     * @param id        文档id
     * @return 删除成功与否
     */
    public boolean delDocById(String indexName, String id) throws Exception {
        DeleteRequest request = new DeleteRequest(indexName, id);
        DeleteResponse response = restHighLevelClient.delete(request, RequestOptions.DEFAULT);
        return response.getShardInfo().getSuccessful() > 0;
    }

    /**
     * 批量删除指定id的文档-同步
     *
     * @param indexName 索引名称
     * @param ids       文档id
     * @return 成功与否
     */
    public boolean delDocBatch(String indexName, List<String> ids) throws Exception {
        BulkRequest bulkRequest = new BulkRequest();
        for (String id : ids) {
            DeleteRequest request = new DeleteRequest(indexName, id);
            bulkRequest.add(request);
        }
        BulkResponse bulkResponse = restHighLevelClient.bulk(bulkRequest, RequestOptions.DEFAULT);
        return RestStatus.OK.equals(bulkResponse.status());
    }

    /**
     * 批量删除指定id的文档-异步
     *
     * @param indexName 索引名称
     * @param ids       文档id
     */
    public void delDocBatchAsync(String indexName, List<String> ids) throws Exception {
        BulkRequest bulkRequest = new BulkRequest();
        for (String id : ids) {
            DeleteRequest request = new DeleteRequest(indexName, id);
            bulkRequest.add(request);
        }
        restHighLevelClient.bulkAsync(bulkRequest, RequestOptions.DEFAULT, createAsyncListener());
    }

    /**
     * 构建查询请求体
     *
     * @param queryBuilder 主查询体
     * @param search       查询参数实体
     * @param <T>          实体类型
     */
    private <T> void buildQueryBuilder(SearchSourceBuilder queryBuilder, T search) throws IllegalAccessException {
        // 根据查询条件构建 QueryBuilder
        Field[] fields = search.getClass().getDeclaredFields();
        // 默认查询所有，如果存在某个字段不为空，则查询指定字段
        Boolean searchAll = Boolean.TRUE;
        for (Field field : fields) {
            field.setAccessible(Boolean.TRUE);
            if (!AnnotatedElementUtils.isAnnotated(field, EsDocField.class)
                    || ObjectUtil.isNull(field.get(search))) {
                // 如果当前字段值为 null 不查询，如果字段不包含在索引里面 不查询
                continue;
            }
            searchAll = Boolean.FALSE;
            EsDocField docField = AnnotatedElementUtils.getMergedAnnotation(field, EsDocField.class);
            if (FieldEnum.NESTED.equals(docField.type())
                    || FieldEnum.OBJECT.equals(docField.type())) {
                // TODO 内嵌查询，内嵌查询过于麻烦，暂时不封装
            } else if (!AnalyzerEnum.NO.equals(docField.analyzer())) {
                // 指定分词类型匹配，匹配到一个即可
                queryBuilder.query(QueryBuilders.matchQuery(field.getName(), field.get(search))
                        .analyzer(docField.analyzer().getType()));
            } else {
                // 精准匹配
                queryBuilder.query(QueryBuilders.termQuery(field.getName(), field.get(search)));
            }
        }
        if (searchAll) {
            queryBuilder.query(QueryBuilders.matchAllQuery());
        }
    }

    /**
     * 从查询的结果中获取到对应的数据并转换成实体
     *
     * @param searchResponse es查询响应结果
     * @param clazz          实体类型
     * @param <T>            泛型类
     */
    private <T> List<T> getHitResult(SearchResponse searchResponse, Class<T> clazz) {
        if (ObjectUtil.isNull(searchResponse)) {
            return CollUtil.newArrayList();
        }
        SearchHits hits = searchResponse.getHits();
        int total = (int) hits.getTotalHits().value;
        List<T> list = new ArrayList<>(total);
        // 获取泛型的class类型
        for (SearchHit hit : hits) {
            String sourceAsString = hit.getSourceAsString();
            list.add(JSONUtil.toBean(sourceAsString, clazz));
        }
        return list;
    }


    /**
     * 设置排序条件
     *
     * @param queryBuilder 查询构造体
     * @param orders       排序集合
     */
    private void buildSortOrder(SearchSourceBuilder queryBuilder, List<EsOrder> orders) {
        if (CollUtil.isEmpty(orders)) {
            return;
        }
        // 设置排序条件
        for (EsOrder order : orders) {
            queryBuilder.sort(order.getName(), order.getAsc() ? SortOrder.ASC : SortOrder.DESC);
        }
    }

    /**
     * 返回插入或更新一条记录请求的封装
     *
     * @param object es 索引映射的实体
     * @return 插入是否成功
     */
    private <T> IndexRequest getIndexRequest(T object) throws Exception {
        Class<?> clazz = object.getClass();
        // 获取索引名称
        String indexName = createIndexNameByClass(clazz);
        // 创建请求体
        IndexRequest request = new IndexRequest(indexName);
        // 遍历获取id字段
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            if (field.isAnnotationPresent(EsDocId.class)) {
                // 获取当前值
                field.setAccessible(Boolean.TRUE);
                request.id(field.get(object).toString());
            }
        }
        request.source(JSONUtil.toJsonStr(object), XContentType.JSON);
        return request;
    }

    /**
     * 执行插入或更新一条文本
     *
     * @param indexName 索引名称
     * @param id        数据id，不提供无自定义id的插入接口
     * @param json      序列化后的插入数据
     * @return 插入是否成功
     */
    private IndexRequest getIndexRequest(String indexName, String id, String json) {
        IndexRequest request = new IndexRequest(indexName);
        request.id(id);
        request.source(json, XContentType.JSON);
        return request;
    }

    /**
     * 创建异步执行的监听器
     *
     * @return 监听器
     */
    private ActionListener<BulkResponse> createAsyncListener() {
        return new ActionListener<BulkResponse>() {
            @Override
            public void onResponse(BulkResponse bulkResponse) {
                // 响应回调
                for (BulkItemResponse itemResponse : bulkResponse.getItems()) {
                    if (itemResponse.isFailed()) {
                        log.info("当前记录执行失败：{}", itemResponse);
                    }
                }
            }

            @Override
            public void onFailure(Exception ex) {
                // 错误回调，忽略当前错误，防止执行中断
                log.error("批量执行错误", ex);
            }
        };
    }
}
