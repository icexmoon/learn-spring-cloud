package com.heima.item.canal;

import com.github.benmanes.caffeine.cache.Cache;
import com.heima.item.config.RedisHandler;
import com.heima.item.pojo.Item;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import top.javatool.canal.client.annotation.CanalTable;
import top.javatool.canal.client.handler.EntryHandler;

/**
 * Created with IntelliJ IDEA.
 *
 * @author : 魔芋红茶
 * @version : 1.0
 * @Project : item-service
 * @Package : com.heima.item.canal
 * @ClassName : .java
 * @createTime : 2023/10/23 18:17
 * @Email : icexmoon@qq.com
 * @Website : https://icexmoon.cn
 * @Description :
 */
@CanalTable("tb_item")
@Component
public class ItemHandler implements EntryHandler<Item> {
    @Autowired
    private RedisHandler redisHandler;
    @Autowired
    private Cache<Long, Item> itemCache;
    @Override
    public void insert(Item item) {
        // 写入本地 JVM 缓存
        itemCache.put(item.getId(), item);
        // 写入 Redis 缓存
        redisHandler.saveItem2Redis(item);
    }

    @Override
    public void update(Item before, Item after) {
        // 写入本地 JVM 缓存
        itemCache.put(after.getId(), after);
        // 写入 Redis 缓存
        redisHandler.saveItem2Redis(after);
    }

    @Override
    public void delete(Item item) {
        // 从本地 JVM 缓存删除
        itemCache.invalidate(item.getId());
        // 从 Redis 缓存删除
        redisHandler.deleteItemFromRedis(item.getId());
    }
}
