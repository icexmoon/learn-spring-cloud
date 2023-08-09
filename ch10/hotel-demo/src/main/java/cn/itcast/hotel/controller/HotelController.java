package cn.itcast.hotel.controller;

import cn.itcast.hotel.pojo.PageResult;
import cn.itcast.hotel.service.IHotelService;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 *
 * @author : 魔芋红茶
 * @version : 1.0
 * @Project : hotel-demo
 * @Package : cn.itcast.hotel.controller
 * @ClassName : .java
 * @createTime : 2023/8/8 17:38
 * @Email : icexmoon@qq.com
 * @Website : https://icexmoon.cn
 * @Description :
 */
@RestController
@RequestMapping("/hotel")
public class HotelController {
    @Autowired
    private IHotelService hotelService;

    @Data
    public static class HotelQueryDTO {
        private String key;
        private Integer page;
        private Integer size;
        private String sortBy;
        private String city;
        private String starName;
        private String brand;
        private Long maxPrice;
        private Long minPrice;
        private String location;
    }

    @PostMapping("/list")
    public PageResult list(@RequestBody HotelQueryDTO requestParams) {
        return hotelService.search(requestParams);
    }

    @PostMapping("/filters")
    public Map<String, List<String>> filters(@RequestBody HotelQueryDTO requestParams){
        return hotelService.filters(requestParams);
    }
}
