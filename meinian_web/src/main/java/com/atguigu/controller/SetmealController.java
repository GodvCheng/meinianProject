package com.atguigu.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.constant.MessageConstant;
import com.atguigu.constant.RedisConstant;
import com.atguigu.entity.PageResult;
import com.atguigu.entity.QueryPageBean;
import com.atguigu.entity.Result;
import com.atguigu.pojo.Setmeal;
import com.atguigu.pojo.TravelGroup;
import com.atguigu.service.SetmealService;
import com.atguigu.util.QiniuUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import redis.clients.jedis.JedisPool;

import java.util.List;
import java.util.UUID;

/*

三个步骤
1. 拿参数
2. 调业务
3. 返回结果
*/

@RestController
@RequestMapping("/SetMeal")
public class SetmealController {
    @Reference
    private SetmealService setmealService;
    @Autowired
    private JedisPool jedisPool;

    @RequestMapping("/find")
    public PageResult findByPage(@RequestBody QueryPageBean queryPageBean) {
        PageResult pageResult = setmealService.findByPage(queryPageBean.getCurrentPage(), queryPageBean.getPageSize(), queryPageBean.getQueryString());
        return pageResult;
    }

    /**
     * 上传图片
     *
     * @param imgFile
     * @return
     */

    @RequestMapping("/upload")
    public Result uploadImage(MultipartFile imgFile) {
        try {
            String originalFilename = imgFile.getOriginalFilename();
            int index = originalFilename.lastIndexOf(".");
            String s = originalFilename.substring(index);
            String fileName = UUID.randomUUID() + s;
            QiniuUtils.upload2Qiniu(imgFile.getBytes(), fileName);
            //将文件名存放到redis中
            jedisPool.getResource().sadd(RedisConstant.SETMEAL_PIC_RESOURCES, fileName);
            return new Result(true, MessageConstant.PIC_UPLOAD_SUCCESS, fileName);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new Result(false, MessageConstant.PIC_UPLOAD_FAIL);
    }

    /**
     * @param setmeal
     * @param travelgroupIds 要和前端的请求参数名保持一致 或者使用@RequestParam注解
     * @return
     */
    @RequestMapping("/add")
    public Result add(@RequestBody Setmeal setmeal, Integer[] travelgroupIds) {
        setmealService.add(setmeal, travelgroupIds);
        try {
            return new Result(true, MessageConstant.ADD_SETMEAL_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.ADD_SETMEAL_FAIL);

        }
    }

    @RequestMapping("/delete")
    public Result delete(Integer id) {
        try {
            setmealService.delete(id);
            return new Result(true, "删除成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "删除失败");
        }
    }

    @RequestMapping("/findById")
    public Result findById(Integer id) {
        try {
            Setmeal setmeal = setmealService.findById(id);
            return new Result(true, "回显成功", setmeal);
        } catch (Exception e) {

            return new Result(false, "会显失败");
        }
    }

    @RequestMapping("/findAll")
    public Result findAll() {
        try {
            List<TravelGroup> list = setmealService.findAll();
            return new Result(true, MessageConstant.QUERY_TRAVELGROUP_SUCCESS, list);
        } catch (Exception e) {
            return new Result(false, MessageConstant.QUERY_TRAVELGROUP_FAIL);
        }
    }

    @RequestMapping("/findTravelGroup")
    public Result findTravelGroup(Integer id) {
        List<Integer> list = setmealService.findTravelGroup(id);
        return new Result(true, "成功", list);
    }

    @RequestMapping("/edit")
    public Result edit(Integer[] travelgroupIds, @RequestBody Setmeal setmeal) {
        try {
            setmealService.edit(travelgroupIds, setmeal);
            return new Result(true, "修改成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "修改成功");
        }
    }

}
