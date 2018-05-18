/**
 * 
 */
package com.rains.proxy.core.config;


import org.springframework.beans.factory.InitializingBean;

import java.io.Serializable;
import java.util.List;

/**
 * redis proxy节点
 * @author dourx
 *
 */
public class RedisProxyGroupNode implements Serializable {

	private static final int MAXSLOTNUM= 64;


	private List<RedisProxyMaster> redisProxyMasters;
	
	private String redisProxyHost;//主机名
	
	private int redisProxyPort;//端口号
	
	private String algorithmRef;
	
	//private LoadBalance loadMasterBalance;//主的一致性算法


	public static int getMAXSLOTNUM() {
		return MAXSLOTNUM;
	}

	public List<RedisProxyMaster> getRedisProxyMasters() {
		return redisProxyMasters;
	}

	public void setRedisProxyMasters(List<RedisProxyMaster> redisProxyMasters) {
		this.redisProxyMasters = redisProxyMasters;
	}

	public String getRedisProxyHost() {
		return redisProxyHost;
	}

	public void setRedisProxyHost(String redisProxyHost) {
		this.redisProxyHost = redisProxyHost;
	}

	public int getRedisProxyPort() {
		return redisProxyPort;
	}

	public void setRedisProxyPort(int redisProxyPort) {
		this.redisProxyPort = redisProxyPort;
	}

	public String getAlgorithmRef() {
		return algorithmRef;
	}

	public void setAlgorithmRef(String algorithmRef) {
		this.algorithmRef = algorithmRef;
	}
}
