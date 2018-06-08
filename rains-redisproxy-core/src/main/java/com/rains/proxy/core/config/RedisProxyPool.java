
package com.rains.proxy.core.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.io.Serializable;

/**
 * @author dourx
 * 连接池配置
 */

public class RedisProxyPool implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7393266010156039L;
	
	private int timeout;//超时时间
	
	private int maxActiveConnection;//最大活动连接数
	
	private int maxIdleConnection;//最大 空闲连接数
	
	private int minConnection;//最小连接数
	
	private int initialConnection = 0;//初始化值
	
	private long maxWaitMillisOnBorrow; // 最大等待时间
	
	private long timeBetweenEvictionRunsMillis;//回收间隔时间点
	
	private long minEvictableIdleTimeMillis;//池中最小生存的时间
	
	private int minIdleEntries = 0;//最小等待时间
	
	private boolean testOnBorrow=false;//取出验证
	
	private int connectionTimeout;//连接超时
	
	private boolean testOnReturn=false;//回收时验证
	
	private boolean testWhileIdle=false;//回收空闲验证


    private String host="127.0.0.1";
    private int port=6379;

	/**
	 * @return the timeout
	 */
	public int getTimeout() {
		return timeout;
	}

	/**
	 * @param timeout the timeout to set
	 */
	public void setTimeout(int timeout) {
		this.timeout = timeout;
	}

	/**
	 * @return the maxActiveConnection
	 */
	public int getMaxActiveConnection() {
		return maxActiveConnection;
	}

	/**
	 * @param maxActiveConnection the maxActiveConnection to set
	 */
	public void setMaxActiveConnection(int maxActiveConnection) {
		this.maxActiveConnection = maxActiveConnection;
	}

	/**
	 * @return the maxIdleConnection
	 */
	public int getMaxIdleConnection() {
		return maxIdleConnection;
	}

	/**
	 * @param maxIdleConnection the maxIdleConnection to set
	 */
	public void setMaxIdleConnection(int maxIdleConnection) {
		this.maxIdleConnection = maxIdleConnection;
	}

	/**
	 * @return the minConnection
	 */
	public int getMinConnection() {
		return minConnection;
	}

	/**
	 * @param minConnection the minConnection to set
	 */
	public void setMinConnection(int minConnection) {
		this.minConnection = minConnection;
	}

	/**
	 * @return the initialConnection
	 */
	public int getInitialConnection() {
		return initialConnection;
	}

	/**
	 * @param initialConnection the initialConnection to set
	 */
	public void setInitialConnection(int initialConnection) {
		this.initialConnection = initialConnection;
	}

	/**
	 * @return the maxWaitMillisOnBorrow
	 */
	public long getMaxWaitMillisOnBorrow() {
		return maxWaitMillisOnBorrow;
	}

	/**
	 * @param maxWaitMillisOnBorrow the maxWaitMillisOnBorrow to set
	 */
	public void setMaxWaitMillisOnBorrow(long maxWaitMillisOnBorrow) {
		this.maxWaitMillisOnBorrow = maxWaitMillisOnBorrow;
	}

	/**
	 * @return the timeBetweenEvictionRunsMillis
	 */
	public long getTimeBetweenEvictionRunsMillis() {
		return timeBetweenEvictionRunsMillis;
	}

	/**
	 * @param timeBetweenEvictionRunsMillis the timeBetweenEvictionRunsMillis to set
	 */
	public void setTimeBetweenEvictionRunsMillis(long timeBetweenEvictionRunsMillis) {
		this.timeBetweenEvictionRunsMillis = timeBetweenEvictionRunsMillis;
	}

	/**
	 * @return the minEvictableIdleTimeMillis
	 */
	public long getMinEvictableIdleTimeMillis() {
		return minEvictableIdleTimeMillis;
	}

	/**
	 * @param minEvictableIdleTimeMillis the minEvictableIdleTimeMillis to set
	 */
	public void setMinEvictableIdleTimeMillis(long minEvictableIdleTimeMillis) {
		this.minEvictableIdleTimeMillis = minEvictableIdleTimeMillis;
	}

	/**
	 * @return the minIdleEntries
	 */
	public int getMinIdleEntries() {
		return minIdleEntries;
	}

	/**
	 * @param minIdleEntries the minIdleEntries to set
	 */
	public void setMinIdleEntries(int minIdleEntries) {
		this.minIdleEntries = minIdleEntries;
	}

	/**
	 * @return the testOnBorrow
	 */
	public boolean isTestOnBorrow() {
		return testOnBorrow;
	}

	/**
	 * @param testOnBorrow the testOnBorrow to set
	 */
	public void setTestOnBorrow(boolean testOnBorrow) {
		this.testOnBorrow = testOnBorrow;
	}

	/**
	 * @return the connectionTimeout
	 */
	public int getConnectionTimeout() {
		return connectionTimeout;
	}

	/**
	 * @param connectionTimeout the connectionTimeout to set
	 */
	public void setConnectionTimeout(int connectionTimeout) {
		this.connectionTimeout = connectionTimeout;
	}

	/**
	 * @return the testOnReturn
	 */
	public boolean isTestOnReturn() {
		return testOnReturn;
	}

	/**
	 * @param testOnReturn the testOnReturn to set
	 */
	public void setTestOnReturn(boolean testOnReturn) {
		this.testOnReturn = testOnReturn;
	}

	/**
	 * @return the testWhileIdle
	 */
	public boolean isTestWhileIdle() {
		return testWhileIdle;
	}

	/**
	 * @param testWhileIdle the testWhileIdle to set
	 */
	public void setTestWhileIdle(boolean testWhileIdle) {
		this.testWhileIdle = testWhileIdle;
	}

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }
}
