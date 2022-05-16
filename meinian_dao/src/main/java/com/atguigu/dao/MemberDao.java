package com.atguigu.dao;

import com.atguigu.pojo.Member;

public interface MemberDao {
    Member getMember(String telephone);

    void add(Member member);

    Integer findMemberCountByMonth(String month);

    int getTodayNewMember(String date);

    int getTotalMember();

    int getThisWeekAndMonthNewMember(String date);


}
