package org.example.shopping.user.mapper;

import org.apache.ibatis.annotations.Select;
import org.example.shopping.user.entity.User;

/**
 * Created with IntelliJ IDEA.
 *
 * @author : 魔芋红茶
 * @version : 1.0
 * @Project : shopping
 * @Package : org.example.shopping.user.mapper
 * @ClassName : .java
 * @createTime : 2023/7/14 9:17
 * @Email : icexmoon@qq.com
 * @Website : https://icexmoon.cn
 * @Description :
 */
public interface UserMapper {
    @Select("select * from tb_user where id = #{id}")
    User findUserById(Long id);
}
