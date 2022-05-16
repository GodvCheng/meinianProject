package com.atguigu.dao;

import com.atguigu.pojo.Role;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface RoleDao {
    Set<Role> findRolesByUserId(Integer id);
}
