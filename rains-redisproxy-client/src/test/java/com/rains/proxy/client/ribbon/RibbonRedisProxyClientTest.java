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
package com.rains.proxy.client.ribbon;

import com.netflix.client.ClientException;
import com.netflix.loadbalancer.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisClusterConnection;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisSentinelConnection;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.StringUtils;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisShardInfo;
import redis.clients.jedis.exceptions.JedisConnectionException;
import redis.clients.util.Pool;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * @author dourx
 * @version V1.0
 * @Description: redisProxy使用ribbon的负载客户端
 * @date 2018/5/18  8:57
 */
public class RibbonRedisProxyClientTest {
    public static Logger logger = LoggerFactory.getLogger(RibbonRedisProxyClientTest.class);
    RibbonRedisProxyClient ribbonRedisProxyClient;
    @Before
    public void setUp() throws Exception {
        List<Server> serverList = new ArrayList<Server>();
        serverList.add(new Server("127.0.0.1", 8082));
        serverList.add(new Server("127.0.0.1", 8083));
        ILoadBalancer  loadBalancer = LoadBalancerBuilder.newBuilder().buildFixedServerListLoadBalancer(serverList);
        ribbonRedisProxyClient = new RibbonRedisProxyClient(loadBalancer);
    }



    @Test(expected = RuntimeException.class)
    public void callWithRibbon() {
        Map<String,Pool<Jedis>> poolMap = new HashMap<>();
        List<Server> serverList=  ribbonRedisProxyClient.getAllServers();
        MockJedisConnectionFactory factory =new  MockJedisConnectionFactory();
        for(Server s : serverList){
            if(poolMap.containsKey(s.getHost())){
                continue;
            }
            factory.setHostName(s.getHost());
            factory.setPort(s.getPort());

            poolMap.put(s.getId(),factory.mockCreatePool());
        }

       Jedis jedis= ribbonRedisProxyClient.callWithRibbon(poolMap);
    }


    @Test
    public void getAllServers() {
      List<Server> serverList=  ribbonRedisProxyClient.getAllServers();
      assertNotNull(serverList);
      assertEquals(serverList.size(),2);
      assertEquals(serverList.get(0).getPort(),8082);
      assertEquals(serverList.get(1).getPort(),8083);
    }

    @Test
    public void getLoadBalancerStats() {
      LoadBalancerStats loadBalancerStats= ribbonRedisProxyClient.getLoadBalancerStats();
      assertNotNull(loadBalancerStats);
      List<Server> serverList=  ribbonRedisProxyClient.getAllServers();
        for(Server server : serverList) {

            ServerStats serverStats = loadBalancerStats.getSingleServerStat(server);
            assertNotNull(serverStats);
            logger.info("serverStats: " + serverStats);
            logger.info("server: " + server.toString());
            logger.info("isCircuitBreakerTripped: " + serverStats.isCircuitBreakerTripped());
        }
    }

    class MockJedisConnectionFactory extends  JedisConnectionFactory{


        public Pool<Jedis> mockCreatePool(){
            if (getShardInfo() == null) {
                JedisShardInfo shardInfo = new JedisShardInfo(getHostName(),getPort());
                super.setShardInfo(shardInfo);
            }
            return super.createRedisPool();
        }

        @Override
        public JedisPoolConfig getPoolConfig() {
            return jedisPoolConfig() ;
        }

        /**
         * 初始化jedispool参数
         * @return jedisPoolConfig
         */
        private JedisPoolConfig jedisPoolConfig() {
            JedisPoolConfig config = new JedisPoolConfig();

            config.setMaxTotal(10);
            config.setMaxIdle(3);
            config.setMinIdle(3);
            config.setMaxWaitMillis(600000);
            return config;
        }
    }
}