package com.demo.redis.service.impl;

import com.demo.redis.entity.User;
import com.demo.redis.service.UserService;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;


import java.util.Map;

@Service
@Slf4j
public class UserServiceImpl implements UserService {
    //Mocking DB
    private static final Map<Long, User> DB = Maps.newConcurrentMap();
    //Initialize
    static{
        DB.put(1L, new User(1L, "Jack"));
        DB.put(2L, new User(2L, "Bill"));
        DB.put(3L, new User(3L, "Mary"));
    }

    @CachePut(value = "user", key = "#user.id")
    @Override
    public User saveOrUpdate(User user){
        DB.put(user.getId(), user);
        log.info("保存用户:" +  user);
        return user;
    }


    @Cacheable(value = "user", key = "#id")
    @Override
    public User get(Long id) {
        log.info("查询用户id=" + id);
        return DB.get(id);
    }

    @CacheEvict(value = "user", key = "#id")
    @Override
    public void delete(Long id) {
        log.info("删除用户id="+ id);
        DB.remove(id);
    }
}
