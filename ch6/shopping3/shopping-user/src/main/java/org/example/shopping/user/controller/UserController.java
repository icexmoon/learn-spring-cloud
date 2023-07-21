package org.example.shopping.user.controller;

import org.example.shopping.user.Result;
import org.example.shopping.user.config.PatternProperties;
import org.example.shopping.user.entity.User;
import org.example.shopping.user.feignapi.UserAPI;
import org.example.shopping.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Created with IntelliJ IDEA.
 *
 * @author : 魔芋红茶
 * @version : 1.0
 * @Project : shopping
 * @Package : org.example.shopping.user.controller
 * @ClassName : .java
 * @createTime : 2023/7/14 9:23
 * @Email : icexmoon@qq.com
 * @Website : https://icexmoon.cn
 * @Description :
 */
@RestController
@RequestMapping("")
@Validated
public class UserController implements UserAPI {
    @Autowired
    private UserService userService;
    @Autowired
    private PatternProperties patternProperties;

    @Override
    public Result<User> getUserInfo(Long id) {
        return Result.success(userService.findUserById(id));
    }

    @GetMapping("/user/now")
    public Result<String> getNowTime() {
        String dateformat = patternProperties.getDateformat();
        String time = LocalDateTime.now().format(DateTimeFormatter.ofPattern(dateformat));
        return Result.success(time);
    }

    @GetMapping("/user/properties")
    public Result<PatternProperties> properties() {
        return Result.success(patternProperties);
    }
}
