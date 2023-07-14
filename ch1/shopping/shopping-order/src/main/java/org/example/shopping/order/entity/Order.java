package org.example.shopping.order.entity;

import lombok.Data;

/**
 * Created with IntelliJ IDEA.
 *
 * @author : 魔芋红茶
 * @version : 1.0
 * @Project : shopping
 * @Package : org.example.shopping.order.entity
 * @ClassName : .java
 * @createTime : 2023/7/13 21:18
 * @Email : icexmoon@qq.com
 * @Website : https://icexmoon.cn
 * @Description :
 */
@Data
public class Order {
    private Long id;
    private Long userId;
    private String name;
    private Long price;
    private Integer num;
    private User user;
}
