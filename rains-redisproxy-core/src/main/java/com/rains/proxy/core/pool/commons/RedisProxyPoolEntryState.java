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
package com.rains.proxy.core.pool.commons;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author dourx
 * @version V1.0
 * 创建日期 2018/6/4
 */
public class RedisProxyPoolEntryState {
	
	private final long startAt = System.currentTimeMillis();

	private AtomicLong lastValidatedAt = new AtomicLong(
			System.currentTimeMillis());

	private AtomicBoolean valid = new AtomicBoolean(true);

	public long getStartAt() {
		return startAt;
	}

	/**
	 *
	 * @return lastValidatedAt
	 * */
	public long getLastValidatedAt() {
		return lastValidatedAt.longValue();
	}

	/**
	 * Set lastValidatedAt.
	 * 
	 * @param lastValidatedAt
	 * */
	public void setLastValidatedAt(long lastValidatedAt) {
		this.lastValidatedAt.set(lastValidatedAt);
	}

	/**
	 * */
	public boolean isValid() {
		return valid.get();
	}

	/**
	 * Compare and set valid
	 * 
	 * @param expect
	 * @param update
	 * @return true, if succeeded
	 * @see AtomicBoolean#compareAndSet(boolean, boolean)
	 * */
	public boolean compareAndSetValid(boolean expect, boolean update) {
		return valid.compareAndSet(expect, update);
	}
	
	public void setValid(Boolean flag) {
		valid.set(flag);
	}
}
