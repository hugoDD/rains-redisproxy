package com.rains.proxy.bolt.domain;

import java.util.List;

public class RedisProxyConfiguration {

    private Nodes nodes;

    public Nodes getNodes() {
        return nodes;
    }

    public void setNodes(Nodes nodes) {
        this.nodes = nodes;
    }

    private class Nodes {
        List<RedisMaster> redisMasters;

        public List<RedisMaster> getRedisMasters() {
            return redisMasters;
        }

        public void setRedisMasters(List<RedisMaster> redisMasters) {
            this.redisMasters = redisMasters;
        }
    }

    private class  RedisMaster extends RedisHost{

        List<RedisHost> redisSlaves;



        public List<RedisHost> getRedisSlaves() {
            return redisSlaves;
        }

        public void setRedisSlaves(List<RedisHost> redisSlaves) {
            this.redisSlaves = redisSlaves;
        }
    }

    private class RedisHost{
       private String host;
       private String port;
       private String password;

        public String getHost() {
            return host;
        }

        public void setHost(String host) {
            this.host = host;
        }

        public String getPort() {
            return port;
        }

        public void setPort(String port) {
            this.port = port;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public String getUrl(){
           return host+":"+port;
       }
    }
}
