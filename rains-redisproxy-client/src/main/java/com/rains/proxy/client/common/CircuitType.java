package com.rains.proxy.client.common;


import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.HystrixCommandKey;
import com.netflix.hystrix.HystrixThreadPoolKey;
import org.springframework.util.StringUtils;

public class CircuitType {
	
	private HystrixCommandGroupKey groupKey;
	
	private HystrixCommandKey commandKey;
	
	private HystrixThreadPoolKey threadPoolKey; 
	
	public CircuitType(String groupKey, String commandKey, String threadPoolKey) {
		
		this.groupKey = HystrixCommandGroupKey.Factory.asKey(groupKey);
		
		if(!StringUtils.isEmpty(commandKey)) {
			this.commandKey = HystrixCommandKey.Factory.asKey(commandKey);
		}
		
		if(!StringUtils.isEmpty(threadPoolKey)) {
			this.threadPoolKey = HystrixThreadPoolKey.Factory.asKey(threadPoolKey);
		}
	}
	
	public CircuitType(String groupKey, String threadPoolKey) {
		
		this(groupKey, null, threadPoolKey);
	}
	
	public CircuitType(String groupKey) {
		
		this(groupKey, null, null);
	}

	public HystrixCommandGroupKey getGroupKey() {
		return groupKey;
	}

	public void setGroupKey(String groupKey) {
		this.groupKey = HystrixCommandGroupKey.Factory.asKey(groupKey);
	}

	public HystrixCommandKey getCommandKey() {
		return commandKey;
	}

	public void setCommandKey(String commandKey) {
		this.commandKey = HystrixCommandKey.Factory.asKey(commandKey);
	}

	public HystrixThreadPoolKey getThreadPoolKey() {
		return threadPoolKey;
	}

	public void setThreadPoolKey(String threadPoolKey) {
		this.threadPoolKey = HystrixThreadPoolKey.Factory.asKey(threadPoolKey);
	}
	
	
}
