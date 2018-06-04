/**
 * 
 */
package com.rains.proxy.core.pool.impl;


import com.rains.proxy.core.pool.LBRedisProxyPoolEntry;
import com.rains.proxy.core.pool.LBRedisProxyPoolObjectEntryFactory;
import com.rains.proxy.core.pool.LBRedisProxyPooledObjectFactory;
import com.rains.proxy.core.pool.commons.Pool;
import com.rains.proxy.core.pool.exception.LBRedisProxyPoolException;

/**对象T 封装 工厂类
 * @author liubing
 *
 */
public class LBRedisProxyPoolBasicObjectEntryFactory<T extends Pool> implements LBRedisProxyPoolObjectEntryFactory<T> {
	
	private final LBRedisProxyPooledObjectFactory<T> ffanRpcPooledObjectFactory;
	
	
	public LBRedisProxyPoolBasicObjectEntryFactory(
			LBRedisProxyPooledObjectFactory<T> ffanRpcPoolBasicEntryFactory) {
		super();
		this.ffanRpcPooledObjectFactory = ffanRpcPoolBasicEntryFactory;
	}


	/* (non-Javadoc)
	 * @see com.wanda.ffan.rpc.pool.FfanRpcPoolEntryFactory#createPoolEntry()
	 */
	@Override
	public LBRedisProxyPoolEntry<T> createPoolEntry() throws LBRedisProxyPoolException {
		T object = null;
		try {
			object = ffanRpcPooledObjectFactory.createInstance();
			return new LBRedisProxyBasicPoolEntry<T>(object);
		} catch (LBRedisProxyPoolException e) {
			throw e;
		}
	}


	@Override
	public Boolean validateEntry(T t) throws LBRedisProxyPoolException {
		return ffanRpcPooledObjectFactory.validateEntry(t);
	}


	@Override
	public void destroyEntry(T t) throws LBRedisProxyPoolException {
		ffanRpcPooledObjectFactory.destroyEntry(t);
	}

}
