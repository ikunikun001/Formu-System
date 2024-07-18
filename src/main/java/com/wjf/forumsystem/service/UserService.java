package com.wjf.forumsystem.service;

import com.wjf.forumsystem.entity.User;
import org.springframework.stereotype.Service;


public interface UserService {
    // 查询用户名是否被占用
     User findByUsername(String username);

     //注册操作
    void register(String username,String password,String email);

    User findByEmail(String email);

    void update(User user);


    void updateAvatat(String avatarUrl);

    void updatePwd(String oldpassword, String newpassword);
}
