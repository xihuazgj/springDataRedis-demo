package com.zgj.springdataredisdemo;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zgj.springdataredisdemo.pojo.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.Map;


@SpringBootTest
class RedisStringTests {


    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    private static final ObjectMapper mapper = new ObjectMapper();

    @Test
    void testString() {
        //1.存入数据
        stringRedisTemplate.opsForValue().set("name","guojunzhang");
        //2.取出数据
        Object name = stringRedisTemplate.opsForValue().get("name");
        System.out.println("name = " + name);

    }

    @Test
    void testSavaUser() throws JsonProcessingException {
        //创建对象
        User user = new User("骏哥",21);
        //手动序列化
        String json = mapper.writeValueAsString(user);
        //1.写入数据
        stringRedisTemplate.opsForValue().set("user:200",json);
        //2.获取数据
        String jsonUser = stringRedisTemplate.opsForValue().get("user:200");
        //手动反序列化
        User user1 = mapper.readValue(jsonUser,User.class);
        System.out.println(user1);

    }

    @Test
    void testHash(){
        stringRedisTemplate.opsForHash().put("user:400","name","章若楠");
        stringRedisTemplate.opsForHash().put("user:400","beauty","第一");

        Map<Object, Object> entries = stringRedisTemplate.opsForHash().entries("user:400");
        System.out.println(entries);

    }

}
