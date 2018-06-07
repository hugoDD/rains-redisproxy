/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.rains.proxy.core.pool.impl;


import com.rains.proxy.core.pool.IPoolEntry;
import com.rains.proxy.core.pool.IdleEntriesQueue;
import com.rains.proxy.core.pool.RedisProxyPool;
import com.rains.proxy.core.pool.PoolEntryFactory;
import com.rains.proxy.core.pool.commons.RedisProxyPoolConfig;
import com.rains.proxy.core.pool.commons.Pool;
import com.rains.proxy.core.pool.exception.RedisProxyPoolException;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;




/**
 * @author dourx
 * @version V1.0
 * 创建日期 2018/6/4
 * 连接池实现类
 */
public class RedisProxyBasicPool<T extends Pool> implements RedisProxyPool<T> {
	
	private final RedisProxyPoolConfig config;
	private final PoolEntryFactory<T> entryFactory;
	private final IdleEntriesQueue<T> idleEntriesQueue;//空闲存放队列
	private int totalCount;//当前总数
	private volatile boolean shuttingDown;//关闭
	private Scavenger scavenger;//回收线程
	
	public RedisProxyBasicPool(RedisProxyPoolConfig config, IdleEntriesQueue<T> idleEntriesQueue,
							   PoolEntryFactory<T> entryFactory) throws Exception {
		this.config = config;
		this.idleEntriesQueue = idleEntriesQueue;
		this.entryFactory = entryFactory;
		for (int i = 0; i < config.getInitialEntries(); i++) {
			try {
				idleEntriesQueue.offer(createIdleEntry());
			} catch (Exception e) {
				throw new RedisProxyPoolException(e);
			}
		}
		totalCount=idleEntriesQueue.getIdleEntriesCount();
		if (config.getTimeBetweenEvictionRunsMillis() > 0) {
	            this.scavenger = new Scavenger();
	            this.scavenger.start();
	    }
	}
	
	private IPoolEntry<T> createIdleEntry() throws Exception {
		IPoolEntry<T> entry= entryFactory.createPoolEntry();
		return entry;
	}
	


	@Override
	public IPoolEntry<T> borrowEntry() throws InterruptedException,
			TimeoutException, RedisProxyPoolException {
		if(idleEntriesQueue.getIdleEntriesCount()>0){//连接池足够
			return borrowEntry(false,config.getMaxWaitMillisOnBorrow(), TimeUnit.MILLISECONDS);
		}else if(idleEntriesQueue.getIdleEntriesCount()<=0){//连接池不足够，但是没有超过最大值
			return borrowEntry(true,config.getMaxWaitMillisOnBorrow(), TimeUnit.MILLISECONDS);
		}
		 throw new RedisProxyPoolException("已经超过连接池最大值");
	}

	/*
	 * 阻塞，默认系统的等待时间
	 */
	@Override
	public IPoolEntry<T> borrowEntry(boolean createNew) throws InterruptedException, TimeoutException, RedisProxyPoolException {
			return innerBorrowEntry(createNew,config.getMaxWaitMillisOnBorrow(),TimeUnit.MILLISECONDS);
	}

	/*
	 * 阻塞，默认设置的等待时间
	 */
	@Override
	public IPoolEntry<T> borrowEntry(boolean createNew, long timeout,
                                     TimeUnit unit) throws InterruptedException, TimeoutException,
            RedisProxyPoolException {

		return innerBorrowEntry(createNew,timeout,TimeUnit.MILLISECONDS);
	}
	
	private IPoolEntry<T> innerBorrowEntry(boolean createNew, long timeout, TimeUnit unit) throws RedisProxyPoolException {
		try {
			IPoolEntry<T> entry = idleEntriesQueue.poll();

			if (entry == null && createNew) {
				increaseObjects(1);
				return innerBorrowEntry(false,timeout,unit);//重新调用一次
			}
			if (entry == null) {
                throw new RedisProxyPoolException("Cannot get a free object from the pool");
            }
			if(entry!=null&&config.isTestOnBorrow()){//需要验证,物体对象是否完好
				if(!entryFactory.validateEntry(entry.getObject())){
					decreaseObject(entry);//验证不通过
					throw new RedisProxyPoolException("TestOnBorrow Entry  validate fail");
				}
			}
			return entry;
		} catch (Exception e) {
			
			throw new RedisProxyPoolException(e);
		}
	}
	
	/**
	 * 削减
	 * @param entry
	 * @throws RedisProxyPoolException
	 */
	public synchronized void decreaseObject(IPoolEntry<T> entry) throws RedisProxyPoolException {
		entry.getState().setValid(false);
		entryFactory.destroyEntry(entry.getObject());
		totalCount--;
		//System.out.println(totalCount);
	}
	/**
	 * 新创建
	 * @param delta
	 * @return
	 */
	private synchronized int increaseObjects(int delta) {
        if (delta + totalCount > config.getMaxActiveEntries()) {
			delta = config.getMaxActiveEntries() - totalCount;
        }
        try {
            for (int i = 0; i < delta; i++) {
            	idleEntriesQueue.offer(createIdleEntry());
            }
            totalCount += delta;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return delta;
    }
	
	

	@Override
	public void returnEntry(IPoolEntry<T> entry)
			throws RedisProxyPoolException {
		if (entry == null){
			throw new NullPointerException("returnEntry, entry is null");
		}
		if(config.isTestOnReturn()){//回归验证
			if(!entryFactory.validateEntry(entry.getObject())){
				decreaseObject(entry);//验证不通过
				throw new RedisProxyPoolException("returnEntry Entry  validate fail");
			}
		}
		idleEntriesQueue.offer(entry);
	}

	@Override
	public void shutDown() throws RedisProxyPoolException {
		
        while (this.idleEntriesQueue.getIdleEntriesCount() > 0) {
        	IPoolEntry<T> entry = idleEntriesQueue.poll();
            if (entry != null) {
                decreaseObject(entry);
            }
        }
        //idleEntriesQueue.clear();
        shuttingDown=true;
	}
	
	public synchronized void scavenge() throws RedisProxyPoolException {
        int delta = this.totalCount - config.getMinActiveEntries();
        if (delta <= 0) {
        	return;
        }
        long now = System.currentTimeMillis();
        IPoolEntry<T> entry;
        while (delta > 0 &&idleEntriesQueue.getIdleEntriesCount()>config.getMinIdleEntries()&& (entry = idleEntriesQueue.poll()) != null) {
            if (now - entry.getState().getLastValidatedAt() > config.getMinEvictableIdleTimeMillis()) {
                decreaseObject(entry); // shrink the pool size if the object reaches minEvictableIdleTimeMillis
            } else {
            	if(config.isTestWhileIdle()&&entryFactory.validateEntry(entry.getObject())){
            		idleEntriesQueue.offer(entry); //offer it back
            	}else if(config.isTestWhileIdle()&&!entryFactory.validateEntry(entry.getObject())){
            		decreaseObject(entry); //offer it back
            	}else{
            		idleEntriesQueue.offer(entry); //offer it back
            	}
            	
            }
        }
    }
	
	/**
	 * @return the totalCount
	 */
	public synchronized int getTotalCount() {
		return totalCount;
	}
	
	/**
	 * 检查空闲线程的状态
	 * @author liubing
	 *
	 */
	 private class Scavenger extends Thread {

	        @Override
	        public void run() {
	            while (!RedisProxyBasicPool.this.shuttingDown) {
	                try {
	                    Thread.sleep(config.getTimeBetweenEvictionRunsMillis());
	                    scavenge();
	                } catch (Exception  ignored) {
	                
	                }
	            }
	        }

	    }
}
