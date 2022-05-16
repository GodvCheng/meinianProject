package com.atguigu.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.atguigu.dao.TravelItemDao;
import com.atguigu.entity.PageResult;
import com.atguigu.pojo.TravelItem;
import com.atguigu.service.TravelItemService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Service
public class TravelItemServiceImpl implements TravelItemService {
    @Autowired
    private TravelItemDao travelItemDao;

    @Override
    public void add(TravelItem travelItem) {
        travelItemDao.add(travelItem);
    }

    @Override
    public PageResult findByPage(Integer currentPage, Integer pageSize, String queryString) {
        PageHelper.startPage(currentPage, pageSize);
        Page page = travelItemDao.findByPage(queryString);
        return new PageResult(page.getTotal(), page.getResult());
    }

    @Override
    public void delete(Integer id) {
        int count = travelItemDao.findCountByTravelItemId(id);
        if (count > 0) {
            throw new RuntimeException("删除失败，存在关联关系");
        }
        travelItemDao.delete(id);
    }

    @Override
    public TravelItem findById(Integer id) {
        return travelItemDao.findById(id);
    }

    @Override
    public void update(TravelItem travelItem) {
        travelItemDao.update(travelItem);
    }

    @Override
    public List<TravelItem> findAll() {
        return travelItemDao.findAll();
    }
}
