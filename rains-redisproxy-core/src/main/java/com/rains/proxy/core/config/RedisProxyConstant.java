/**
 * 
 */
package com.rains.proxy.core.config;

/**
 * @author dourx
 *
 */
public class RedisProxyConstant {
	
	public static final String HOST = "host";
	
	public static final String PORT="port";
	
	public static final String CONNECTIONTIMEOUT="connectionTimeout";
	
	public static final String MAXACTIVECONNECTION="maxActiveConnection";
	
	public static final String MAXIDLECONNECTION="maxIdleConnection";
	
	public static final String MINCONNECTION="minConnection";
	
	public static final String INITIALCONNECTION="initialConnection";
	public static final String MAXWAITMILLISONBORROW="maxWaitMillisOnBorrow";
	
	public static final String TIMEBETWEENEVICTIONRUNSMILLIS="timeBetweenEvictionRunsMillis";
	
	public static final String MINEVICTABLEIDLETIMEMILLIS="minEvictableIdleTimeMillis";
	public static final String MINIDLEENTRIES="minIdleEntries";
	public static final String TESTONBORROW="testOnBorrow";
	public static final String TESTONRETURN="testOnReturn";
	public static final String TESTWHILEIDLE="testWhileIdle";
	
	public static final String WEIGHT="weight";
	
	public static final String REDISPROXYCLUSTERS="redisProxyClusters";
	
	public static final String REDISPROXYHOST="redisProxyHost";//nredis-proxy 主机名
	
	public static final String REDISPROXYPORT="redisProxyPort";//nredis-proxy 端口号
	
	public static final String REDISPROXYSLAVE="redisProxySlave";
	
	public static final String REDISPROXYMASTER="redisProxyMaster";
	
	public static final String REDISPROXYMASTERS="redisProxyMasters";
	
	public static final String REDISPROXYNODE="redisProxyNode";
	
	public static final String ALGORITHMREF="algorithm-ref";
	
	public static final String CONFIGREF="config-ref";
	
	public static final String LOADCLUSTERBALANCE="loadClusterBalance";
	
	public static final String LOADMASTERRBALANCE="loadMasterBalance";
	
	public static final String REDISPOOLCONFIG="redisPoolConfig";
}
