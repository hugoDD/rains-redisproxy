package com.rains.proxy.bolt.domain.yaml;

/**
 * @author ly-dourx
 */
public class RedisHost {
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
        return host+":"+port+"?_PROTOCOL=0";
    }

    @Override
    public String toString() {
        return host + ":" + port;
    }
}
