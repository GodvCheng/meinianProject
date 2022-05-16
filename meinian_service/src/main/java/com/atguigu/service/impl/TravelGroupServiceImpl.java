package com.atguigu.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.atguigu.dao.TravelGroupDao;
import com.atguigu.entity.PageResult;
import com.atguigu.pojo.TravelGroup;
import com.atguigu.service.TravelGroupService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;

@Service(interfaceClass = TravelGroupService.class)
@Transactional
public class TravelGroupServiceImpl implements TravelGroupService {
    @Autowired
    private TravelGroupDao travelGroupDao;


    @Override
    public List<TravelGroup> findAll() {

        return  travelGroupDao.findAll();
    }

    @Override
    public void add(Integer[] travelItemIds, TravelGroup travelGroup) {
        travelGroupDao.add(travelGroup);
        Integer travelGroupId = travelGroup.getId();
        setTravelGroupAndTravelItem(travelGroupId, travelItemIds);
    }

    @Override
    public void edit(Integer[] travelItemIds, TravelGroup travelGroup) {
        travelGroupDao.edit(travelGroup);
        Integer id = travelGroup.getId();
        travelGroupDao.delete(id);
        setTravelGroupAndTravelItem(id, travelItemIds);
    }

    @Override
    public void deleteTravelGroup(Integer id) {
        travelGroupDao.deleteTravelGroup(id);
    }

    /**
     *
     * @param travelGroupId 跟团游的id
     * @param travelItemIds 每个跟团游对应的自由行id
     */
    private void setTravelGroupAndTravelItem(int travelGroupId, Integer[] travelItemIds) {
        if (travelItemIds != null && travelItemIds.length > 0) {
            for (Integer travelItemId : travelItemIds) {
                HashMap<String, Integer> map = new HashMap<>();
                map.put("travelGroupId", travelGroupId);
                map.put("travelItemId", travelItemId);
                //向关联表中添加数据
                travelGroupDao.setTravelGroupAndTravelItem(map);
            }
        }
    }


    @Override
    public PageResult findByPage(Integer currentPage, Integer pageSize, String queryString) {
        PageHelper.startPage(currentPage, pageSize);
        //设置查询条件，返回分页结果
        Page page = travelGroupDao.findByPage(queryString);
        return new PageResult(page.getTotal(), page.getResult());
    }

    @Override
    public TravelGroup findById(Integer id) {
        TravelGroup travelGroup = travelGroupDao.findById(id);
        return travelGroup;
    }

    @Override
    public List<Integer> findTravelItemIdByTravelGroupId(Integer id) {
        return travelGroupDao.findTravelItemIdByTravelGroupId(id);
    }

}
