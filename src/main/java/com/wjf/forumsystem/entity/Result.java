package com.wjf.forumsystem.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Result<T> {
    private Integer code;
    private String message;
    private T data;

    public static <E> Result<E> success(E data){
        return new Result<>(0,"操作成功",data);
    }
    //无参数返回
    public static Result success(){
        return new Result(0,"操作成功",null);
    }
    //错误返回
    public static Result error(String message){
        return new Result(1,message,null);
    }
}
