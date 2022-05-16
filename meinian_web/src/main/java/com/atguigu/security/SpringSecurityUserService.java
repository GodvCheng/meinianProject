package com.atguigu.security;

import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.pojo.Permission;
import com.atguigu.pojo.Role;
import com.atguigu.service.UserService;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Component
public class SpringSecurityUserService implements UserDetailsService {
    @Reference
    private UserService userService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        com.atguigu.pojo.User user = userService.getUserByUserName(username);
        if (user == null) {
            return null;
        }
        List<GrantedAuthority> list = new ArrayList<>();
        // Set<GrantedAuthority> authorities = new HashSet<>();
        Set<Role> roles = user.getRoles();
        for (Role role : roles) {
            Set<Permission> permissions = role.getPermissions();
            for (Permission permission : permissions) {
                list.add(new SimpleGrantedAuthority(permission.getKeyword()));
            }
        }

        UserDetails securityUser = new User(username, user.getPassword(), list);
        return securityUser;
    }
}
