package com.rains.proxy.net;

import com.netflix.appinfo.EurekaInstanceConfig;
import com.netflix.appinfo.HealthCheckHandler;
import com.netflix.discovery.EurekaClientConfig;
import com.rains.proxy.net.model.Server;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.*;
import org.springframework.boot.bind.RelaxedPropertyResolver;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cglib.beans.BeanMap;
import org.springframework.cloud.client.CommonsClientAutoConfiguration;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.discovery.noop.NoopDiscoveryClientAutoConfiguration;
import org.springframework.cloud.client.serviceregistry.ServiceRegistryAutoConfiguration;
import org.springframework.cloud.commons.util.InetUtils;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.eureka.EurekaDiscoveryClientConfiguration;
import org.springframework.cloud.netflix.eureka.EurekaInstanceConfigBean;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.PropertyResolver;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.StringUtils;


import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

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
