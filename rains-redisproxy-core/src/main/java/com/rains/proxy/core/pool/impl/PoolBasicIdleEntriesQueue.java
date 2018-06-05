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


import com.rains.proxy.core.pool.PoolEntry;
import com.rains.proxy.core.pool.IdleEntriesQueue;
import com.rains.proxy.core.pool.commons.Pool;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.TimeUnit;



/**
 * @author dourx
 * @version V1.0
 * 创建日期 2018/6/5
 * 队列实现类
 */
public class PoolBasicIdleEntriesQueue<T extends Pool> implements IdleEntriesQueue<T> {
	
	private final ArrayBlockingQueue<PoolEntry<T>> idleEntries;
	


	public PoolBasicIdleEntriesQueue(int maxActive){
		idleEntries = new ArrayBlockingQueue<>(maxActive);
	}

	@Override
	public PoolEntry<T> poll(long timeout) throws InterruptedException {

		PoolEntry<T> idle = idleEntries.poll(timeout,TimeUnit.MILLISECONDS);
		return idle;
	}
	
	@Override
	public PoolEntry<T> poll(long timeout, TimeUnit unit) throws InterruptedException {
		
		PoolEntry<T> idle = idleEntries.poll(timeout,unit);
		return idle;	
	}

	@Override
	public boolean offer(PoolEntry<T> entry) throws NullPointerException {
		if (entry == null)
			throw new NullPointerException("entry is null.");

		boolean offerSuccessful = idleEntries.offer(entry);
		return offerSuccessful;
	}

//	protected ArrayBlockingQueue<PoolEntry<T>> getIdleEntries() {
//		return idleEntries;
//	}

	@Override
	public int getIdleEntriesCount() {
		return idleEntries.size();
	}

	@Override
	public void clear() {
		
		idleEntries.clear();
	}

	@Override
	public PoolEntry<T> poll() {
		PoolEntry<T> idle = idleEntries.poll();
		return idle;
	}
	
	
}
