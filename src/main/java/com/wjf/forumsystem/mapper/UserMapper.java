package com.wjf.forumsystem.mapper;

import com.wjf.forumsystem.entity.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Component;

@Mapper
public interface UserMapper {
    //查询用户名是否被占用接口
    @Select("select * from users where username=#{username}")
    User findByUsername(String username);
    //查询邮箱
    @Select("select * from users where email=#{email}")
    User findByEmail(String email);
    // 添加用户
    @Insert("INSERT INTO users (username, password, registration_date, email, role_id) " +
            "VALUES (#{username}, #{password}, now(), #{email}, 2)")
    void add(String username, String password, String email);

    @Update("update users set email=#{email},nick_name=#{nickName},update_time=now() where user_id=#{userId}")
    void update(User user);
    @Update("update users set profile_picture=#{avatarUrl},update_time=now() where user_id=#{userId}")
    void updateAvatar(String avatarUrl, Integer userId);
    @Update("update users set password=#{newpassword},update_time=now() where user_id=#{userId}")
    void updatePwd(String newpassword, Integer userId);
    @Select("select password from users where user_id=#{userId}")
    String findPwd(Integer userId);
}
