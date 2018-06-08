///**
// * Licensed to the Apache Software Foundation (ASF) under one or more
// * contributor license agreements.  See the NOTICE file distributed with
// * this work for additional information regarding copyright ownership.
// * The ASF licenses this file to You under the Apache License, Version 2.0
// * (the "License"); you may not use this file except in compliance with
// * the License.  You may obtain a copy of the License at
// *
// *     http://www.apache.org/licenses/LICENSE-2.0
// *
// * Unless required by applicable law or agreed to in writing, software
// * distributed under the License is distributed on an "AS IS" BASIS,
// * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// * See the License for the specific language governing permissions and
// * limitations under the License.
// */
//package com.rains.proxy.core.pool.commons;
//
//
//import com.rains.proxy.core.pool.exception.RedisProxyPoolPropertyValidationException;
//
///**
// * @author dourx
// * @version V1.0
// * 创建日期 2018/6/4
// */
//public class RedisProxyPoolConfig {
//
//
//
//	private String host;
//
//	private int port;
//
//	private int maxActiveEntries = 8;//最大活跃值
//
//	private int initialEntries = 0;//初始化值
//
//	private int minActiveEntries = 0;//最大活跃值
//
//	private long maxWaitMillisOnBorrow = 500; // 最大等待时间
//
//	private long timeBetweenEvictionRunsMillis;//回收间隔时间点
//
//	private long minEvictableIdleTimeMillis;//池中最小生存的时间
//
//	private int minIdleEntries = 0;//最小等待时间
//
//	private boolean testOnBorrow=false;//取出验证
//
//	private int connectionTimeout;//连接超时
//
//	private boolean testOnReturn=false;//回收时验证
//
//	private boolean testWhileIdle=false;//回收空闲验证
//
//	public void validatePropCorrelation() throws RedisProxyPoolPropertyValidationException {
//		if (maxActiveEntries < initialEntries)
//			throw new RedisProxyPoolPropertyValidationException(
//					"maxActiveEntries < initialEntries");
//		if (maxActiveEntries < minActiveEntries)
//			throw new RedisProxyPoolPropertyValidationException(
//					"maxActiveEntries < minActiveEntries");
//		if (initialEntries < minActiveEntries)
//			throw new RedisProxyPoolPropertyValidationException(
//					"initialEntries < minActiveEntries");
//		if (maxActiveEntries < minIdleEntries)
//			throw new RedisProxyPoolPropertyValidationException(
//					"maxActiveEntries < minIdleEntries");
//
//	}
//
//
//	public int getMaxActiveEntries() {
//		return maxActiveEntries;
//	}
//
//	public void setMaxActiveEntries(int maxActiveEntries) {
//		this.maxActiveEntries = maxActiveEntries;
//	}
//
//	public int getInitialEntries() {
//		return initialEntries;
//	}
//
//	public void setInitialEntries(int initialEntries) {
//		this.initialEntries = initialEntries;
//	}
//
//	public long getMaxWaitMillisOnBorrow() {
//		return maxWaitMillisOnBorrow;
//	}
//
//	public void setMaxWaitMillisOnBorrow(long maxWaitMillisOnBorrow) {
//		this.maxWaitMillisOnBorrow = maxWaitMillisOnBorrow;
//	}
//
//
//	public int getMinIdleEntries() {
//		return minIdleEntries;
//	}
//
//	public void setMinIdleEntries(int minIdleEntries) {
//		this.minIdleEntries = minIdleEntries;
//	}
//
//	public String getHost() {
//		return host;
//	}
//
//	public void setHost(String host) {
//		this.host = host;
//	}
//
//	public int getPort() {
//		return port;
//	}
//
//	public void setPort(int port) {
//		this.port = port;
//	}
//
//
//	/**
//	 * @return the testOnBorrow
//	 */
//	public boolean isTestOnBorrow() {
//		return testOnBorrow;
//	}
//
//
//	/**
//	 * @param testOnBorrow the testOnBorrow to set
//	 */
//	public void setTestOnBorrow(boolean testOnBorrow) {
//		this.testOnBorrow = testOnBorrow;
//	}
//
//	/**
//	 * @return the testOnReturn
//	 */
//	public boolean isTestOnReturn() {
//		return testOnReturn;
//	}
//
//
//	/**
//	 * @param testOnReturn the testOnReturn to set
//	 */
//	public void setTestOnReturn(boolean testOnReturn) {
//		this.testOnReturn = testOnReturn;
//	}
//
//
//	/**
//	 * @return the testWhileIdle
//	 */
//	public boolean isTestWhileIdle() {
//		return testWhileIdle;
//	}
//
//
//	/**
//	 * @param testWhileIdle the testWhileIdle to set
//	 */
//	public void setTestWhileIdle(boolean testWhileIdle) {
//		this.testWhileIdle = testWhileIdle;
//	}
//
//
//	/**
//	 * @return the minActiveEntries
//	 */
//	public int getMinActiveEntries() {
//		return minActiveEntries;
//	}
//
//
//	/**
//	 * @param minActiveEntries the minActiveEntries to set
//	 */
//	public void setMinActiveEntries(int minActiveEntries) {
//		this.minActiveEntries = minActiveEntries;
//	}
//
//
//	/**
//	 * @return the timeBetweenEvictionRunsMillis
//	 */
//	public long getTimeBetweenEvictionRunsMillis() {
//		return timeBetweenEvictionRunsMillis;
//	}
//
//
//	/**
//	 * @param timeBetweenEvictionRunsMillis the timeBetweenEvictionRunsMillis to set
//	 */
//	public void setTimeBetweenEvictionRunsMillis(long timeBetweenEvictionRunsMillis) {
//		this.timeBetweenEvictionRunsMillis = timeBetweenEvictionRunsMillis;
//	}
//
//
//	/**
//	 * @return the minEvictableIdleTimeMillis
//	 */
//	public long getMinEvictableIdleTimeMillis() {
//		return minEvictableIdleTimeMillis;
//	}
//
//
//	/**
//	 * @param minEvictableIdleTimeMillis the minEvictableIdleTimeMillis to set
//	 */
//	public void setMinEvictableIdleTimeMillis(long minEvictableIdleTimeMillis) {
//		this.minEvictableIdleTimeMillis = minEvictableIdleTimeMillis;
//	}
//
//
//	/**
//	 * @return the connectionTimeout
//	 */
//	public int getConnectionTimeout() {
//		return connectionTimeout;
//	}
//
//
//	/**
//	 * @param connectionTimeout the connectionTimeout to set
//	 */
//	public void setConnectionTimeout(int connectionTimeout) {
//		this.connectionTimeout = connectionTimeout;
//	}
//
//
//}
