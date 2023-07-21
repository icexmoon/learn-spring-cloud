package org.example.shopping.order.client;

import org.example.shopping.order.Result;
import org.example.shopping.order.config.DefaultFeignConfiguration;
import org.example.shopping.order.entity.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * Created with IntelliJ IDEA.
 *
 * @author : 魔芋红茶
 * @version : 1.0
 * @Project : shopping
 * @Package : org.example.shopping.order.client
 * @ClassName : .java
 * @createTime : 2023/7/20 21:33
 * @Email : icexmoon@qq.com
 * @Website : https://icexmoon.cn
 * @Description :
 */
@FeignClient(value = "shopping-user", configuration = DefaultFeignConfiguration.class)
public interface UserClient {
    @GetMapping("/user/{id}")
    Result<User> getUserInfo(@PathVariable Long id);
}
