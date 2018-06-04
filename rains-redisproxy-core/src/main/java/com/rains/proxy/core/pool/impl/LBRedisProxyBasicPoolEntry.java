/**
 * 
 */
package com.rains.proxy.core.pool.impl;


import com.rains.proxy.core.pool.LBRedisProxyPoolEntry;
import com.rains.proxy.core.pool.commons.LBRedisProxyPoolEntryState;
import com.rains.proxy.core.pool.commons.Pool;

/**
 * 对象T 封装
 * @author liubing
 *
 */
public class LBRedisProxyBasicPoolEntry<T extends Pool> implements LBRedisProxyPoolEntry<T> {
	
	private final T object;
	
	private final LBRedisProxyPoolEntryState state;
	
	public LBRedisProxyBasicPoolEntry(T object) {
		super();
		this.object = object;
		this.state = new LBRedisProxyPoolEntryState();
	}

	/* (non-Javadoc)
	 * @see com.wanda.ffan.rpc.pool.FfanRpcPoolEntry#getObject()
	 */
	@Override
	public T getObject() {
		return object;
	}

	/* (non-Javadoc)
	 * @see com.wanda.ffan.rpc.pool.FfanRpcPoolEntry#getState()
	 */
	@Override
	public LBRedisProxyPoolEntryState getState() {
		return state;
	}

}
