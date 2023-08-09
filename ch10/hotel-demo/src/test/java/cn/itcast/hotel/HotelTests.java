package cn.itcast.hotel;

import cn.itcast.hotel.util.FileUtil;
import lombok.SneakyThrows;
import org.apache.http.HttpHost;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.common.xcontent.XContentType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.io.File;

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
public class HotelTests {
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
    public void testClientBuild() {
        System.out.println(restHighLevelClient);
    }

    @Test
    @SneakyThrows
    public void testIndexCreate() {
        // 从 Spring 资源文件读取 es 映射定义
        Resource resource = new ClassPathResource("/es/mapping/hotel.json");
        File jsonFile = resource.getFile();
        String source = FileUtil.getFileContent(jsonFile);
        // 新建索引创建请求
        CreateIndexRequest createIndexRequest = new CreateIndexRequest("hotel");
        // 添加映射定义
        createIndexRequest.source(source, XContentType.JSON);
        // 发送请求到 es 服务器
        restHighLevelClient.indices().create(createIndexRequest, RequestOptions.DEFAULT);
    }

    @Test
    @SneakyThrows
    public void testIndexExists() {
        String indexName = "hotel";
        GetIndexRequest request = new GetIndexRequest(indexName);
        boolean exists = restHighLevelClient.indices().exists(request, RequestOptions.DEFAULT);
        System.out.println(String.format("index %s %s", indexName, exists ? "exists" : "not exists"));
    }

    @Test
    @SneakyThrows
    public void testDeleteIndex(){
        DeleteIndexRequest request = new DeleteIndexRequest("hotel");
        restHighLevelClient.indices().delete(request, RequestOptions.DEFAULT);
    }
}
