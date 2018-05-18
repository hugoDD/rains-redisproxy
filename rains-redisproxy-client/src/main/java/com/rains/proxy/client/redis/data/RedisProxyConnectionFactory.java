package com.rains.proxy.client.redis.data;

import com.netflix.loadbalancer.ILoadBalancer;
import com.netflix.loadbalancer.Server;
import com.rains.proxy.client.redis.RedsiProxyException;
import com.rains.proxy.client.ribbon.RibbonRedisProxyClient;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.cloud.netflix.ribbon.SpringClientFactory;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.util.Pool;

import java.util.*;

public class RedisProxyConnectionFactory extends JedisConnectionFactory {
    private static final Logger logger = LoggerFactory.getLogger(RedisProxyConnectionFactory.class);
    private final static String REDISPROXYSERVICENAME = "redisProxy";
    private RibbonRedisProxyClient loadBalancerClient;


    private String serviceId =REDISPROXYSERVICENAME;

   private RedisProperties properties;



    public RedisProxyConnectionFactory(SpringClientFactory springClientFactory, RedisProperties properties) {
        this.properties = properties;
        this.serviceId = StringUtils.isNotEmpty(properties.getHost())?properties.getHost():serviceId;

        ILoadBalancer loadBalancer =springClientFactory.getLoadBalancer(serviceId);

        if(loadBalancer==null){
            logger.error("serviceId : {} not fetch loadBalancer,sure redisProxy service is ok!",serviceId);
            throw  new RedsiProxyException("serviceId : "+serviceId +" not fetch loadBalancer");
        }

        this.loadBalancerClient = new RibbonRedisProxyClient(springClientFactory.getLoadBalancer(serviceId));

    }

    @Override
    public RedisConnection getConnection() {

        return super.getConnection();
    }

    @Override
    protected Jedis fetchJedisConnector() {
        Map<String,Pool<Jedis>> poolMap = createPoolList();

        if(!poolMap.isEmpty()){

           return loadBalancerClient.callWithRibbon(poolMap);
        }

        return super.fetchJedisConnector();
    }

    /**
     * 初始化多个redis pool,并缓存起来
     * @return
     */
    private  Map<String,Pool<Jedis>> createPoolList(){
      List<Server> servers=  getAllRedisProxyServers();
      Map<String,Pool<Jedis>> poolMap = new HashMap<>();

      if(servers==null){
           return  poolMap;
      }
        super.setPoolConfig(jedisPoolConfig());
      for(Server s : servers){
          if(poolMap.containsKey(s.getHost())){
              continue;
          }
          super.setHostName(s.getHost());
          super.setPort(s.getPort());

          poolMap.put(s.getId(),super.createRedisPool());
      }

      return poolMap;
    }

    /**
     * 初始化jedispool参数
     * @return jedisPoolConfig
     */
    private JedisPoolConfig jedisPoolConfig() {
        JedisPoolConfig config = new JedisPoolConfig();
        RedisProperties.Pool props = this.properties.getPool();
        config.setMaxTotal(props.getMaxActive());
        config.setMaxIdle(props.getMaxIdle());
        config.setMinIdle(props.getMinIdle());
        config.setMaxWaitMillis(props.getMaxWait());
        return config;
    }

    /**
     * 从注册中心获取所有redisProxy的服务
     * @return
     */
    private  List<Server> getAllRedisProxyServers(){
       List<Server> servers = loadBalancerClient.getAllServers();
       if(servers==null){
           return Collections.emptyList();
       }
       return servers;
    }


}
