package com.rains.proxy.client.command;


import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandProperties;
import com.netflix.hystrix.HystrixCommandProperties.ExecutionIsolationStrategy;
import com.rains.proxy.client.common.CircuitType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RedisProxyHystrixCommand extends HystrixCommand<String> {

	private static final Logger logger = LoggerFactory.getLogger(RedisProxyHystrixCommand.class);
    
    private static com.netflix.hystrix.HystrixCommandProperties.Setter setter = null;


    static {
    	setter = HystrixCommandProperties.Setter()
    				.withCircuitBreakerRequestVolumeThreshold(1) // 断路器打开的失败最大数
    				.withCircuitBreakerSleepWindowInMilliseconds(5 * 1000) // 断路器打开后尝试回复时间
    				.withExecutionIsolationThreadInterruptOnTimeout(true)
    				.withExecutionTimeoutInMilliseconds(5000)  // Command 超时时间
    				.withExecutionIsolationStrategy(ExecutionIsolationStrategy.SEMAPHORE);
    }
    
    public RedisProxyHystrixCommand(CircuitType type) {
    	
        super(Setter.withGroupKey(type.getGroupKey())
        		.andCommandKey(type.getCommandKey())
        		.andThreadPoolKey(type.getThreadPoolKey())
        		.andCommandPropertiesDefaults(setter));
    }


    @Override
    protected String run() throws Exception {
        return null;
    }
}
