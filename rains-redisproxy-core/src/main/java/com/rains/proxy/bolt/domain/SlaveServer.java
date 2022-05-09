package com.rains.proxy.bolt.domain;

import com.rains.proxy.core.algorithm.impl.support.RedisWeight;

public class SlaveServer implements RedisWeight {
    /**默认权重比例为*/
    private int weight = 1;

    private String url;

    public SlaveServer(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public int getWeight() {
        return this.weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    @Override
    public String toString() {
        return url;
    }
}
