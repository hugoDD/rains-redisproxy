package com.rains.proxy.client.config;

import com.rains.proxy.client.redis.data.RedisProxyConnectionFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.cloud.netflix.ribbon.SpringClientFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.RedisConnectionFactory;

@Configuration
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
