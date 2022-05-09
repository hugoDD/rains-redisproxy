package com.rains.proxy.bolt.domain;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.rains.proxy.bolt.domain.yaml.RedisMaster;
import com.rains.proxy.bolt.domain.yaml.RedisProxy;
import com.rains.proxy.bolt.loadBalance.ConsistentHashLoadBalance;
import com.rains.proxy.bolt.loadBalance.ILoadBalance;
import com.rains.proxy.bolt.loadBalance.RoundRobinLoadBalance;
import com.rains.proxy.core.command.IRedisCommand;
import com.rains.proxy.core.command.impl.RedisRequestPolicy;
import com.rains.proxy.core.utils.StringUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * @author ly-dourx
 */
public class ClusterDomain {
    private List<RedisMaster> redisServers;

    private ILoadBalance<RedisMaster,String> masterLoadBalance;
    private ILoadBalance<String,String> slaveLoadBalance;

    public ClusterDomain() {
        initYaml();
        masterLoadBalance = new ConsistentHashLoadBalance();
        slaveLoadBalance = new RoundRobinLoadBalance();
    }

    protected List<RedisMaster> initYaml(){
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream("redisproxy.yaml");
        try {
            RedisProxy redisProxy = mapper.readValue(inputStream, RedisProxy.class);
            if(redisProxy.getNodes()==null){
                throw new RuntimeException("not redis host ");
            }
           this.redisServers = redisProxy.getNodes().getRedisMasters();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String select(IRedisCommand redisCommand){
        RedisRequestPolicy policy = redisCommand.getPolicy();
        if(policy.isNotThrough()){
            return "";
        }
        String key = redisCommand.getKey();

        RedisMaster master = masterLoadBalance.select(redisServers, key);
        if(StringUtils.isEmpty(key)){
            return master.getUrl();
        }
        if(policy.isRead()){
           return slaveLoadBalance.select(master.getSlaves(),key);
        }
        return master.getUrl();
    }

}
