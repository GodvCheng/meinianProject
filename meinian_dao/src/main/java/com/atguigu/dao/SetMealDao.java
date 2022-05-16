package com.atguigu.dao;

import com.atguigu.pojo.Setmeal;
import com.atguigu.pojo.TravelGroup;
import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.Param;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface SetMealDao {

    void add(Setmeal setmeal);

    void setMealAndTravelGroup(HashMap<String, Integer> map);

    Page findByPage(@Param("queryString") String queryString);

    void delete(Integer id);

    List<Setmeal> getSetMeal();

    Setmeal findById(Integer id);

    Setmeal setMealInfo(Integer id);

    List<Map<String, Object>> findSetMealNameAndOrderCount();

    List<TravelGroup> findAll();

    List<Integer> findTravelGroup(Integer id);

    void edit(Setmeal setmeal);

    void deleteAssociation(Integer id);

    void setSetMealAndTravelGroup(Map<String, Integer> map);
}
