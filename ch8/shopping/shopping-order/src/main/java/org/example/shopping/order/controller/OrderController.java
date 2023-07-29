package org.example.shopping.order.controller;

import org.example.shopping.feignapi.entity.Result;
import org.example.shopping.order.entity.Order;
import org.example.shopping.order.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * Created with IntelliJ IDEA.
 *
 * @author : 魔芋红茶
 * @version : 1.0
 * @Project : shopping
 * @Package : org.example.shopping.order.controller
 * @ClassName : .java
 * @createTime : 2023/7/14 8:33
 * @Email : icexmoon@qq.com
 * @Website : https://icexmoon.cn
 * @Description :
 */
@RestController
@RequestMapping("/order")
@Validated
public class OrderController {
    @Autowired
    private OrderService orderService;

    @GetMapping("/{id}")
    public Result<Order> getOrderInfo(@Min(1) @NotNull @PathVariable Long id,
                                      @RequestHeader(value = "X-Request-color", required = false) String color,
                                      @RequestHeader(value = "X-Request-msg", required = false) String msg) {
        System.out.println(String.format("color:%s", color));
        System.out.println(String.format("msg:%s", msg));
        return Result.success(orderService.findOrderById(id));
    }
}
