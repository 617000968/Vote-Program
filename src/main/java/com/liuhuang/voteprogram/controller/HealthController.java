package com.liuhuang.voteprogram.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/system")
public class HealthController {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    // 生产环境需要添加权限控制注解
    @GetMapping("/cache/clear")
    public String clearCache() {
        try (RedisConnection connection = redisTemplate.getConnectionFactory().getConnection()) {
            connection.serverCommands().flushDb();
            return "缓存已清空";
        }
    }
}