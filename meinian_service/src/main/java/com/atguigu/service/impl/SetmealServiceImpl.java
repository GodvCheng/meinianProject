package com.atguigu.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.atguigu.constant.RedisConstant;
import com.atguigu.dao.SetMealDao;
import com.atguigu.entity.PageResult;
import com.atguigu.pojo.Setmeal;
import com.atguigu.pojo.TravelGroup;
import com.atguigu.service.SetmealService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import redis.clients.jedis.JedisPool;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service(interfaceClass = SetmealService.class)
@Transactional
public class SetmealServiceImpl implements SetmealService {
    @Autowired
    private SetMealDao setmealDao;
    @Autowired
    private JedisPool jedisPool;

    @Override
    public void delete(Integer id) {
        setmealDao.delete(id);
    }

    @Override
    public PageResult findByPage(Integer currentPage, Integer pageSize, String queryString) {
        PageHelper.startPage(currentPage, pageSize);
        Page page = setmealDao.findByPage(queryString);
        return new PageResult(page.getTotal(), page.getResult());
    }

    @Override
    public void add(Setmeal setmeal, Integer[] ids) {
        setmealDao.add(setmeal);
        Integer setMealId = setmeal.getId();//获取到setMeal的id
        setMealAndTravelGroup(setMealId, ids);
        //将图片名称保存到Redis
        savePic2Redis(setmeal.getImg());
    }

    private void setMealAndTravelGroup(Integer setMealId, Integer[] ids) {
        if (ids != null && ids.length > 0) {
            for (Integer id : ids) {
                HashMap<String, Integer> map = new HashMap<>();
                map.put("travelGroupId", id);
                map.put("setMealId", setMealId);
                setmealDao.setMealAndTravelGroup(map);
            }
        }
    }

    //将图片名称保存到Redis
    private void savePic2Redis(String pic) {
        jedisPool.getResource().sadd(RedisConstant.SETMEAL_PIC_DB_RESOURCES, pic);
    }

    @Override
    public List<Setmeal> getSetMeal() {
        return setmealDao.getSetMeal();
    }

    @Override
    public Setmeal findById(Integer id) {
        return setmealDao.findById(id);
    }

    @Override
    public Setmeal setMealInfo(Integer id) {
        return setmealDao.setMealInfo(id);
    }

    @Override
    public List<Map<String, Object>> findSetMealNameAndOrderCount() {
        return setmealDao.findSetMealNameAndOrderCount();
    }

    @Override
    public List<TravelGroup> findAll() {
        return setmealDao.findAll();
    }

    @Override
    public List<Integer> findTravelGroup(Integer id) {
        return setmealDao.findTravelGroup(id);
    }

    @Override
    public void edit(Integer[] travelgroupIds, Setmeal setmeal) {
        //编辑套餐信息
        setmealDao.edit(setmeal);
        this.savePic2Redis(setmeal.getImg());
        Integer id = setmeal.getId();
        //删除关联表中的数据
        setmealDao.deleteAssociation(id);
        //编辑套餐对应的跟团游信息
        setSetMealAndTravelGroup(id, travelgroupIds);
    }

    /**
     * @param id             套餐表的id
     * @param travelgroupIds 每个套餐对应的报团游id
     */
    private void setSetMealAndTravelGroup(Integer id, Integer[] travelgroupIds) {
        if (travelgroupIds != null && travelgroupIds.length > 0) {
            for (Integer travelgroupId : travelgroupIds) {
                Map<String, Integer> map = new HashMap();
                map.put("setMealId", id);
                map.put("travelGroupIds", travelgroupId);
                setmealDao.setSetMealAndTravelGroup(map); //map的键和mapper映射中的属性对应
            }
        }

        /*
        if (travelItemIds != null && travelItemIds.length > 0) {
            for (Integer travelItemId : travelItemIds) {
                HashMap<String, Integer> map = new HashMap<>();
                map.put("travelGroupId", travelGroupId);
                map.put("travelItemId", travelItemId);
                //向关联表中添加数据
                travelGroupDao.setTravelGroupAndTravelItem(map);
            }
        }
         */
    }
}
