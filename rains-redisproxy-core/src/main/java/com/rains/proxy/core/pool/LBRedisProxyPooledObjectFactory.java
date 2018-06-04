/**
 * 
 */
package com.rains.proxy.core.pool;


import com.rains.proxy.core.pool.commons.Pool;
import com.rains.proxy.core.pool.exception.LBRedisProxyPoolException;

/**
 * 对象创建接口 T
 * @author liubing
 *
 */
public interface LBRedisProxyPooledObjectFactory<T extends Pool> {
	
	/**
	 * 创建实例
	 * @return
	 * @throws LBRedisProxyPoolException
	 */
	T createInstance() throws LBRedisProxyPoolException;
	
	/**
	 * 验证T
	 * @param t
	 * @throws Exception
	 */
	public Boolean validateEntry(T t) throws LBRedisProxyPoolException;
	
	/**
	 * 销废
	 * @param t
	 * @throws Exception
	 */
	public void destroyEntry(T t) throws LBRedisProxyPoolException;
}
