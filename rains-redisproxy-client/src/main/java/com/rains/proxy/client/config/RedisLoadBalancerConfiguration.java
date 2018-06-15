package com.rains.proxy.client.config;

import com.netflix.discovery.EurekaClientConfig;
import com.rains.proxy.client.redis.data.RedisProxyConnectionFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.cloud.netflix.ribbon.SpringClientFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.RedisConnectionFactory;

@Configuration
@ConditionalOnClass(EurekaClientConfig.class)
@ConditionalOnProperty(value = "eureka.client.service-url.defaultZone", matchIfMissing = false)
public class RedisLoadBalancerConfiguration{
    @Bean
    @Primary
    @ConditionalOnMissingBean
    public RedisConnectionFactory redisProxyConnectionFactory(
            SpringClientFactory springClientFactory,
           RedisProperties properties) {
        return new RedisProxyConnectionFactory(springClientFactory,properties);
    }


}
