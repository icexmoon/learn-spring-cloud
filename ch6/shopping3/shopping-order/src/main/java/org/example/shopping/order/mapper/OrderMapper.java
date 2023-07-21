package org.example.shopping.order.mapper;

import org.apache.ibatis.annotations.Select;
import org.example.shopping.order.entity.Order;

/**
 * Created with IntelliJ IDEA.
 *
 * @author : 魔芋红茶
 * @version : 1.0
 * @Project : shopping
 * @Package : org.example.shopping.order.mapper
 * @ClassName : .java
 * @createTime : 2023/7/14 8:31
 * @Email : icexmoon@qq.com
 * @Website : https://icexmoon.cn
 * @Description :
 */
public interface OrderMapper {
    @Select("select * from tb_order where id = #{id}")
    Order findById(Long id);
}
