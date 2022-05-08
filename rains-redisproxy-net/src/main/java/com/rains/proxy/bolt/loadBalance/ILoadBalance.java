package com.rains.proxy.bolt.loadBalance;

import com.rains.proxy.core.bean.RedisServerMasterCluster;
import com.rains.proxy.core.bean.support.RedisServerBean;
import com.rains.proxy.core.cluster.impl.support.RedisQuestBean;

import java.util.List;

public interface ILoadBalance<T,K> {

    /**
     * 选取策略
     * @return
     */
    public T select(List<T> list,K key);
}
