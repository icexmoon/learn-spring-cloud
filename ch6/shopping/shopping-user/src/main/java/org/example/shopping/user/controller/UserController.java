package org.example.shopping.user.controller;

import org.example.shopping.user.Result;
import org.example.shopping.user.config.PatternProperties;
import org.example.shopping.user.entity.User;
import org.example.shopping.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
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
@RequestMapping("/user")
@Validated
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private PatternProperties patternProperties;

    @GetMapping("/{id}")
    public Result<User> getUserInfo(@Min(1) @NotNull @PathVariable Long id) {
        return Result.success(userService.findUserById(id));
    }

    @GetMapping("/now")
    public Result<String> getNowTime() {
        String dateformat = patternProperties.getDateformat();
        String time = LocalDateTime.now().format(DateTimeFormatter.ofPattern(dateformat));
        return Result.success(time);
    }

    @GetMapping("/properties")
    public Result<PatternProperties> properties() {
        return Result.success(patternProperties);
    }
}
