package com.wjf.forumsystem.controller;

import com.wjf.forumsystem.entity.Result;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/article")
public class ArticleController {
    @GetMapping("/list")
    public Result<String> getList(){
        return Result.success("数据列表。。。。");
    }
}
