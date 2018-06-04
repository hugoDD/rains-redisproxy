/**
 * 
 */
package com.rains.proxy.core.pool;


import com.rains.proxy.core.pool.commons.Pool;

import java.util.concurrent.TimeUnit;


/**
 * 空闲队列接口
 * @author liubing
 *
 */
public interface LBRedisProxyIdleEntriesQueue<T extends Pool> {
	
	/**
	 * 
	 * @return
	 */
	LBRedisProxyPoolEntry<T> poll(long timeout, TimeUnit unit) throws InterruptedException;
	
	
	LBRedisProxyPoolEntry<T> poll();
	
	/**
	 * 
	 * @param entry
	 * @return
	 * @throws NullPointerException
	 */
	boolean offer(LBRedisProxyPoolEntry<T> entry) throws NullPointerException;
	
	
	public int getIdleEntriesCount();
	
	
	public void clear();
}
