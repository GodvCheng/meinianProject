package com.atguigu.dao;

import com.atguigu.pojo.TravelGroup;
import com.github.pagehelper.Page;

import java.util.HashMap;
import java.util.List;

public interface TravelGroupDao {

    void add(TravelGroup travelGroup);

    void setTravelGroupAndTravelItem(HashMap<String, Integer> map);

    Page findByPage(String queryString);

    TravelGroup findById(Integer id);

    List<Integer> findTravelItemIdByTravelGroupId(Integer id);

    void edit(TravelGroup travelGroup);

    void delete(Integer id);

    void deleteTravelGroup(Integer id);

    List<TravelGroup> findAll();

    List<TravelGroup> findTravelGroupById(Integer id);
}
