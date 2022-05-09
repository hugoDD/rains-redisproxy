package com.rains.proxy.bolt.loadBalance;

import java.util.List;

public interface ILoadBalance<T,K> {

    /**
     * 选取策略
     * @return
     */
    public T select(List<T> list,K key);
}
