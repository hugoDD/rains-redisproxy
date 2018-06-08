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
package com.rains.proxy.net;

import com.netflix.appinfo.EurekaInstanceConfig;
import com.rains.proxy.core.bean.RedisServerMasterCluster;
import com.rains.proxy.core.bean.support.RedisServerBean;
import com.rains.proxy.core.bean.support.RedisServerClusterBean;
import com.rains.proxy.core.cluster.LoadBalance;
import com.rains.proxy.core.cluster.impl.ConsistentHashLoadBalance;
import com.rains.proxy.core.cluster.impl.RoundRobinLoadBalance;
import com.rains.proxy.core.config.RedisProxyConfiguration;
import com.rains.proxy.core.config.RedisProxyMaster;
import com.rains.proxy.core.config.RedisProxyPoolConfig;
import com.rains.proxy.core.config.RedisProxySlave;
import com.rains.proxy.net.model.Server;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.*;
import org.springframework.boot.bind.RelaxedPropertyResolver;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.commons.util.InetUtils;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.eureka.EurekaInstanceConfigBean;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.PropertyResolver;
import org.springframework.util.StringUtils;


import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;

import static org.springframework.cloud.commons.util.IdUtils.getDefaultInstanceId;

@Configuration
@ComponentScan
@EnableEurekaClient
@EnableFeignClients
@EnableConfigurationProperties
//@ConditionalOnClass(EurekaClientConfig.class)
//@ConditionalOnProperty(value = "eureka.client.enabled", matchIfMissing = true)
//@AutoConfigureBefore({ NoopDiscoveryClientAutoConfiguration.class,
//        CommonsClientAutoConfiguration.class, ServiceRegistryAutoConfiguration.class })
//@AutoConfigureAfter(name = "org.springframework.cloud.autoconfigure.RefreshAutoConfiguration")
public class ServerConfiguration {
    private ConfigurableEnvironment env;

    private RelaxedPropertyResolver propertyResolver;

    public ServerConfiguration(ConfigurableEnvironment env) {
        this.env = env;
        this.propertyResolver = new RelaxedPropertyResolver(env);
    }


    @Bean(name="loadMasterBalance")
    public LoadBalance consistentHashLoadBalance(){
        return new ConsistentHashLoadBalance();
    }

    @Bean(name="loadSlaveBalance")
    public LoadBalance roundRobinLoadBalance(){
        return new RoundRobinLoadBalance();
    }


    @Bean
    public RedisServerMasterCluster redisServerMasterCluster(RedisProxyConfiguration redisProxyConfiguration, @Qualifier("loadMasterBalance") LoadBalance loadMasterBalance, @Qualifier("loadSlaveBalance") LoadBalance loadSlaveBalance){
        List<RedisServerClusterBean> list = new ArrayList<>();

//        RedisPoolConfig poolConfig = new RedisPoolConfig();
//        BeanUtils.copyProperties(redisProxyConfiguration.getRedisPool(),poolConfig);

        RedisProxyPoolConfig poolConfig =redisProxyConfiguration.getRedisPool();

        List<RedisProxyMaster> masters =redisProxyConfiguration.getGroupNode().get(0).getRedisMasters();

        for(RedisProxyMaster master : masters){
            RedisServerBean redisServerBean = new RedisServerBean();
            redisServerBean.setRedisPoolConfig(poolConfig);
            redisServerBean.setHost(master.getHost());
            redisServerBean.setPort(master.getPort());


            RedisServerClusterBean redisServerClusterBean=  new RedisServerClusterBean();
            redisServerClusterBean.setRedisServerMasterBean(redisServerBean);
            redisServerClusterBean.setLoadClusterBalance(loadSlaveBalance);

            //slave
            List<RedisServerBean> slaves = new ArrayList<>();
            for(RedisProxySlave slave :master.getRedisSlaves()){
                RedisServerBean redisSlaveServerBean = new RedisServerBean();
                redisSlaveServerBean.setRedisPoolConfig(poolConfig);
                redisSlaveServerBean.setHost(slave.getHost());
                redisSlaveServerBean.setPort(slave.getPort());
                slaves.add(redisSlaveServerBean);
            }
            redisServerClusterBean.setRedisServerSlaveBeans(slaves);
            list.add(redisServerClusterBean);
        }




        RedisServerMasterCluster redisServerMasterCluster = new RedisServerMasterCluster(list);
        redisServerMasterCluster.setLoadMasterBalance(loadMasterBalance);

        return redisServerMasterCluster;
    }


