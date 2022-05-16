package com.atguigu.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.atguigu.dao.OrderSettingDao;
import com.atguigu.pojo.OrderSetting;
import com.atguigu.service.OrderSettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service(interfaceClass = OrderSettingService.class)
@Transactional
public class OrderSettingServiceImpl implements OrderSettingService {
    @Autowired
    private OrderSettingDao orderSettingDao;

    @Override
    public void setNumber(Map map) {
        String number = (String) map.get("number");
        String orderDate = (String) map.get("orderDate");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date = null;
        try {
            date = sdf.parse(orderDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        OrderSetting orderSetting = new OrderSetting();
        orderSetting.setNumber(Integer.parseInt(number));
        orderSetting.setOrderDate(date);
        int count = orderSettingDao.findCount(date);
        if (count > 0) {
            orderSettingDao.update(orderSetting);
        } else {
            orderSettingDao.add(orderSetting);
        }
    }

    @Override
    public List<Map> getReservation(String date) {
        String startDate = date + "-1";
        String endDate = date + "-31";
        List<Map> listMap = new ArrayList<>();
        List<OrderSetting> listOrderSetting = orderSettingDao.getReservation(startDate, endDate);
        for (OrderSetting orderSetting : listOrderSetting) {
            HashMap<String, Object> map = new HashMap<>();
            map.put("date", orderSetting.getOrderDate().getDate()); //注意key的值和前端设置的属性值保持一致
            map.put("number", orderSetting.getNumber());
            map.put("reservations", orderSetting.getReservations());
            listMap.add(map);
        }
        return listMap;
    }

    @Override
    public void addBatch(List<OrderSetting> orderSettingList) {
        for (OrderSetting orderSetting : orderSettingList) {
            int count = orderSettingDao.findCount(orderSetting.getOrderDate()); //查询数据库中是否已经存在该数据
            if (count > 0) {
                orderSettingDao.update(orderSetting);
            } else {
                orderSettingDao.add(orderSetting);
            }
        }
    }
}
