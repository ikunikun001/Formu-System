package com.wjf.forumsystem;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.wjf.forumsystem.mapper")
public class ForumSystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(ForumSystemApplication.class, args);
    }

}
