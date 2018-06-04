/**
 * 
 */
package com.rains.proxy.core.pool.util;


import com.rains.proxy.core.connection.IConnection;
import com.rains.proxy.core.pool.LBRedisProxyIdleEntriesQueue;
import com.rains.proxy.core.pool.LBRedisProxyPoolObjectEntryFactory;
import com.rains.proxy.core.pool.LBRedisProxyPooledObjectFactory;
import com.rains.proxy.core.pool.commons.LBRedisProxyPoolConfig;
import com.rains.proxy.core.pool.impl.LBRedisProxyBasicPool;
import com.rains.proxy.core.pool.impl.LBRedisProxyPoolBasicIdleEntriesQueue;
import com.rains.proxy.core.pool.impl.LBRedisProxyPoolBasicObjectEntryFactory;

/**
 * @author liubing
 *
 */
public class LBRedisProxyChannelPoolUtils {
	
	public static LBRedisProxyBasicPool<IConnection> createPool(LBRedisProxyPoolConfig config, LBRedisProxyPooledObjectFactory<IConnection> factory) throws Exception {
		return createPool( config, createQueue(config),createPoolEntryFactory(factory));
	}
	
	private static LBRedisProxyPoolObjectEntryFactory<IConnection> createPoolEntryFactory(LBRedisProxyPooledObjectFactory<IConnection> objectFactory) {

		return new LBRedisProxyPoolBasicObjectEntryFactory<IConnection>(objectFactory);
	}
	
	private static LBRedisProxyIdleEntriesQueue<IConnection> createQueue(LBRedisProxyPoolConfig config) {
		return new LBRedisProxyPoolBasicIdleEntriesQueue<IConnection>(config);
	}

	
	public static  LBRedisProxyBasicPool<IConnection> createPool(LBRedisProxyPoolConfig config, LBRedisProxyIdleEntriesQueue<IConnection> queue, LBRedisProxyPoolObjectEntryFactory<IConnection> factory) throws Exception {
		return new LBRedisProxyBasicPool<IConnection>(config, queue, factory);
	}
}
