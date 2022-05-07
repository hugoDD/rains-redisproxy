package com.rains.proxy.core.config;

import com.rains.proxy.core.cluster.LoadBalance;
import com.rains.proxy.core.cluster.impl.ConsistentHashLoadBalance;
import com.rains.proxy.core.cluster.impl.RoundRobinLoadBalance;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

/**
 * @author dourx
 * @version V1.0
 * @Description: TODO
 * @date 2018年 05 月  18日  15:15
 */
@SpringBootApplication
public class RedisProxyMockTest {

    @Bean(name="loadMasterBalance")
    public LoadBalance consistentHashLoadBalance(){
        return new ConsistentHashLoadBalance();
    }

    @Bean(name="loadMasterBalance")
    public LoadBalance roundRobinLoadBalance(){
        return new RoundRobinLoadBalance();
    }
}
