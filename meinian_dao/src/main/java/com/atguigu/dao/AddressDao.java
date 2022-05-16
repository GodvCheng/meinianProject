package com.atguigu.dao;

import com.atguigu.pojo.Address;
import com.github.pagehelper.Page;

import java.util.List;
import java.util.Map;

public interface AddressDao {

    List<Address> findAllMaps();

    Page findPage(String queryString);

    void deleteById(Integer id);

    void addAddress(Map map);
}
