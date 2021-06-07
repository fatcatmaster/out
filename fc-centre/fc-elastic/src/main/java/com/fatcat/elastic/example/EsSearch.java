package com.fatcat.elastic.example;

/**
 * @author fatcat
 * @description 查询api的示例
 * @create 2021/5/13
 **/

import org.apache.lucene.search.join.ScoreMode;
import org.elasticsearch.index.query.IdsQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;

public class EsSearch {


    /**
     * 使用QueryBuilder
     * termQuery("key", obj) 完全匹配
     * termsQuery("key", obj1, obj2..)   一次匹配多个值
     * matchQuery("key", Obj) 单个匹配, field不支持通配符, 前缀具高级特性
     * multiMatchQuery("text", "field1", "field2"..);  匹配多个字段, field有通配符忒行
     * matchAllQuery();         匹配所有文件
     */
    public void testQueryBuilder() {
        // 对查询参数不分词，匹配到内容（内容可能被分词）一个片段即可
        QueryBuilder queryBuilder1 = QueryBuilders.termQuery("user", "kimchy");
        // 对查询参数分词，分词中只要有一个和内容（或其分词）匹配即可
        QueryBuilder queryBuilder2 = QueryBuilders.matchQuery("user", "kimchy");
        // 对查询参数分词，所有分词都包含在内容（或其分词）中，且顺序（分词偏移从小到大）保证一致
        QueryBuilder queryBuilder3 = QueryBuilders.matchPhrasePrefixQuery("user", "i like u");
        QueryBuilder queryBuilder4 = QueryBuilders.multiMatchQuery("kimchy", "user", "message", "gender");
        QueryBuilder queryBuilder5 = QueryBuilders.matchAllQuery();
    }

    /**
     * 组合查询
     * must(QueryBuilders) :   AND
     * mustNot(QueryBuilders): NOT
     * should:                  : OR
     */
    public void testQueryBuilder2() {
        QueryBuilder queryBuilder = QueryBuilders.boolQuery()
                .must(QueryBuilders.termQuery("user", "kimchy"))
                .mustNot(QueryBuilders.termQuery("message", "nihao"))
                .should(QueryBuilders.termQuery("gender", "male"));
    }

    /**
     * 只查询一个id的
     * QueryBuilders.idsQuery(String...type).ids(Collection<String> ids)
     */
    public void testIdsQuery() {
        String indexName = "indexName";
        IdsQueryBuilder queryBuilder = QueryBuilders.idsQuery();
        queryBuilder.addIds("1");
    }

    /**
     * 包裹查询, 高于设定分数, 不计算相关性
     */
    public void testConstantScoreQuery() {
        QueryBuilder queryBuilder = QueryBuilders
                .constantScoreQuery(QueryBuilders.termQuery("name", "kimchy"))
                .boost(2.0f);
    }

    /**
     * disMax查询
     * 对子查询的结果做union, score沿用子查询score的最大值,
     * 广泛用于muti-field查询
     */
    public void testDisMaxQuery() {
        QueryBuilder queryBuilder = QueryBuilders.disMaxQuery()
                // 查询条件
                .add(QueryBuilders.termQuery("user", "kimch"))
                .add(QueryBuilders.termQuery("message", "hello"))
                .boost(1.3f)
                .tieBreaker(0.7f);
    }

    /**
     * 模糊查询
     * 不能用通配符, 不知道干啥用
     */
    public void testFuzzyQuery() {
        QueryBuilder queryBuilder = QueryBuilders.fuzzyQuery("user", "kimch");
    }

