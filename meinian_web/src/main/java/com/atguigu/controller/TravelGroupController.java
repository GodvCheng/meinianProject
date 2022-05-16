package com.atguigu.controller;


import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.constant.MessageConstant;
import com.atguigu.entity.PageResult;
import com.atguigu.entity.QueryPageBean;
import com.atguigu.entity.Result;
import com.atguigu.pojo.TravelGroup;
import com.atguigu.service.TravelGroupService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/TravelGroup")
public class TravelGroupController {
    @Reference
    private TravelGroupService travelGroupService;

    @RequestMapping("/findAll")
    public Result findAll() {
        List<TravelGroup> list = travelGroupService.findAll();
        return new Result(true, MessageConstant.QUERY_TRAVELGROUP_SUCCESS,list);
    }

    @RequestMapping("/edit")
    public Result edit(Integer[] travelItemIds, @RequestBody TravelGroup travelGroup) {
        travelGroupService.edit(travelItemIds, travelGroup);
        try {
            return new Result(true, MessageConstant.EDIT_TRAVELGROUP_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.EDIT_TRAVELGROUP_FAIL);
        }
    }

    @RequestMapping("/add")
    public Result add(Integer[] travelItemIds, @RequestBody TravelGroup travelGroup) {
        travelGroupService.add(travelItemIds, travelGroup);
        try {
            return new Result(true, MessageConstant.ADD_TRAVELGROUP_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.ADD_TRAVELGROUP_FAIL);
        }
    }

    /**
     * 分页查找
     *
     * @param queryPageBean
     * @return
     */
    @RequestMapping("/find")
    public PageResult findByPage(@RequestBody QueryPageBean queryPageBean) {
        PageResult pageResult = travelGroupService.findByPage(queryPageBean.getCurrentPage(), queryPageBean.getPageSize(), queryPageBean.getQueryString());
        return pageResult;
    }

    @RequestMapping("/findById")
    public Result findById(Integer id) {
        TravelGroup travelGroup = travelGroupService.findById(id);
        try {
            return new Result(true, MessageConstant.EDIT_TRAVELGROUP_SUCCESS, travelGroup);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(true, MessageConstant.EDIT_TRAVELGROUP_FAIL, travelGroup);
        }
    }

    @RequestMapping("/findTravelItemIdByTravelGroupId")
    public Result findTravelItemIdByTravelGroupId(Integer id) {
        List<Integer> ids = travelGroupService.findTravelItemIdByTravelGroupId(id);
        try {
            return new Result(true, "查询成功", ids);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(true, "查询失败");
        }
    }

    @RequestMapping("/deleteTravelGroup")
    public Result deleteTravelGroup(Integer id) {
        travelGroupService.deleteTravelGroup(id);
        try {
            return new Result(true, MessageConstant.DELETE_TRAVELGROUP_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.DELETE_TRAVELGROUP_FAIL);
        }

    }
}
