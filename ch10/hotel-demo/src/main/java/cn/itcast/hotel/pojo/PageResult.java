package cn.itcast.hotel.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @author : 魔芋红茶
 * @version : 1.0
 * @Project : hotel-demo
 * @Package : cn.itcast.hotel.pojo
 * @ClassName : .java
 * @createTime : 2023/8/8 17:42
 * @Email : icexmoon@qq.com
 * @Website : https://icexmoon.cn
 * @Description :
 */
@Data
@AllArgsConstructor
public class PageResult {
    private Long total;
    private List<HotelDoc> hotels;
}