    /**
     * moreLikeThisQuery: 实现基于内容推荐, 支持实现一句话相似文章查询
     * {
     * "more_like_this" : {
     * "fields" : ["title", "content"],   // 要匹配的字段, 不填默认_all
     * "like_text" : "text like this one",   // 匹配的文本
     * }
     * }
     * <p>
     * percent_terms_to_match：匹配项（term）的百分比，默认是0.3
     * <p>
     * min_term_freq：一篇文档中一个词语至少出现次数，小于这个值的词将被忽略，默认是2
     * <p>
     * max_query_terms：一条查询语句中允许最多查询词语的个数，默认是25
     * <p>
     * stop_words：设置停止词，匹配时会忽略停止词
     * <p>
     * min_doc_freq：一个词语最少在多少篇文档中出现，小于这个值的词会将被忽略，默认是无限制
     * <p>
     * max_doc_freq：一个词语最多在多少篇文档中出现，大于这个值的词会将被忽略，默认是无限制
     * <p>
     * min_word_len：最小的词语长度，默认是0
     * <p>
     * max_word_len：最多的词语长度，默认无限制
     * <p>
     * boost_terms：设置词语权重，默认是1
     * <p>
     * boost：设置查询权重，默认是1
     * <p>
     * analyzer：设置使用的分词器，默认是使用该字段指定的分词器
     */
    public void testMoreLikeThisQuery() {
        QueryBuilder queryBuilder = QueryBuilders.moreLikeThisQuery(new String[]{"user"})
                //最少出现的次数
                .minTermFreq(1)
                // 最多允许查询的词语
                .maxQueryTerms(12);
    }

    /**
     * 前缀查询
     */
    public void testPrefixQuery() {
        QueryBuilder queryBuilder = QueryBuilders.matchQuery("user", "kimchy");
    }

    /**
     * 查询解析查询字符串, 查询包含include 不包含 exclude的字符串，匹配所有字段
     */
    public void testQueryString() {
        QueryBuilder queryBuilder = QueryBuilders.queryStringQuery("+include -exclude");
    }

    /**
     * 范围内查询
     */
    public void testRangeQuery() {
        QueryBuilder queryBuilder = QueryBuilders.rangeQuery("user")
                .from("kimchy")
                .to("wenbronk")
                // 包含上界
                .includeLower(true)
                // 包含下届
                .includeUpper(true);
    }

    /**
     * 跨度查询
     */
    public void testSpanQueries() {
        QueryBuilder queryBuilder1 = QueryBuilders
                // Max查询范围的结束位置, end 值小于等于当前查询值在语句中的实际中的偏移量
                // name : this is a elasticsearch; elasticsearch 的偏移量=4
                .spanFirstQuery(QueryBuilders.spanTermQuery("name", "elasticsearch"), 3);

//        QueryBuilder queryBuilder2 = QueryBuilders.spanNearQuery()
//                .clause(QueryBuilders.spanTermQuery("name", "葫芦580娃"))
//                .clause(QueryBuilders.spanTermQuery("name", "葫芦3812娃"))
//                .clause(QueryBuilders.spanTermQuery("name", "葫芦7139娃"))
//                .slop(30000)
//                .inOrder(false)
//                .collectPayloads(false);

        // Span Not
//        QueryBuilder queryBuilder3 = QueryBuilders.spanNotQuery()
//                .include(QueryBuilders.spanTermQuery("name", "葫芦580娃"))
//                .exclude(QueryBuilders.spanTermQuery("home", "山西省太原市2552街道"));

        // Span Or
//        QueryBuilder queryBuilder4 = QueryBuilders.spanOrQuery()
//                .clause(QueryBuilders.spanTermQuery("name", "葫芦580娃"))
//                .clause(QueryBuilders.spanTermQuery("name", "葫芦3812娃"))
//                .clause(QueryBuilders.spanTermQuery("name", "葫芦7139娃"));

        // Span Term
        QueryBuilder queryBuilder5 = QueryBuilders.spanTermQuery("name", "葫芦580娃");
    }

    /**
     * 通配符查询, 支持 *
     * 匹配任何字符序列, 包括空
     * 避免* 开始, 会检索大量内容造成效率缓慢
     */
    public void testWildCardQuery() {
        QueryBuilder queryBuilder = QueryBuilders.wildcardQuery("user", "ki*hy");
    }

    /**
     * 嵌套查询, 内嵌文档查询
     */
    public void testNestedQuery() {
        QueryBuilder boolBuilder = QueryBuilders.boolQuery()
                .must(QueryBuilders.matchQuery("location.lat", 0.962590433140581))
                .must(QueryBuilders.rangeQuery("location.lon").lt(36.0000).gt(0.000));
        QueryBuilder queryBuilder = QueryBuilders.nestedQuery("location", boolBuilder, ScoreMode.Total);
    }

}

