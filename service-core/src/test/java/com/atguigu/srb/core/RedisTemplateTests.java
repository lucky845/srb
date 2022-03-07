package com.atguigu.srb.core;

import com.atguigu.srb.core.mapper.DictMapper;
import com.atguigu.srb.core.pojo.entity.Dict;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.concurrent.TimeUnit;

/**
 * Redis存值测试
 *
 * @author lucky845
 * @date 2022年03月04日 20:42
 */
@SpringBootTest
public class RedisTemplateTests {

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private DictMapper dictMapper;

    @Test
    public void saveData() {
        Dict dict = dictMapper.selectById(1);
        // 向数据库中存储string类型的键值对, 过期时间5分钟
        redisTemplate.opsForValue().set("dict", dict, 5, TimeUnit.MINUTES);
    }

    @Test
    public void getDict(){
        Dict dict = (Dict) redisTemplate.opsForValue().get("dict");
        System.out.println("dict = " + dict);
    }

}
