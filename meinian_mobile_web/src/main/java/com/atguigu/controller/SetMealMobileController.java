package com.atguigu.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.constant.MessageConstant;
import com.atguigu.entity.Result;
import com.atguigu.pojo.Setmeal;
import com.atguigu.service.SetmealService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/SetMeal")
public class SetMealMobileController {
    @Reference
    private SetmealService setmealService;
    @RequestMapping("/setMealInfo")
    public Result setMealInfo(Integer id){
        try {
            Setmeal setmeal = setmealService.setMealInfo(id);
            return new Result(true, MessageConstant.GET_SETMEAL_LIST_SUCCESS, setmeal);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.GET_SETMEAL_LIST_FAIL);
        }

    }

    @RequestMapping("/getSetMeal")
    public Result getSetMeal() {
        List<Setmeal> list = setmealService.getSetMeal();
        try {
            return new Result(true, MessageConstant.GET_SETMEAL_LIST_SUCCESS, list);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.GET_SETMEAL_LIST_FAIL);
        }
    }

    @RequestMapping("/findById")
    public Result findById(Integer id) {
        Setmeal setmeal = setmealService.findById(id);
        try {
            return new Result(true, MessageConstant.GET_SETMEAL_LIST_SUCCESS, setmeal);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.GET_SETMEAL_LIST_FAIL);
        }
    }
}
