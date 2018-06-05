/*
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
package com.rains.proxy.core.pool.util;


import com.rains.proxy.core.connection.IConnection;
import com.rains.proxy.core.pool.IdleEntriesQueue;
import com.rains.proxy.core.pool.PoolEntryFactory;
import com.rains.proxy.core.pool.PooledObjectFactory;
import com.rains.proxy.core.pool.commons.RedisProxyPoolConfig;
import com.rains.proxy.core.pool.impl.RedisProxyBasicPool;
import com.rains.proxy.core.pool.impl.PoolBasicIdleEntriesQueue;
import com.rains.proxy.core.pool.impl.PoolBasicEntryFactory;

/**
 * @author dourx
 * @version V1.0
 * 创建日期 2018/6/5
 */
public class PoolUtils {

	/**
	 * 连接池 pool
	 * @param config
	 * @param factory
	 * @return
	 * @throws Exception
	 */
	public static RedisProxyBasicPool<IConnection> createPool(RedisProxyPoolConfig config, PooledObjectFactory<IConnection> factory) throws Exception {
		return createPool( config, new PoolBasicIdleEntriesQueue(config.getMaxActiveEntries()),new PoolBasicEntryFactory(factory));
	}


	


	
	public static RedisProxyBasicPool<IConnection> createPool(RedisProxyPoolConfig config, IdleEntriesQueue<IConnection> queue, PoolEntryFactory<IConnection> factory) throws Exception {
		return new RedisProxyBasicPool<>(config, queue, factory);
	}
}
