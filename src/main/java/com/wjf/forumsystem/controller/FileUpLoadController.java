package com.wjf.forumsystem.controller;

import com.wjf.forumsystem.entity.Result;
import com.wjf.forumsystem.utils.AliyunOSSUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.UUID;

@RestController
@RequestMapping("/file")
public class FileUpLoadController {
    @PostMapping("/upload")
    public Result<String> upload(MultipartFile file) throws Exception {
        String originalFilename = file.getOriginalFilename();
        String filename= UUID.randomUUID().toString()+originalFilename.substring(originalFilename.lastIndexOf("."));
        /*// 把文件放入本地
        file.transferTo(new File("C:\\Users\\吴建飞\\OneDrive\\Desktop\\img\\"+filename));*/
        // 把文件放入云
        String url=AliyunOSSUtils.upload(filename, file.getInputStream());
        return Result.success(url);
    }
}
