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
package com.rains.proxy.net.client;


import com.rains.proxy.core.connection.IConnection;
import com.rains.proxy.core.pool.PooledObjectFactory;
import com.rains.proxy.core.pool.commons.RedisProxyPoolConfig;
import com.rains.proxy.core.pool.exception.RedisProxyPoolException;

/**
 * @author dourx
 * @version V1.0
 * 创建日期 2018/6/4
 */
public class RedisConnectionFactory implements PooledObjectFactory<IConnection> {
	
	private RedisProxyPoolConfig redisProxyPoolConfig;
	
	/**
	 * 
	 * @param redisProxyPoolConfig
	 */
	public RedisConnectionFactory(RedisProxyPoolConfig redisProxyPoolConfig) {
		super();
		this.redisProxyPoolConfig = redisProxyPoolConfig;
	}

	@Override
	public IConnection createInstance() throws RedisProxyPoolException {
		RedisConnection redisConnection=new RedisConnection(redisProxyPoolConfig);
		redisConnection.open();
		return redisConnection;
	}



	@Override
	public Boolean validateEntry(IConnection connection)
			throws RedisProxyPoolException {
		return connection.isAvailable();
	}



	@Override
	public void destroyEntry(IConnection connection) throws RedisProxyPoolException {
		connection.close();
	}
	
	
	
}
