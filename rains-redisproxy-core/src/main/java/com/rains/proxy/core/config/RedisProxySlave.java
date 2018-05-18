
package com.rains.proxy.core.config;


import java.io.Serializable;

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
	
	private RedisProxyPool redisProxyPool;

}
