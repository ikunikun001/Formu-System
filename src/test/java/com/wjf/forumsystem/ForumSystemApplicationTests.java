package com.wjf.forumsystem;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class ForumSystemApplicationTests {

    @Test
    void contextLoads() {

    }
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    public void testDatabaseConnection() {
        // 测试数据库连接是否成功
        int result = jdbcTemplate.queryForObject("SELECT 1", Integer.class);
        assertTrue(result == 1, "数据库连接测试失败");
    }
}
