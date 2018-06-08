
package com.rains.proxy.core.config;


import java.io.Serializable;
import java.util.List;

/**
 * @author dourx
 *
 */
public class RedisProxySlave implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2087897700756695413L;
    
	private String host;//主机名
	
	private int port;//端口号
		
	private int weight=1;//默认权重比例为1

	private List<String> name;
	
	private RedisProxyPoolConfig redisProxyPool;

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

	public int getWeight() {
		return weight;
	}

	public void setWeight(int weight) {
		this.weight = weight;
	}

	public RedisProxyPoolConfig getRedisProxyPool() {
		return redisProxyPool;
	}

	public void setRedisProxyPool(RedisProxyPoolConfig redisProxyPool) {
		this.redisProxyPool = redisProxyPool;
	}

	public List<String> getName() {
		return name;
	}

	public void setName(List<String> name) {
		this.name = name;
	}
}
