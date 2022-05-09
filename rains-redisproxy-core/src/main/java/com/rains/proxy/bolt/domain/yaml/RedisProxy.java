package com.rains.proxy.bolt.domain.yaml;

/**
 * @author ly-dourx
 */
public class RedisProxy {
    private String name;
    private Node nodes;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Node getNodes() {
        return nodes;
    }

    public void setNodes(Node nodes) {
        this.nodes = nodes;
    }
}
