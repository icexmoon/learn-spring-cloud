package cn.itcast.hotel;

import cn.itcast.hotel.pojo.HotelDoc;
import com.alibaba.fastjson.JSON;
import lombok.SneakyThrows;
import org.apache.http.HttpHost;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.lucene.search.function.CombineFunction;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.functionscore.FunctionScoreQueryBuilder;
import org.elasticsearch.index.query.functionscore.ScoreFunctionBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.BucketOrder;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.aggregations.metrics.Stats;
import org.elasticsearch.search.aggregations.metrics.StatsAggregationBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.elasticsearch.search.sort.SortOrder;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 *
 * @author : 魔芋红茶
 * @version : 1.0
 * @Project : hotel-demo
 * @Package : cn.itcast.hotel
 * @ClassName : .java
 * @createTime : 2023/8/8 10:46
 * @Email : icexmoon@qq.com
 * @Website : https://icexmoon.cn
 * @Description :
 */
public class HotelSearchTests {
    private RestHighLevelClient restHighLevelClient;

    @BeforeEach
    void initClient() {
        restHighLevelClient = new RestHighLevelClient(RestClient.builder(
                HttpHost.create("http://192.168.0.88:9200")
        ));
    }

    @AfterEach
    @SneakyThrows
    void closeClient() {
        restHighLevelClient.close();
    }

    @Test
    void testMatchAll() {
        doQuery(QueryBuilders.matchAllQuery());
    }

    @SneakyThrows
    private void doQuery(QueryBuilder queryBuilder) {
        // 构建请求对象，并指定索引库名
        SearchRequest request = new SearchRequest("hotel");
        // 构建 DSL 查询语句
        request.source().query(queryBuilder);
        // 执行查询并返回结果
        SearchResponse searchResponse = restHighLevelClient.search(request, RequestOptions.DEFAULT);
        printResponse(searchResponse);
    }

    private void printResponse(SearchResponse searchResponse) {
        // 获取外层的 hits 对象
        SearchHits searchHits = searchResponse.getHits();
        // 查询到的总数
        long total = searchHits.getTotalHits().value;
        System.out.println(String.format("找到了%d条数据", total));
        // 获取内层 hits 数组（查询到的实际数据）
        SearchHit[] hits = searchHits.getHits();
        // 遍历
        for (SearchHit h : hits) {
            // 对原始数据进行 json 解码，生成 java 对象
            String json = h.getSourceAsString();
            HotelDoc hotelDoc = JSON.parseObject(json, HotelDoc.class);
            System.out.println(hotelDoc);
        }
    }

    @Test
    void testMatch() {
        doQuery(QueryBuilders.matchQuery("all", "外滩如家"));
    }

    @Test
    void testMultiMatch() {
        doQuery(QueryBuilders.multiMatchQuery("外滩如家", "name", "brand", "city"));
    }

    @Test
    void testTerm() {
        doQuery(QueryBuilders.termQuery("city", "上海"));
    }

    @Test
    void testRange() {
        doQuery(QueryBuilders.rangeQuery("price").gte(100).lte(150));
    }

    @Test
    void testGeoDistance() {
        doQuery(QueryBuilders.geoDistanceQuery("location")
                .distance("5km")
                .point(31.21, 121.5));
    }

    @Test
    void testFunctionScore() {
        FunctionScoreQueryBuilder.FilterFunctionBuilder[] filterFunctionBuilders = {
                new FunctionScoreQueryBuilder.FilterFunctionBuilder(
                        QueryBuilders.termQuery("brand", "如家"),
                        ScoreFunctionBuilders.weightFactorFunction(10))
        };
        FunctionScoreQueryBuilder fsqb = QueryBuilders.functionScoreQuery(
                QueryBuilders.matchQuery("all", "浦东"),
                filterFunctionBuilders);
        fsqb.filterFunctionBuilders();
        fsqb.boostMode(CombineFunction.SUM);
        doQuery(fsqb);
    }

    @Test
    void testBool() {
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        boolQueryBuilder.must(QueryBuilders.matchQuery("name", "如家"));
        boolQueryBuilder.mustNot(QueryBuilders.rangeQuery("price").gt(400));
        boolQueryBuilder.filter(QueryBuilders.geoDistanceQuery("location")
                .distance("10km")
                .point(31.21, 121.5));
        doQuery(boolQueryBuilder);
    }

    @Test
    @SneakyThrows
    void testSortAndPage() {
        SearchRequest request = new SearchRequest("hotel");
        request.source()
                .query(QueryBuilders.matchAllQuery())
                .sort("price", SortOrder.ASC)
                .from(0).size(10);
        SearchResponse searchResponse = restHighLevelClient.search(request, RequestOptions.DEFAULT);
        printResponse(searchResponse);
    }

