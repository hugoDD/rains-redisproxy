package com.rains.proxy.bolt.domain.yaml;

import org.springframework.util.Assert;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author ly-dourx
 */
public class RedisMaster extends RedisHost {

    List<RedisHost> redisSlaves;


    public List<RedisHost> getRedisSlaves() {
        return redisSlaves;
    }

    public void setRedisSlaves(List<RedisHost> redisSlaves) {
        this.redisSlaves = redisSlaves;
    }

    public List<String> getSlaves(){
        Assert.notEmpty(redisSlaves,"redis slaves must not null");
        List<String> hosts = redisSlaves.stream().map(RedisHost::getUrl).collect(Collectors.toList());
        return hosts;
    }
}
