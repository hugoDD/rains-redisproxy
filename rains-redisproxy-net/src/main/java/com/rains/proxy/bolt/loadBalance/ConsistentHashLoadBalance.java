package com.rains.proxy.bolt.loadBalance;

import com.rains.proxy.core.algorithm.impl.GuavaConsistentHash;
import com.rains.proxy.core.bean.support.RedisServerBean;

import java.util.List;

public class ConsistentHashLoadBalance implements ILoadBalance<String,String>{
    @Override
    public String select(List<String> list,String key) {
        GuavaConsistentHash<String> hash = new GuavaConsistentHash<>(list);
        return hash.getNodeByKey(key);
    }
}
