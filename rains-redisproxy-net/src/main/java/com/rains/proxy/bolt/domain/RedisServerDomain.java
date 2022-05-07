package com.rains.proxy.bolt.domain;

import java.util.List;

/**
 * @author ly-dourx
 */
public class RedisServerDomain {
    private String master;
    private List<String> slave;

    public String getMaster() {
        return master;
    }

    public void setMaster(String master) {
        this.master = master;
    }

    public List<String> getSlave() {
        return slave;
    }

    public void setSlave(List<String> slave) {
        this.slave = slave;
    }
}
