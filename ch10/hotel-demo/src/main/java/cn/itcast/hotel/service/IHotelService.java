package cn.itcast.hotel.service;

import cn.itcast.hotel.controller.HotelController;
import cn.itcast.hotel.pojo.Hotel;
import cn.itcast.hotel.pojo.PageResult;
import com.baomidou.mybatisplus.extension.service.IService;

public interface IHotelService extends IService<Hotel> {
    PageResult search(HotelController.ListRequestParams requestParams);
}
