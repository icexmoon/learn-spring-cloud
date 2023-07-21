package org.example.shopping.user.feignapi;

import org.example.shopping.user.Result;
import org.example.shopping.user.entity.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * Created with IntelliJ IDEA.
 *
 * @author : 魔芋红茶
 * @version : 1.0
 * @Project : shopping3
 * @Package : org.example.shopping.user.feignapi
 * @ClassName : .java
 * @createTime : 2023/7/21 15:25
 * @Email : icexmoon@qq.com
 * @Website : https://icexmoon.cn
 * @Description :
 */
public interface UserAPI {
    @GetMapping("/user/{id}")
    Result<User> getUserInfo(@Min(1) @NotNull @PathVariable Long id);
}
