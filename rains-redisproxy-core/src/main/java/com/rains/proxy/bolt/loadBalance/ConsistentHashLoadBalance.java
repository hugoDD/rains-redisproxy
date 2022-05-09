package com.rains.proxy.bolt.loadBalance;

import com.rains.proxy.bolt.domain.yaml.RedisMaster;
import com.rains.proxy.core.algorithm.impl.GuavaConsistentHash;
import com.rains.proxy.core.utils.StringUtils;
import org.springframework.util.Assert;

import java.util.List;

/**
 * @author ly-dourx
 */
public class ConsistentHashLoadBalance implements ILoadBalance<RedisMaster,String>{


    @Override
    public RedisMaster select(List<RedisMaster> list,String key) {
        Assert.notNull(list,"redis server must not null");
        if(StringUtils.isEmpty(key)){
            return list.get(0);
        }
        GuavaConsistentHash<RedisMaster> hash = new GuavaConsistentHash<>(list);
        return hash.getNodeByKey(key);
    }
}
