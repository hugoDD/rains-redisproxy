//package com.rains.proxy.server.demo.config;
//
//import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
//import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingClass;
//import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
//import org.springframework.cloud.client.loadbalancer.LoadBalancerInterceptor;
//import org.springframework.cloud.client.loadbalancer.LoadBalancerRequestFactory;
//import org.springframework.cloud.client.loadbalancer.RestTemplateCustomizer;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.http.client.ClientHttpRequestInterceptor;
//import org.springframework.web.client.RestTemplate;
//
//import java.util.ArrayList;
//import java.util.List;
//
//@Configuration
//@ConditionalOnMissingClass("org.springframework.retry.support.RetryTemplate")
//public class RedisLoadBalancerInterceptorConfig {
//    @Bean
//    public LoadBalancerInterceptor ribbonInterceptor(
//            LoadBalancerClient loadBalancerClient,
//            LoadBalancerRequestFactory requestFactory) {
//        return new LoadBalancerInterceptor(loadBalancerClient, requestFactory);
//    }
//
//    @Bean
//    @ConditionalOnMissingBean
//    public RestTemplateCustomizer restTemplateCustomizer(
//            final LoadBalancerInterceptor loadBalancerInterceptor) {
//        return new RestTemplateCustomizer() {
//            @Override
//            public void customize(RestTemplate restTemplate) {
//                List<ClientHttpRequestInterceptor> list = new ArrayList<>(
//                        restTemplate.getInterceptors());
//                list.add(loadBalancerInterceptor);
//                restTemplate.setInterceptors(list);
//            }
//        };
//    }
//}
