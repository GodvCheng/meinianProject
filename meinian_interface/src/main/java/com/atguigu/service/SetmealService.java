package com.atguigu.service;

import com.atguigu.entity.PageResult;
import com.atguigu.pojo.Setmeal;
import com.atguigu.pojo.TravelGroup;

import java.util.List;
import java.util.Map;

public interface SetmealService {
    void add(Setmeal setmeal,Integer [] ids);

    PageResult findByPage(Integer currentPage, Integer pageSize, String queryString);

    void delete(Integer id);

    List<Setmeal> getSetMeal();

    Setmeal findById(Integer id);

    Setmeal setMealInfo(Integer id);

    List<Map<String, Object>> findSetMealNameAndOrderCount();

    List<TravelGroup> findAll();

    List<Integer> findTravelGroup(Integer id);

    void edit(Integer [] travelgroupIds, Setmeal setmeal);
}