    //
    @Bean
    public Server server(){
        Server server =  new Server();
        if(server.getPort()==0){
            int nonSecurePort = Integer.valueOf(propertyResolver.getProperty("server.port", propertyResolver.getProperty("port", "8080")));
            server.setPort(nonSecurePort);
        }
       return server;
    }


    @Bean
    @ConditionalOnMissingBean(value = EurekaInstanceConfig.class, search = SearchStrategy.CURRENT)
    public EurekaInstanceConfigBean eurekaInstanceConfigBean(InetUtils inetUtils,Server server) throws MalformedURLException {
        PropertyResolver eurekaPropertyResolver = new RelaxedPropertyResolver(this.env, "eureka.instance.");
        String hostname = eurekaPropertyResolver.getProperty("hostname");

       // boolean preferIpAddress = Boolean.parseBoolean(eurekaPropertyResolver.getProperty("preferIpAddress"));
        int nonSecurePort = Integer.valueOf(propertyResolver.getProperty("server.port", propertyResolver.getProperty("port", "8080")));
        int managementPort = Integer.valueOf(propertyResolver.getProperty("management.port", String.valueOf(nonSecurePort)));
        String managementContextPath = propertyResolver.getProperty("management.contextPath", propertyResolver.getProperty("server.contextPath", "/"));
        EurekaInstanceConfigBean instance = new EurekaInstanceConfigBean(inetUtils);
        instance.setNonSecurePort(nonSecurePort);
        instance.setInstanceId(getDefaultInstanceId(propertyResolver));
        instance.setPreferIpAddress(true);//只注册ip

        if (managementPort != nonSecurePort && managementPort != 0) {
            if (StringUtils.hasText(hostname)) {
                instance.setHostname(hostname);
            }
            String statusPageUrlPath = eurekaPropertyResolver.getProperty("statusPageUrlPath");
            String healthCheckUrlPath = eurekaPropertyResolver.getProperty("healthCheckUrlPath");
            if (!managementContextPath.endsWith("/")) {
                managementContextPath = managementContextPath + "/";
            }
            if (StringUtils.hasText(statusPageUrlPath)) {
                instance.setStatusPageUrlPath(statusPageUrlPath);
            }
            if (StringUtils.hasText(healthCheckUrlPath)) {
                instance.setHealthCheckUrlPath(healthCheckUrlPath);
            }
            String scheme = instance.getSecurePortEnabled() ? "https" : "http";
            URL base = new URL(scheme, instance.getHostname(), managementPort, managementContextPath);
            instance.setStatusPageUrl(new URL(base, StringUtils.trimLeadingCharacter(instance.getStatusPageUrlPath(), '/')).toString());
            instance.setHealthCheckUrl(new URL(base, StringUtils.trimLeadingCharacter(instance.getHealthCheckUrlPath(), '/')).toString());
        }

        //补充metadata

        Map<String,String> metaData = new HashMap<>();
       metaData.put("ip",server.getIp());
       metaData.put("tag",server.getTag());
       metaData.put("hearTime",server.getHeartTime()+"");
       metaData.put("maxCount",server.getMaxCount()+"");
       metaData.put("nowCount",server.getNowCount()+"");
       metaData.put("port",server.getPort()+"");
       metaData.put("isRedisProxy","true");
        metaData.put("redisProxyServiceId",server.getServiceId());
        instance.setMetadataMap(metaData);
        return instance;
    }

//    @Bean
//    public String testClient(DiscoveryClient discoveryClient){
//        discoveryClient.getServices();
//        return "";
//    }
}
