package com.atguigu.service;

import com.atguigu.entity.PageResult;
import com.atguigu.pojo.TravelItem;

import java.util.List;

public interface TravelItemService {

    void add(TravelItem travelItem);

    PageResult findByPage(Integer currentPage, Integer pageSize, String queryString);

    void delete(Integer id);

    TravelItem findById(Integer id);

    void update(TravelItem travelItem);

    List<TravelItem> findAll();

}
