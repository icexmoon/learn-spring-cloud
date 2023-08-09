package cn.itcast.hotel.service.impl;

import cn.itcast.hotel.controller.HotelController;
import cn.itcast.hotel.mapper.HotelMapper;
import cn.itcast.hotel.pojo.Hotel;
import cn.itcast.hotel.pojo.HotelDoc;
import cn.itcast.hotel.pojo.PageResult;
import cn.itcast.hotel.service.IHotelService;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.SneakyThrows;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.geo.GeoPoint;
import org.elasticsearch.common.lucene.search.function.CombineFunction;
import org.elasticsearch.common.unit.DistanceUnit;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.RangeQueryBuilder;
import org.elasticsearch.index.query.functionscore.FunctionScoreQueryBuilder;
import org.elasticsearch.index.query.functionscore.ScoreFunctionBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class HotelService extends ServiceImpl<HotelMapper, Hotel> implements IHotelService {
    @Autowired
    private RestHighLevelClient restHighLevelClient;

    @Override
    @SneakyThrows
    public PageResult search(HotelController.ListRequestParams requestParams) {
        SearchRequest request = new SearchRequest("hotel");
        String key = requestParams.getKey();
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        if (key == null || key.isEmpty()) {
            boolQueryBuilder.must(QueryBuilders.matchAllQuery());
        } else {
            boolQueryBuilder.must(QueryBuilders.matchQuery("all", key));
        }
        String brand = requestParams.getBrand();
        if (brand != null && !brand.isEmpty()) {
            boolQueryBuilder.filter(QueryBuilders.termQuery("brand", brand));
        }
        Long maxPrice = requestParams.getMaxPrice();
        Long minPrice = requestParams.getMinPrice();
        RangeQueryBuilder rqb = QueryBuilders.rangeQuery("price");
        boolean hasPriceCondition = false;
        if (maxPrice != null) {
            rqb.lte(maxPrice);
            hasPriceCondition = true;
        }
        if (minPrice != null) {
            rqb.gte(minPrice);
            hasPriceCondition = true;
        }
        if (hasPriceCondition) {
            boolQueryBuilder.filter(rqb);
        }
        String city = requestParams.getCity();
        if (city != null && !city.isEmpty()) {
            boolQueryBuilder.filter(QueryBuilders.termQuery("city", city));
        }
        String starName = requestParams.getStarName();
        if (starName != null && !starName.isEmpty()) {
            boolQueryBuilder.filter(QueryBuilders.termQuery("starName", starName));
        }
        FunctionScoreQueryBuilder.FilterFunctionBuilder[] filterFunctionScoreBuilders = new FunctionScoreQueryBuilder.FilterFunctionBuilder[]{
                new FunctionScoreQueryBuilder.FilterFunctionBuilder(
                        QueryBuilders.termQuery("isAD", true),
                        ScoreFunctionBuilders.weightFactorFunction(10)
                )
        };
        FunctionScoreQueryBuilder functionScoreQueryBuilder = QueryBuilders.functionScoreQuery(boolQueryBuilder, filterFunctionScoreBuilders);
        functionScoreQueryBuilder.boostMode(CombineFunction.MULTIPLY);
        request.source().query(functionScoreQueryBuilder);
        request.source().from((requestParams.getPage() - 1) * requestParams.getSize()).size(requestParams.getSize());
        String sortBy = requestParams.getSortBy();
        if (sortBy != null && !sortBy.isEmpty() && !sortBy.equals("default")) {
            request.source().sort(sortBy, SortOrder.ASC);
        }
        // 如果有地理坐标，按照远近进行排序
        String location = requestParams.getLocation();
        if (location != null && !location.isEmpty()) {
            request.source().sort(
                    SortBuilders.geoDistanceSort("location", new GeoPoint(location))
                            .order(SortOrder.ASC)
                            .unit(DistanceUnit.KILOMETERS));
        }
        SearchResponse searchResponse = restHighLevelClient.search(request, RequestOptions.DEFAULT);
        SearchHit[] hits = searchResponse.getHits().getHits();
        List<HotelDoc> hotelDocs = new ArrayList<>();
        for (SearchHit sh : hits) {
            HotelDoc hotelDoc = JSON.parseObject(sh.getSourceAsString(), HotelDoc.class);
            Object[] sortValues = sh.getSortValues();
            if (sortValues != null && sortValues.length >= 1) {
                hotelDoc.setDistance(sortValues[0]);
            }
            hotelDocs.add(hotelDoc);
        }
        return new PageResult(searchResponse.getHits().getTotalHits().value, hotelDocs);
    }
}
