package org.example.shopping.order.service;

import org.example.shopping.order.Result;
import org.example.shopping.order.entity.Order;
import org.example.shopping.order.entity.User;
import org.example.shopping.order.mapper.OrderMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * Created with IntelliJ IDEA.
 *
 * @author : 魔芋红茶
 * @version : 1.0
 * @Project : shopping
 * @Package : org.example.shopping.order.service
 * @ClassName : .java
 * @createTime : 2023/7/14 8:32
 * @Email : icexmoon@qq.com
 * @Website : https://icexmoon.cn
 * @Description :
 */
@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
@Service
public class OrderService {
    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private RestTemplate restTemplate;

    public Order findOrderById(Long orderId) {
        Order order = orderMapper.findById(orderId);
        String url = String.format("http://shopping-user/user/%d", order.getUserId());
        Result<?> result = restTemplate.getForObject(url, Result.class);
        User user = Result.parseData(result, User.class);
        order.setUser(user);
        return order;
    }
}
