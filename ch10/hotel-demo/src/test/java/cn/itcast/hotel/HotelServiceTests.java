package cn.itcast.hotel;

import cn.itcast.hotel.controller.HotelController;
import cn.itcast.hotel.service.IHotelService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 *
 * @author : 魔芋红茶
 * @version : 1.0
 * @Project : hotel-demo
 * @Package : cn.itcast.hotel
 * @ClassName : .java
 * @createTime : 2023/8/9 18:43
 * @Email : icexmoon@qq.com
 * @Website : https://icexmoon.cn
 * @Description :
 */
@SpringBootTest
public class HotelServiceTests {
    @Autowired
    private IHotelService hotelService;

    @Test
    void testFilters(){
        Map<String, List<String>> filters = hotelService.filters(new HotelController.HotelQueryDTO());
        System.out.println(filters);
    }
}
