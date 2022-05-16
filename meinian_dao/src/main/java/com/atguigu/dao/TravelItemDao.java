package com.atguigu.dao;

import com.atguigu.pojo.TravelItem;
import com.github.pagehelper.Page;

import java.util.List;

public interface TravelItemDao {

    void add(TravelItem travelItem);

    Page findByPage(String queryString);

    void delete(Integer id);

    TravelItem findById(Integer id);

    void update(TravelItem travelItem);

    List<TravelItem> findAll();

    int findCountByTravelItemId(Integer id);

    List<TravelItem> findTravelItemById(Integer id);
}
