package com.devscircle.journalApp.Service;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

@SpringBootTest
public class RedisTests {
    
    @Autowired
    private RedisTemplate redisTemplate;
    
    @Test
    @Disabled
    public void redisTest(){
        redisTemplate.opsForValue().set("email","email@gmail.com");

        Object email = redisTemplate.opsForValue().get("email");
        int a=1;
    }
}
