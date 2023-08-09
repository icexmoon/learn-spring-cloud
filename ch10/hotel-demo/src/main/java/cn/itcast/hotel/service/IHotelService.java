package cn.itcast.hotel.service;

import cn.itcast.hotel.controller.HotelController;
import cn.itcast.hotel.pojo.Hotel;
import cn.itcast.hotel.pojo.PageResult;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

public interface IHotelService extends IService<Hotel> {
    PageResult search(HotelController.HotelQueryDTO requestParams);

    Map<String, List<String>> filters(HotelController.HotelQueryDTO requestParams);
}
