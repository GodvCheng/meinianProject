package com.atguigu.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.atguigu.constant.MessageConstant;
import com.atguigu.dao.MemberDao;
import com.atguigu.dao.OrderDao;
import com.atguigu.dao.OrderSettingDao;
import com.atguigu.entity.Result;
import com.atguigu.pojo.Member;
import com.atguigu.pojo.Order;
import com.atguigu.pojo.OrderSetting;
import com.atguigu.service.OrderService;
import com.atguigu.util.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Transactional
@Service(interfaceClass = OrderService.class)
public class OrderServiceImpl implements OrderService {
    @Autowired
    private OrderDao orderDao;
    @Autowired
    private OrderSettingDao orderSettingDao;

    @Autowired
    private MemberDao memberDao;

    @Override
    public Result saveOrder(Map map) throws Exception {
        /*
         1. 判断当前的日期是否可以预约(根据orderDate查询t_ordersetting, 能查询出来可以预约;查询不出来,不能预约)
         2. 判断当前日期是否预约已满(判断reservations（已经预约人数）是否等于number（最多预约人数）)
         3. 判断是否是会员(根据手机号码查询t_member)
         - 如果是会员(能够查询出来), 防止重复预约(根据member_id,orderDate,setmeal_id查询t_order)
         - 如果不是会员(不能够查询出来) ,自动注册为会员(直接向t_member插入一条记录)
         4.进行预约
         - 向t_order表插入一条记录
         - t_ordersetting表里面预约的人数reservations+1
         */
        String orderDateStr = (String) map.get("orderDate");
        Date orderDate = DateUtils.parseString2Date(orderDateStr);
        String telephone = (String) map.get("telephone");
        String name = (String) map.get("name");
        String sex = (String) map.get("sex");
        String idCard = (String) map.get("idCard");
        Integer setmealId = Integer.parseInt((String) map.get("setmealId"));
        OrderSetting orderSetting = orderSettingDao.getOrderSettingCount(orderDate);
        if (orderSetting == null) { //日期不存在，不能预约
            return new Result(false, MessageConstant.SELECTED_DATE_CANNOT_ORDER);
        } else {//检查预约是否已满
            int number = orderSetting.getNumber();
            int reservations = orderSetting.getReservations();
            if (reservations >= number) {//预约人数已满
                return new Result(false, MessageConstant.ORDER_FULL);
            }
        }
        //预约人数未满,判断预约人是否为会员，如果不是，则根据表单信息进行快捷注册
        Member member = memberDao.getMember(telephone);//根据手机号查询该预订人员是否存在
        if (member == null) {//会员不存在
            member = new Member();
            member.setName(name);
            member.setIdCard(idCard);
            member.setSex(sex);
            member.setPhoneNumber(telephone);
            member.setRegTime(new Date());
            memberDao.add(member);//主键回填
        } else {//会员存在，查询是否已经预约，防止重复预约,(根据member_id,orderDate,setmeal_id查询t_order)
            Order order = new Order();
            order.setSetmealId(setmealId);
            order.setOrderDate(orderDate);
            order.setMemberId(member.getId());
            List<Order> orderList = orderDao.getRepeatOrder(order);
            if (orderList != null && orderList.size() > 0) {//已经预约
                return new Result(false, MessageConstant.HAS_ORDERED);
            }
        }
        //未预约
        Order order = new Order();
        Integer memberId = member.getId();
        order.setMemberId(memberId);
        order.setOrderType("微信预约");
        order.setOrderStatus("未出游");
        order.setOrderDate(orderDate);
        order.setSetmealId(setmealId);
        orderDao.add(order);
        orderSetting.setReservations(orderSetting.getReservations() + 1);
        orderSettingDao.editReservations(orderSetting);
        return new Result(true, MessageConstant.ORDERSETTING_SUCCESS, order);
    }

    @Override
    public Map<String, Object> findById(Integer id) {
        try {
            Map<String, Object> map = orderSettingDao.findById(id);
            Date orderDate = (Date) map.get("orderDate");
            //去除预约日期的时分秒
            map.put("orderDate", DateUtils.parseDate2String(orderDate));
            return map;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
