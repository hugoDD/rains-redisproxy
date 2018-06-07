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
package com.rains.proxy.core.pool;

import com.rains.proxy.core.pool.commons.Pool;
import com.rains.proxy.core.pool.exception.RedisProxyPoolException;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;


/**
 * @author dourx
 * @version V1.0
 * 创建日期 2018/6/4
 */
public interface RedisProxyPool<T  extends Pool> {


	IPoolEntry<T> borrowEntry() throws InterruptedException, TimeoutException,RedisProxyPoolException;

	IPoolEntry<T> borrowEntry(boolean createNew) throws InterruptedException,
			TimeoutException, RedisProxyPoolException;

	IPoolEntry<T> borrowEntry(boolean createNew, long timeout, TimeUnit unit)
			throws InterruptedException, TimeoutException, RedisProxyPoolException;

	void returnEntry(IPoolEntry<T> entry) throws RedisProxyPoolException;
	
	void shutDown()throws RedisProxyPoolException;
}
