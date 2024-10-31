package com.wjf.forumsystem.service.impl;

import com.wjf.forumsystem.entity.Permission;
import com.wjf.forumsystem.entity.Posts;
import com.wjf.forumsystem.entity.Result;
import com.wjf.forumsystem.entity.User;
import com.wjf.forumsystem.exception.PasswordMatchException;
import com.wjf.forumsystem.mapper.PermissionMapper;
import com.wjf.forumsystem.mapper.UserMapper;
import com.wjf.forumsystem.service.UserService;
import com.wjf.forumsystem.utils.BytesToHex;
import com.wjf.forumsystem.utils.JwtUtils;
import com.wjf.forumsystem.utils.ThreadLocalUtils;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.transaction.annotation.Transactional;
import redis.clients.jedis.GeoCoordinate;
import redis.clients.jedis.Jedis;
@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private PermissionMapper permissionMapper;

    private Jedis jedis;
    public UserServiceImpl() {
        this.jedis = new Jedis("localhost", 6379);
    }
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
    // 获取用户权限
    @Override
    public List<Permission> getPermission() {
        // 获取用户角色id
        Map<String,Object> claims=ThreadLocalUtils.get();
        Integer userId= (Integer) claims.get("userId");
        User user=userMapper.findByUserId(userId);
        Integer roleId=user.getRoleId();
        //根据用户角色id获取权限
        List<Permission> list = permissionMapper.getUserPermission(roleId);
        return list;
    }

    @Override
    public void adminRe(String username, String password, String email) {
        String hashPassword=BytesToHex.getHash(password);
        userMapper.addAdmin(username,hashPassword,email);
    }

    @Override
    public User userById(int userId) {
        User user = userMapper.findByUserId(userId);
        User user1 = new User();
        user1.setUserId(user.getUserId());
        user1.setUsername(user.getUsername());
        user1.setEmail(user.getEmail());
        user1.setRegistrationDate(user.getRegistrationDate());
        user1.setLastLoginDate(user.getLastLoginDate());
        user1.setProfilePicture(user.getProfilePicture());
        user1.setCoverPhoto(user.getCoverPhoto());
        user1.setAboutMe(user.getAboutMe());
        user1.setRoleId(user.getRoleId());
        user.setNickName(user.getNickName());
        return user1;
    }
    // 修改地理位置服务
    @Override
    @Transactional
    public String updateLd(double longitude, double latitude) {
        // 获取用户角色id
        Map<String,Object> claims=ThreadLocalUtils.get();
        Integer userId= (Integer) claims.get("userId");
        userMapper.updateLD(longitude,latitude,userId);
        jedis.geoadd("user_locations", longitude, latitude, String.valueOf(userId));
        String city = convertLatLngToCity(latitude, longitude);
        userMapper.updateLocation(city,userId);
        if (city!= null) {
            close();
            // 修改用户现在地
            return city;
        } else {
            close();
            return  "无法确定城市名称";

        }

    }




    //注册
    @Override
    public void register(String username, String password) {
        String hashPassword=BytesToHex.getHash(password);

        userMapper.add(username,hashPassword);
    }

    public void close() {
        jedis.close();
    }

    // 百度地图API
    public static String convertLatLngToCity(double latitude, double longitude) {
        try {
            // 百度地图地理编码 API URL
            String apiUrl = "http://api.map.baidu.com/reverse_geocoding/v3/?ak=8nPp8YtxciE8DRUqr6B0cHg9DTLvvW74&output=json&coordtype=wgs84ll&location=" + latitude + "," + longitude;
            HttpClient httpClient = HttpClients.createDefault();
            HttpGet httpGet = new HttpGet(apiUrl);
            HttpResponse response = httpClient.execute(httpGet);
            HttpEntity entity = response.getEntity();
            String responseString = EntityUtils.toString(entity);
            if (responseString!= null && responseString.startsWith("{")) {
                JSONObject jsonObject = new JSONObject(responseString);
                if (jsonObject.has("result")) {
                    JSONObject resultObject = jsonObject.getJSONObject("result");
                    if (resultObject.has("addressComponent")) {
                        JSONObject addressComponentObject = resultObject.getJSONObject("addressComponent");
                        if (addressComponentObject.has("province")) {
                            return addressComponentObject.getString("province");
                        }
                    }
                }
            } else {
                System.err.println("无效的百度地图 API 响应：" + responseString);
            }
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
