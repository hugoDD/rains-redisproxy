/**
 * 
 */
package com.rains.proxy.core.pool.impl;


import com.rains.proxy.core.pool.LBRedisProxyIdleEntriesQueue;
import com.rains.proxy.core.pool.LBRedisProxyPool;
import com.rains.proxy.core.pool.LBRedisProxyPoolEntry;
import com.rains.proxy.core.pool.LBRedisProxyPoolObjectEntryFactory;
import com.rains.proxy.core.pool.commons.LBRedisProxyPoolConfig;
import com.rains.proxy.core.pool.commons.Pool;
import com.rains.proxy.core.pool.exception.LBRedisProxyPoolException;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;


/**
 * 连接池实现类
 * @author liubing
 *
 */
public class LBRedisProxyBasicPool<T extends Pool> implements LBRedisProxyPool<T> {
	
	private final LBRedisProxyPoolConfig config;
	private final LBRedisProxyPoolObjectEntryFactory<T> entryFactory;
	private final LBRedisProxyIdleEntriesQueue<T> idleEntriesQueue;//空闲存放队列
	private int totalCount;//当前总数
	private volatile boolean shuttingDown;//关闭
	private Scavenger scavenger;//回收线程
	
	public LBRedisProxyBasicPool(LBRedisProxyPoolConfig config, LBRedisProxyIdleEntriesQueue<T> idleEntriesQueue,
			LBRedisProxyPoolObjectEntryFactory<T> entryFactory) throws Exception {
		this.config = config;
		this.idleEntriesQueue = idleEntriesQueue;
		this.entryFactory = entryFactory;
		for (int i = 0; i < config.getInitialEntries(); i++) {
			try {
				idleEntriesQueue.offer(createIdleEntry());
			} catch (Exception e) {
				throw new LBRedisProxyPoolException(e);
			}
		}
		totalCount=config.getMinActiveEntries();
		if (config.getTimeBetweenEvictionRunsMillis() > 0) {
	            this.scavenger = new Scavenger();
	            this.scavenger.start();
	    }
	}
	
	private LBRedisProxyPoolEntry<T> createIdleEntry() throws Exception {
		LBRedisProxyPoolEntry<T> entry= entryFactory.createPoolEntry();
		return entry;
	}
	

	/* (non-Javadoc)
	 * @see com.wanda.ffan.rpc.pool.FfanRpcPool#borrowEntry()
	 */
	@Override
	public LBRedisProxyPoolEntry<T> borrowEntry() throws InterruptedException,
			TimeoutException, LBRedisProxyPoolException {
		if(idleEntriesQueue.getIdleEntriesCount()>0){//连接池足够
			return borrowEntry(false,config.getMaxWaitMillisOnBorrow(), TimeUnit.MILLISECONDS);
		}else if(idleEntriesQueue.getIdleEntriesCount()<=0){//连接池不足够，但是没有超过最大值
			return borrowEntry(true,config.getMaxWaitMillisOnBorrow(), TimeUnit.MILLISECONDS);
		}
		 throw new LBRedisProxyPoolException("已经超过连接池最大值");
	}

	/* (non-Javadoc)
	 * @see com.wanda.ffan.rpc.pool.FfanRpcPool#borrowEntry(boolean)
	 * 阻塞，默认系统的等待时间
	 */
	@Override
	public LBRedisProxyPoolEntry<T> borrowEntry(boolean createNew) throws InterruptedException, TimeoutException, LBRedisProxyPoolException {		
			return innerBorrowEntry(createNew,config.getMaxWaitMillisOnBorrow(),TimeUnit.MILLISECONDS);
	}

	/* (non-Javadoc)
	 * @see com.wanda.ffan.rpc.pool.FfanRpcPool#borrowEntry(boolean, long, java.util.concurrent.TimeUnit)
	 * 阻塞，默认设置的等待时间
	 */
	@Override
	public LBRedisProxyPoolEntry<T> borrowEntry(boolean createNew, long timeout,
			TimeUnit unit) throws InterruptedException, TimeoutException,
			LBRedisProxyPoolException {

		return innerBorrowEntry(createNew,timeout,TimeUnit.MILLISECONDS);
	}
	
	private LBRedisProxyPoolEntry<T> innerBorrowEntry(boolean createNew, long timeout,TimeUnit unit) throws LBRedisProxyPoolException {
		try {
			LBRedisProxyPoolEntry<T> entry = idleEntriesQueue.poll();

			if (entry == null && createNew) {
				increaseObjects(1);
				return innerBorrowEntry(false,timeout,unit);//重新调用一次
			}
			if (entry == null) {
                throw new LBRedisProxyPoolException("Cannot get a free object from the pool");
            }
			if(entry!=null&&config.isTestOnBorrow()){//需要验证,物体对象是否完好
				if(!entryFactory.validateEntry(entry.getObject())){
					decreaseObject(entry);//验证不通过
					throw new LBRedisProxyPoolException("TestOnBorrow Entry  validate fail");
				}
			}
			return entry;
		} catch (Exception e) {
			
			throw new LBRedisProxyPoolException(e);
		}
	}
	
	/**
	 * 削减
	 * @param entry
	 * @throws LBRedisProxyPoolException
	 */
	public synchronized void decreaseObject(LBRedisProxyPoolEntry<T> entry) throws LBRedisProxyPoolException{
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
	
	
	/* (non-Javadoc)
	 * @see com.wanda.ffan.rpc.pool.FfanRpcPool#returnEntry(com.wanda.ffan.rpc.pool.FfanRpcPoolEntry)
	 */
	@Override
	public void returnEntry(LBRedisProxyPoolEntry<T> entry)
			throws LBRedisProxyPoolException {
		if (entry == null){
			throw new NullPointerException("returnEntry, entry is null");
		}
		if(config.isTestOnReturn()){//回归验证
			if(!entryFactory.validateEntry(entry.getObject())){
				decreaseObject(entry);//验证不通过
				throw new LBRedisProxyPoolException("returnEntry Entry  validate fail");
			}
		}
		idleEntriesQueue.offer(entry);
	}

	@Override
	public void shutDown() throws LBRedisProxyPoolException{		
		
        while (this.idleEntriesQueue.getIdleEntriesCount() > 0) {
        	LBRedisProxyPoolEntry<T> entry = idleEntriesQueue.poll();
            if (entry != null) {
                decreaseObject(entry);
            }
        }
        //idleEntriesQueue.clear();
        shuttingDown=true;
	}
	
	public synchronized void scavenge() throws LBRedisProxyPoolException {
        int delta = this.totalCount - config.getMinActiveEntries();
        if (delta <= 0) {
        	return;
        }
        long now = System.currentTimeMillis();
        LBRedisProxyPoolEntry<T> entry;
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
	            while (!LBRedisProxyBasicPool.this.shuttingDown) {
	                try {
	                    Thread.sleep(config.getTimeBetweenEvictionRunsMillis());
	                    scavenge();
	                } catch (Exception  ignored) {
	                
	                }
	            }
	        }

	    }
}
