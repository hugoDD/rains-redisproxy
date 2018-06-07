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
import com.rains.proxy.core.pool.PoolEntryFactory;
import com.rains.proxy.core.pool.PooledObjectFactory;
import com.rains.proxy.core.pool.commons.Pool;
import com.rains.proxy.core.pool.exception.RedisProxyPoolException;



/**
 * @author dourx
 * @version V1.0
 * 创建日期 2018/6/4
 * 对象T 封装 工厂类
 */
public class PoolBasicEntryFactory<T extends Pool> implements PoolEntryFactory<T> {
	
	private final PooledObjectFactory<T> rpcPooledObjectFactory;
	
	
	public PoolBasicEntryFactory(
			PooledObjectFactory<T> rpcPoolBasicEntryFactory) {
		super();
		this.rpcPooledObjectFactory = rpcPoolBasicEntryFactory;
	}



	@Override
	public IPoolEntry createPoolEntry() throws RedisProxyPoolException {
		T object = null;
		try {
			object = rpcPooledObjectFactory.createInstance();
			return new PoolEntry<T>(object);
		} catch (RedisProxyPoolException e) {
			throw e;
		}
	}


	@Override
	public Boolean validateEntry(T t) throws RedisProxyPoolException {
		return rpcPooledObjectFactory.validateEntry(t);
	}


	@Override
	public void destroyEntry(T t) throws RedisProxyPoolException {
		rpcPooledObjectFactory.destroyEntry(t);
	}

}
