package com.rains.proxy.bolt.domain;

import com.rains.proxy.bolt.loadBalance.ILoadBalance;
import com.rains.proxy.core.cluster.LoadBalance;
import com.rains.proxy.core.command.IRedisCommand;
import com.rains.proxy.core.command.impl.RedisRequestPolicy;

import java.util.List;

public class ClusterDomain {
    private List<RedisServerDomain> redisServers;

    private ILoadBalance<RedisServerDomain,String> masterLoadBalance;
    private ILoadBalance<String,String> slaveLoadBalance;


    public String select(IRedisCommand redisCommand){
        RedisRequestPolicy policy = redisCommand.getPolicy();
        if(policy.isNotThrough()){
            return "";
        }
        String key = redisCommand.getKey();

        RedisServerDomain master = masterLoadBalance.select(redisServers, key);
        if(policy.isRead()){
           return slaveLoadBalance.select(master.getSlave(),key);
        }
        return master.getMaster();
    }

}
