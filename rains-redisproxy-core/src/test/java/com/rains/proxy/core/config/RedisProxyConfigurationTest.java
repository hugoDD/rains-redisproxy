package com.rains.proxy.core.config;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.junit.Assert.*;

/**
 * @author dourx
 * @version V1.0
 * @Description: TODO
 * @date 2018年 05 月  18日  10:56
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes=RedisProxyMockTest.class)
public class RedisProxyConfigurationTest {

    @Autowired
    private RedisProxyConfiguration redisProxyConfiguration;
    RedisProxyPool poolConfig;

    @Before
    public void setUp() throws Exception {
        poolConfig= redisProxyConfiguration.getRedisPool();
        assertNotNull(poolConfig);
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void getRedisPool() {

       assertEquals(5000,poolConfig.getConnectionTimeout());
       assertEquals(100,poolConfig.getMaxActiveConnection());
       assertEquals(80,poolConfig.getMaxIdleConnection());
       assertEquals(3,poolConfig.getMinConnection());
       assertEquals(500,poolConfig.getMaxWaitMillisOnBorrow());
       assertEquals(5,poolConfig.getInitialConnection());
       assertEquals(60000,poolConfig.getTimeBetweenEvictionRunsMillis());
       assertEquals(300000,poolConfig.getMinEvictableIdleTimeMillis());
       assertEquals(1,poolConfig.getMinIdleEntries());
       assertEquals(true,poolConfig.isTestOnBorrow());
       assertEquals(true,poolConfig.isTestOnReturn());
       assertEquals(true,poolConfig.isTestWhileIdle());



    }


//    redisMasters :
//            -  host: 172.26.223.109
//    port: 16379
//    redisSlaves:
//            - host: 172.26.223.110
//    port: 26379
//            - host: 172.26.223.111
//    port: 36379
//            -  host: 172.26.223.108
//    port: 16379
//    redisSlaves:
//            - host: 172.26.223.110
//    port: 26379
//            - host: 172.26.223.111
//    port: 36379
    @Test
    public void getNode() {
       List<RedisProxyGroupNode> node= redisProxyConfiguration.getGroupNode();
        assertNotNull(node);
        assertEquals(1,node.size());
        RedisProxyGroupNode groupNode= node.get(0);
        assertEquals(groupNode.getRedisMasters().size(),2);
        assertEquals(groupNode.getRedisMasters().get(0).getRedisSlaves().size(),2);
        assertEquals(groupNode.getRedisMasters().get(1).getRedisSlaves().size(),2);


    }


}