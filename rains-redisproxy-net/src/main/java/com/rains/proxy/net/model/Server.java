package com.rains.proxy.net.model;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * create by lorne on 2017/9/21
 */
@ConfigurationProperties(prefix = "redisproxy.netty", ignoreUnknownFields = true)
public class Server {

    private String ip;
    private int port;


    private String tag;
    private int nowCount;
    private int maxCount;
    private int heartTime;



    private String serviceId;

    public int getHeartTime() {
        return heartTime;
    }

    public void setHeartTime(int heartTime) {
        this.heartTime = heartTime;
    }




    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public int getNowCount() {
        return nowCount;
    }

    public void setNowCount(int nowCount) {
        this.nowCount = nowCount;
    }

    public int getMaxCount() {
        return maxCount;
    }

    public void setMaxCount(int maxCount) {
        this.maxCount = maxCount;
    }

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }
}
