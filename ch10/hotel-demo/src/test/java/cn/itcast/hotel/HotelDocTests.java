package cn.itcast.hotel;

import cn.itcast.hotel.pojo.Hotel;
import cn.itcast.hotel.pojo.HotelDoc;
import cn.itcast.hotel.service.IHotelService;
import com.alibaba.fastjson.JSON;
import lombok.SneakyThrows;
import org.apache.http.HttpHost;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashMap;
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
 * @createTime : 2023/8/5 9:49
 * @Email : icexmoon@qq.com
 * @Website : https://icexmoon.cn
 * @Description :
 */
@SpringBootTest
public class HotelDocTests {
    @Autowired
    private IHotelService hotelService;
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
    @SneakyThrows
    void testAddHotelDoc() {
        // 从数据库查询要添加的数据
        Hotel hotel = hotelService.getById(38665L);
        // 创建文档添加请求
        IndexRequest request = new IndexRequest("hotel").id(hotel.getId().toString());
        // 将数据转化为文档需要的格式
        HotelDoc hotelDoc = new HotelDoc(hotel);
        // 转化为 json 格式并附加到请求对象
        request.source(JSON.toJSONString(hotelDoc), XContentType.JSON);
        // 发送文档添加请求
        restHighLevelClient.index(request, RequestOptions.DEFAULT);
    }

    @Test
    @SneakyThrows
    void testGetHotelDoc() {
        // 构建文档查询请求对象
        GetRequest request = new GetRequest("hotel", "38665");
        // 进行查询并获取返回值
        GetResponse response = restHighLevelClient.get(request, RequestOptions.DEFAULT);
        // 从返回对象中获取文档内容（source）
        String sourceAsString = response.getSourceAsString();
        // 解析字符串，获取对象
        HotelDoc hotelDoc = JSON.parseObject(sourceAsString, HotelDoc.class);
        System.out.println(hotelDoc);
    }

    @Test
    @SneakyThrows
    void testUpdateHotelDoc() {
        UpdateRequest request = new UpdateRequest("hotel", "38665");
        request.doc("price", 300,
                "starName", "四钻");
        restHighLevelClient.update(request, RequestOptions.DEFAULT);
    }

    @Test
    @SneakyThrows
    void testUpdateHotelDoc2() {
        UpdateRequest request = new UpdateRequest("hotel", "38665");
        Map<String, Object> hotelUpdated = new HashMap<>();
        hotelUpdated.put("price", 350);
        hotelUpdated.put("starName", "五钻");
        request.doc(hotelUpdated);
        restHighLevelClient.update(request, RequestOptions.DEFAULT);
    }

    @Test
    @SneakyThrows
    void testDelHotelDoc(){
        DeleteRequest request = new DeleteRequest("hotel","38665");
        restHighLevelClient.delete(request, RequestOptions.DEFAULT);
    }

    @Test
    @SneakyThrows
    void testBatchAddHotelDoc(){
        BulkRequest request = new BulkRequest();
        List<Hotel> hotels = hotelService.list();
        for(Hotel hotel: hotels){
            HotelDoc hotelDoc = new HotelDoc(hotel);
            request.add(new IndexRequest("hotel")
                    .id(hotelDoc.getId().toString())
                    .source(JSON.toJSONString(hotelDoc), XContentType.JSON));
        }
        restHighLevelClient.bulk(request, RequestOptions.DEFAULT);
    }
}
