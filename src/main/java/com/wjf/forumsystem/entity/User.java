package com.wjf.forumsystem.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

import java.sql.Timestamp;
@Data
public class User {
    @NotNull
    private int userId;
    private String username;
    @NotEmpty
    @Pattern(regexp = "^\\S{1,10}$")
    private String nickName;
    @JsonIgnore //转换为json格式时忽略属性
    private String password;
    @NotEmpty
    @Email
    private String email;
    private Timestamp registrationDate;
    private Timestamp lastLoginDate;
    private Timestamp updateTime;
    private String profilePicture;
    private String coverPhoto;
    private String aboutMe;
    private String location;
    private String website;
    private int roleId;
}
