package com.rains.proxy.bolt.domain.yaml;

import java.util.List;

/**
 * @author ly-dourx
 */
public class Node {
    private List<RedisMaster> redisMasters;

    public List<RedisMaster> getRedisMasters() {
        return redisMasters;
    }

    public void setRedisMasters(List<RedisMaster> redisMasters) {
        this.redisMasters = redisMasters;
    }
}
