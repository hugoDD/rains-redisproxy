package com.rains.proxy.core.cluster.impl;

import com.rains.proxy.core.algorithm.impl.GuavaConsistentHash;
import com.rains.proxy.core.bean.support.RedisServerBean;
import com.rains.proxy.core.cluster.impl.support.RedisQuestBean;

import java.util.List;

/**
 * @author ly-dourx
 */
public class GuavaConsistentHashLoadBalance extends AbstractLoadBalance{
    @Override
    protected RedisServerBean doSelect(RedisQuestBean redisQuestBean, List<RedisServerBean> ffanRedisMasterServers) {
        GuavaConsistentHash<RedisServerBean> hash = new GuavaConsistentHash<>(ffanRedisMasterServers);
        return hash.getNodeByKey(new String(redisQuestBean.getKey()));
    }
}
