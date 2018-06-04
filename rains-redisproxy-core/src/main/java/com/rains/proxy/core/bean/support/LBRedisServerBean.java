/**
 *
 */
package com.rains.proxy.core.bean.support;


import com.rains.proxy.core.algorithm.impl.support.RedisWeight;
import com.rains.proxy.core.bean.RedisPoolConfig;
import com.rains.proxy.core.constants.RedisConstants;

/**
 * @author liubing
 */
public class LBRedisServerBean implements RedisWeight {

    private String host;//主机名

    private int port;//端口号

    private RedisPoolConfig redisPoolConfig;

    private int weight = 1;//默认权重比例为1


    /**
     *
     */
    public LBRedisServerBean() {
        super();
    }

    /**
     * @return the host
     */
    public String getHost() {
        return host;
    }

    /**
     * @param host the host to set
     */
    public void setHost(String host) {
        this.host = host;
    }

    /**
     * @return the port
     */
    public int getPort() {
        return port;
    }

    /**
     * @param port the port to set
     */
    public void setPort(int port) {
        this.port = port;
    }

    /**
     * 关键key
     *
     * @return
     */
    public String getKey() {
        StringBuffer sbBuffer = new StringBuffer();
        sbBuffer.append(RedisConstants.REDIS_PROXY).append(host).append(RedisConstants.SEPERATOR_ACCESS_LOG).append(port);
        return sbBuffer.toString();
    }

    public String getServerKey() {
        StringBuffer sbBuffer = new StringBuffer();
        sbBuffer.append(host).append(RedisConstants.PROTOCOL_SEPARATOR).append(port);
        return sbBuffer.toString();
    }

    @Override
    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    /**
     * @return the redisPoolConfig
     */
    public RedisPoolConfig getRedisPoolConfig() {
        return redisPoolConfig;
    }

    /**
     * @param redisPoolConfig the redisPoolConfig to set
     */
    public void setRedisPoolConfig(RedisPoolConfig redisPoolConfig) {
        this.redisPoolConfig = redisPoolConfig;
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
