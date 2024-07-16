package com.zgj.springdataredisdemo;

import com.zgj.springdataredisdemo.pojo.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;


@SpringBootTest
class SpringDataRedisDemoApplicationTests {


    @Autowired
    private RedisTemplate<String,Object> redisTemplate;

    @Test
    void testString() {
        //1.存入数据
        redisTemplate.opsForValue().set("name","guojunzhang");
        //2.取出数据
        Object name = redisTemplate.opsForValue().get("name");
        System.out.println("name = " + name);

    }

    @Test
    void testSavaUser(){
        //1.写入数据
        redisTemplate.opsForValue().set("user:100",new User("骏哥",21));
        //2.获取数据
        User o = (User) redisTemplate.opsForValue().get("user:100");
        System.out.println(o);

    }

}
