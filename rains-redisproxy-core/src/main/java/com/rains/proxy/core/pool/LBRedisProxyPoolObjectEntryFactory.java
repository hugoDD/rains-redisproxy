/**
 * 
 */
package com.rains.proxy.core.pool;


import com.rains.proxy.core.pool.commons.Pool;
import com.rains.proxy.core.pool.exception.LBRedisProxyPoolException;

/**
 * 对象T 封装 工厂类 接口
 * @author liubing
 *
 */
public interface LBRedisProxyPoolObjectEntryFactory<T extends Pool> {
	
	LBRedisProxyPoolEntry<T> createPoolEntry() throws LBRedisProxyPoolException;
	
	/**
	 * 销废
	 * @param t
	 * @throws Exception
	 */
	public void destroyEntry(T t) throws LBRedisProxyPoolException;
	
	/**
	 * 验证T
	 * @param t
	 * @throws Exception
	 */
	public Boolean validateEntry(T t) throws LBRedisProxyPoolException;
}
