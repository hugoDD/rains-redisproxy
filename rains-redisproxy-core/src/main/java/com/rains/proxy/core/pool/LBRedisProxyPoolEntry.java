/**
 * 
 */
package com.rains.proxy.core.pool;

import com.rains.proxy.core.pool.commons.LBRedisProxyPoolEntryState;
import com.rains.proxy.core.pool.commons.Pool;

/**
 * 对象T接口封装
 * @author liubing
 *
 */
public interface LBRedisProxyPoolEntry<T extends Pool> {
	
	T getObject();

	
	LBRedisProxyPoolEntryState getState();
}
