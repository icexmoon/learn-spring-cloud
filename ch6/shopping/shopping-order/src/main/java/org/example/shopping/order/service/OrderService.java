package org.example.shopping.order.service;

import org.example.shopping.order.Result;
import org.example.shopping.order.client.UserClient;
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
    @Autowired
    private UserClient userClient;

    public Order findOrderById(Long orderId) {
        Order order = orderMapper.findById(orderId);
        Result<User> userInfo = userClient.getUserInfo(order.getUserId());
        order.setUser(userInfo.getData());
        return order;
    }
}
