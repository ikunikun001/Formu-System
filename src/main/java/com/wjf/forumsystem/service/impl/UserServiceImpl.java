package com.wjf.forumsystem.service.impl;

import com.wjf.forumsystem.entity.Result;
import com.wjf.forumsystem.entity.User;
import com.wjf.forumsystem.exception.PasswordMatchException;
import com.wjf.forumsystem.mapper.UserMapper;
import com.wjf.forumsystem.service.UserService;
import com.wjf.forumsystem.utils.BytesToHex;
import com.wjf.forumsystem.utils.JwtUtils;
import com.wjf.forumsystem.utils.ThreadLocalUtils;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Map;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;
    //查询用户名是否被占用
    @Override
    public User findByUsername(String username) {
        User user=userMapper.findByUsername(username);
        return user;
    }
    //查询邮箱
    public User findByEmail(String email){
        User user=userMapper.findByEmail(email);
        return user;
    }

    @Override
    public void update(User user) {

        userMapper.update(user);
    }

    // 修改头像
    @Override
    public void updateAvatat(String avatarUrl) {
        Map<String,Object> claims=ThreadLocalUtils.get();
        Integer userId= (Integer) claims.get("userId");
        userMapper.updateAvatar(avatarUrl,userId);
    }
    // 修改密码
    @Override
    public void updatePwd(String oldpassword, String newpassword) {
        Map<String,Object> claims=ThreadLocalUtils.get();
        Integer userId= (Integer) claims.get("userId");
        // 查询旧密码
        String password=userMapper.findPwd(userId);
        // 加密用户输入原密码
        String hashPassword=BytesToHex.getHash(oldpassword);
        //密码错误
        if(!password.equals(hashPassword)){
           throw new PasswordMatchException("密码错误！");
        }
        // 原密码正确,加密输入的新密码，然后修改
        newpassword=BytesToHex.getHash(newpassword);
        userMapper.updatePwd(newpassword,userId);
    }

    //注册
    @Override
    public void register(String username, String password, String email) {
        String hashPassword=BytesToHex.getHash(password);

        userMapper.add(username,hashPassword,email);
    }

}
