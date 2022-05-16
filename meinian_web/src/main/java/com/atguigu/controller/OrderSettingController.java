package com.atguigu.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.constant.MessageConstant;
import com.atguigu.entity.Result;
import com.atguigu.pojo.OrderSetting;
import com.atguigu.service.OrderSettingService;
import com.atguigu.util.POIUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/OrderSetting")
public class OrderSettingController {
    @Reference
    private OrderSettingService orderSettingService;

    /**
     * 文件上传
     * @param excelFile
     * @return
     */
    @RequestMapping("/upload")
    public Result upload(MultipartFile excelFile) { // 形参名和前端请求参数名保持一致
        try {
            List<String[]> list = POIUtils.readExcel(excelFile); // 读取上传的文件
            List<OrderSetting> orderSettingList = new ArrayList<>();
            for (String[] strArray : list) { //遍历每一条数据
                String orderDate = strArray[0]; //获取第一列数据
                String number = strArray[1];//获取第二列数据
                OrderSetting orderSetting = new OrderSetting(new Date(orderDate), Integer.parseInt(number));
                orderSettingList.add(orderSetting);
            }
            orderSettingService.addBatch(orderSettingList);
            return new Result(true, MessageConstant.UPLOAD_SUCCESS);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return new Result(false, MessageConstant.UPLOAD_FAIL);
    }

    @RequestMapping("/getReservation")
    public Result getReservation(String date) {
        try {
            List<Map> list = orderSettingService.getReservation(date);
            return new Result(true, MessageConstant.GET_ORDERSETTING_SUCCESS, list);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.GET_ORDERSETTING_FAIL);
        }
    }

    @RequestMapping("/setNumber")
    public Result setNumber(@RequestBody Map map) {
        try {
            orderSettingService.setNumber(map);
            return new Result(true, MessageConstant.ORDERSETTING_SUCCESS);
        } catch (Exception e) {
            return new Result(false, MessageConstant.ORDERSETTING_FAIL);
        }
    }

}
