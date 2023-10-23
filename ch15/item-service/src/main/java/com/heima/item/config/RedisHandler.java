package com.heima.item.config;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.heima.item.pojo.Item;
import com.heima.item.pojo.ItemStock;
import com.heima.item.service.IItemStockService;
import com.heima.item.service.impl.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @author : 魔芋红茶
 * @version : 1.0
 * @Project : item-service
 * @Package : com.heima.item.config
 * @ClassName : .java
 * @createTime : 2023/10/22 15:32
 * @Email : icexmoon@qq.com
 * @Website : https://icexmoon.cn
 * @Description : APP 初始化
 */
@Configuration
public class RedisHandler {
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    private ItemService itemService;
    @Autowired
    private IItemStockService itemStockService;
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    @Bean
    CommandLineRunner commandLineRunner() {
        return args -> {
            // 获取所有的商品信息
            List<Item> items = itemService.list();
            for (Item item : items) {
                // 将商品信息写入 Redis 缓存
                RedisHandler.this.saveItem2Redis(item);
            }
            // 获取所有库存信息
            List<ItemStock> stocks = itemStockService.list();
            for (ItemStock itemStock : stocks) {
                // 将库存信息写入 Redis 缓存
                stringRedisTemplate.opsForValue().set("item:stock:id:" + itemStock.getId(), OBJECT_MAPPER.writeValueAsString(itemStock));
            }
        };
    }

    /**
     * 将商品信息保存到 Redis 缓存
     * @param item
     */
    public void saveItem2Redis(Item item) {
        try {
            stringRedisTemplate.opsForValue().set("item:id:" + item.getId(), OBJECT_MAPPER.writeValueAsString(item));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    /**
     * 从 Redis 中删除某个商品的缓存
     * @param id
     */
    public void deleteItemFromRedis(Long id) {
        stringRedisTemplate.delete("item:id:" + id);
    }
}
