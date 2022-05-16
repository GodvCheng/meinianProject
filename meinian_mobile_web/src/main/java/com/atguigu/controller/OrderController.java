package com.atguigu.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.constant.MessageConstant;
import com.atguigu.constant.RedisMessageConstant;
import com.atguigu.entity.Result;
import com.atguigu.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.JedisPool;

import java.util.Map;

@RestController
@RequestMapping("/order")
public class OrderController {
    @Reference
    private OrderService orderService;
    @Autowired
    private JedisPool jedisPool;

    @RequestMapping("/saveOrder")
    public Result saveOrder(@RequestBody Map map) {
        try {
            String telephone = (String) map.get("telephone");
            String validateCode = (String) map.get("validateCode");
            String code = jedisPool.getResource().get(telephone + RedisMessageConstant.SENDTYPE_ORDER);
            if (code == null && !code.equals(validateCode)) {
                return new Result(false, MessageConstant.VALIDATECODE_ERROR);
            }
            Result result = orderService.saveOrder(map);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.ORDERSETTING_FAIL);

        }
    }

    @RequestMapping("/findById")
    public Result findById(Integer id) throws Exception {
        Map<String, Object> map = orderService.findById(id);
        return new Result(true, MessageConstant.ORDER_SUCCESS, map);
    }
}
