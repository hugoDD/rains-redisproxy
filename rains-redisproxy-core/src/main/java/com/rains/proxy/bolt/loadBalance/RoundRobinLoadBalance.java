package com.rains.proxy.bolt.loadBalance;

import com.rains.proxy.bolt.domain.SlaveServer;
import com.rains.proxy.core.algorithm.impl.RoundRobinHash;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author ly-dourx
 */
public class RoundRobinLoadBalance implements ILoadBalance<String,String>{
    @Override
    public String select(List<String> list, String key) {
        List<SlaveServer> hostList = list.stream().map(SlaveServer::new).collect(Collectors.toList());
        RoundRobinHash<SlaveServer> roundRobinHash=new RoundRobinHash<>(hostList);
        SlaveServer result=roundRobinHash.weightRandom();
        return result.getUrl();

    }
}