    @Test
    @SneakyThrows
    void testHighlight() {
        SearchRequest request = new SearchRequest("hotel");
        request.source()
                .query(QueryBuilders.matchQuery("all", "如家"))
                .highlighter(new HighlightBuilder()
                        .field("name")
                        .field("brand")
                        .field("city")
                        .requireFieldMatch(false));
        SearchResponse searchResponse = restHighLevelClient.search(request, RequestOptions.DEFAULT);
        // 获取外层的 hits 对象
        SearchHits searchHits = searchResponse.getHits();
        // 查询到的总数
        long total = searchHits.getTotalHits().value;
        System.out.println(String.format("找到了%d条数据", total));
        // 获取内层 hits 数组（查询到的实际数据）
        SearchHit[] hits = searchHits.getHits();
        // 遍历
        for (SearchHit h : hits) {
            // 对原始数据进行 json 解码，生成 java 对象
            String json = h.getSourceAsString();
            HotelDoc hotelDoc = JSON.parseObject(json, HotelDoc.class);
            // 获取高亮部分以替换原始内容
            Map<String, HighlightField> highlightFields = h.getHighlightFields();
            for (Map.Entry<String, HighlightField> entry : highlightFields.entrySet()) {
                String fieldName = entry.getKey();
                HighlightField highlightField = entry.getValue();
                if (highlightField == null) {
                    //高亮内容为空，下一条
                    continue;
                }
                Text[] fragments = highlightField.getFragments();
                if (fragments == null || fragments.length == 0) {
                    //缺少实际的高亮内容，不处理
                    continue;
                }
                String highlightContent = fragments[0].toString();
                //利用反射，将高亮内容替换原始内容
                Field hotelDocField = null;
                try {
                    hotelDocField = HotelDoc.class.getDeclaredField(fieldName);
                } catch (NoSuchFieldException e) {
                    //不能和类型中的字段名匹配，不处理
                    continue;
                }
                hotelDocField.setAccessible(true);
                hotelDocField.set(hotelDoc, highlightContent);
            }
            System.out.println(hotelDoc);
        }
    }

    @Test
    @SneakyThrows
    void testBrandAgg() {
        // 准备查询语句
        SearchRequest request = new SearchRequest("hotel");
        request.source()
                .query(QueryBuilders.rangeQuery("price")
                        .lte(200))
                .size(0)
                .aggregation(AggregationBuilders.terms("brandAgg")
                        .field("brand")
                        .size(20));
        SearchResponse searchResponse = restHighLevelClient.search(request, RequestOptions.DEFAULT);
        // 处理返回值
        // 获取 aggregations 中的内容
        Aggregations aggregations = searchResponse.getAggregations();
        if (aggregations != null) {
            // 获取查询时定义的聚合结果
            // 返回的类型与查询时的聚合类型相关
            Terms brandAgg = aggregations.get("brandAgg");
            if (brandAgg != null) {
                // 遍历聚合查询结果并打印
                List<? extends Terms.Bucket> buckets = brandAgg.getBuckets();
                buckets.forEach(b -> {
                    System.out.println(String.format("key:%s, count: %d", b.getKeyAsString(), b.getDocCount()));
                });
            }
        }
    }

    @Test
    @SneakyThrows
    void testScoreAgg() {
        SearchRequest request = new SearchRequest("hotel");
        TermsAggregationBuilder brandAgg = AggregationBuilders.terms("brandAgg");
        request.source().size(0)
                .aggregation(brandAgg);
        StatsAggregationBuilder scoreAgg = AggregationBuilders.stats("scoreAgg");
        brandAgg.field("brand")
                .size(10)
                .order(BucketOrder.aggregation("scoreAgg.avg", false))
                .subAggregation(scoreAgg);
        scoreAgg.field("score");
        SearchResponse searchResponse = restHighLevelClient.search(request, RequestOptions.DEFAULT);
        Aggregations aggregations = searchResponse.getAggregations();
        if (aggregations != null) {
            Terms brandTerms = aggregations.get("brandAgg");
            List<? extends Terms.Bucket> buckets = brandTerms.getBuckets();
            for (Terms.Bucket bucket : buckets) {
                System.out.println(String.format("brand: %s, count: %d", bucket.getKeyAsString(), bucket.getDocCount()));
                Aggregations subAggregations = bucket.getAggregations();
                if (subAggregations != null) {
                    Stats scoreStats = subAggregations.get("scoreAgg");
                    System.out.println(String.format("min: %.2f, max: %.2f, avg: %.2f, sum: %.2f",
                            scoreStats.getMin(),
                            scoreStats.getMax(),
                            scoreStats.getAvg(),
                            scoreStats.getSum()));
                }
            }
        }
    }

}
