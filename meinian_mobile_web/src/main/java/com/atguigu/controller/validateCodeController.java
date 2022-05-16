package com.atguigu.controller;

import com.atguigu.constant.MessageConstant;
import com.atguigu.constant.RedisMessageConstant;
import com.atguigu.entity.Result;
import com.atguigu.util.SMSUtils;
import com.atguigu.util.ValidateCodeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.JedisPool;

@RequestMapping("/validateCode")
@RestController
public class validateCodeController {
    @Autowired
    private JedisPool jedisPool;

    @RequestMapping("/sendValidateCode")
    public Result sendValidateCode(String telephone) {
        try {
            //1.生成验证码
            Integer code = ValidateCodeUtils.generateValidateCode(6);
            //2.将验证码发送到手机
            SMSUtils.sendShortMessage(telephone, code.toString(), "5");
            //3.将验证码存到redis中，设置5分钟有效时间
            jedisPool.getResource().setex(telephone + RedisMessageConstant.SENDTYPE_ORDER, 5 * 60, code.toString());
            return new Result(true, MessageConstant.SEND_VALIDATECODE_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.SEND_VALIDATECODE_FAIL);
        }

    }
}
