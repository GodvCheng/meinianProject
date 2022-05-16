package com.atguigu.service;

import com.atguigu.pojo.OrderSetting;

import java.util.List;
import java.util.Map;

public interface OrderSettingService {
    void addBatch(List<OrderSetting> orderSettingList);

    List<Map> getReservation(String date);

    void setNumber(Map map);
}
