package com.atguigu.dao;

import com.atguigu.pojo.OrderSetting;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface OrderSettingDao {
    void add(OrderSetting orderSetting);

    int findCount(Date orderDate);

    void update(OrderSetting orderSetting);

    List<OrderSetting> getReservation(@Param("startDate") String startDate, @Param("endDate") String endDate);

    OrderSetting getOrderSettingCount(Date orderDate);

    void editReservations(OrderSetting orderSetting);

    Map findById(Integer id);
}
