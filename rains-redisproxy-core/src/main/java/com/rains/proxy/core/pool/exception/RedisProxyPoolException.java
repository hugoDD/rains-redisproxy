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
package com.rains.proxy.core.pool.exception;

/**
 * @author dourx
 * @version V1.0
 * 创建日期 2018/6/4
 * 自定义连接池exception
 */
public class RedisProxyPoolException extends Exception {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1172816022950399557L;

	public RedisProxyPoolException() {
		super();
	}

	public RedisProxyPoolException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

	public RedisProxyPoolException(String arg0) {
		super(arg0);
	}

	public RedisProxyPoolException(Throwable arg0) {
		super(arg0);
	}
}
