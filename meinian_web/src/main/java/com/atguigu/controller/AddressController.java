package com.atguigu.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.entity.PageResult;
import com.atguigu.entity.QueryPageBean;
import com.atguigu.entity.Result;
import com.atguigu.pojo.Address;
import com.atguigu.service.AddressService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/Address")
public class AddressController {
    @Reference
    private AddressService addressService;

    @RequestMapping("/findAllMaps")
    public Map findAllMaps() {
        Map<String, List> map = new HashMap();
        List<Address> listMaps = addressService.findAllMaps();
        List<Map> addressGridList = new ArrayList<>();
        List<Map> addressNameList = new ArrayList<>();
        for (Address address : listMaps) {
            Map addressNameMap = new HashMap<>();
            String addressName = address.getAddressName();
            addressNameMap.put("addressName", addressName);
            addressNameList.add(addressNameMap);

            Map gridMaps = new HashMap();
            gridMaps.put("lng", address.getLng());
            gridMaps.put("lat", address.getLat());
            addressGridList.add(gridMaps);
        }
        map.put("gridnMaps", addressGridList);
        map.put("nameMaps", addressNameList);
        return map;
    }
    @RequestMapping("/findPage")
    public PageResult findPage(@RequestBody QueryPageBean queryPageBean){
       return addressService.findPage(queryPageBean);
    }
    @RequestMapping("/deleteById")
    public Result deleteById(Integer id){
        try {
            addressService.deleteById(id);
            return new Result(true, "删除成功");
        } catch (Exception e) {
            return new Result(false, "删除失败");
        }
    }
    @RequestMapping("/addAddress")
    public Result addAddress(@RequestBody Map map){
        try {
            addressService.addAddress(map);
            return new Result(true, "添加成功");
        } catch (Exception e) {
            return new Result(false, "添加失败");
        }
    }
}
