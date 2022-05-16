package com.atguigu.controller;


import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.constant.MessageConstant;
import com.atguigu.entity.PageResult;
import com.atguigu.entity.QueryPageBean;
import com.atguigu.entity.Result;
import com.atguigu.pojo.TravelItem;
import com.atguigu.service.TravelItemService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/TravelItem")
public class TravelItemController {
    @Reference//远程调用服务
    private TravelItemService travelItemService;

    /**
     * 查询自由行的所有数据
     *
     * @return
     */
    @RequestMapping("/findAll")

    public Result findAll() {
        List<TravelItem> list = travelItemService.findAll();
        return new Result(true, MessageConstant.ADD_TRAVELGROUP_SUCCESS, list);
    }

    @RequestMapping("/add")
    @PreAuthorize("hasAuthority('TRAVELITEM_ADD')")//权限校验
    public Result add(@RequestBody TravelItem travelItem) {
        try {
            travelItemService.add(travelItem);
            return new Result(true, MessageConstant.ADD_TRAVELITEM_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.ADD_TRAVELITEM_FAIL);//返回给前端
        }
    }

    /**
     * 根据条件查询
     *
     * @param queryPageBean
     * @return
     */
    @RequestMapping("/find")
    public PageResult findByPage(@RequestBody QueryPageBean queryPageBean) {
        PageResult pageResult = travelItemService.findByPage(queryPageBean.getCurrentPage(), queryPageBean.getPageSize(), queryPageBean.getQueryString());
        return pageResult;
    }

    @RequestMapping("/delete")
    @PreAuthorize("hasAuthority('TRAVELITEM_DELETE')")//权限校验，使用TRAVELITEM_DELETE123测试
    public Result delete(Integer id) {
        travelItemService.delete(id);
        try {
            return new Result(true, MessageConstant.DELETE_TRAVELITEM_SUCCESS);
        } catch (RuntimeException e) {
            e.printStackTrace();
            return new Result(false, e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.DELETE_TRAVELITEM_FAIL);
        }
    }

    @RequestMapping("/findById")
    public Result findById(Integer id) {
        TravelItem travelItem = travelItemService.findById(id);
        try {
            return new Result(true, MessageConstant.EDIT_TRAVELITEM_SUCCESS, travelItem);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.EDIT_TRAVELITEM_FAIL);
        }
    }

    @RequestMapping("/update")
    @PreAuthorize("hasAuthority('TRAVELITEM_EDIT')")//权限校验
    public Result update(@RequestBody TravelItem travelItem) {
        travelItemService.update(travelItem);
        try {
            return new Result(true, MessageConstant.EDIT_TRAVELITEM_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.EDIT_TRAVELITEM_FAIL);
        }
    }

}
