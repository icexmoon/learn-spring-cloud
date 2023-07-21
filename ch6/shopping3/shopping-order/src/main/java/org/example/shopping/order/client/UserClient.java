package org.example.shopping.order.client;

import org.example.shopping.user.feignapi.UserAPI;
import org.springframework.cloud.openfeign.FeignClient;

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
@FeignClient(value = "shopping-user")
public interface UserClient extends UserAPI {
}
