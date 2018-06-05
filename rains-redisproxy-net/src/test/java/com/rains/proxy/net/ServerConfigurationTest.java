package com.rains.proxy.net;

import com.rains.proxy.core.bean.RedisServerMasterCluster;
import com.rains.proxy.core.bean.support.RedisServerBean;
import com.rains.proxy.core.cluster.LoadBalance;
import com.rains.proxy.core.cluster.impl.ConsistentHashLoadBalance;
import com.rains.proxy.net.model.Server;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

import java.util.List;

import static org.junit.Assert.*;

/**
 * @author dourx
 * @version V1.0
 * @Description: TODO
 * @date 2018年 05 月  23日  17:15
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes=RedisProxyConfigurationTest.class)
public class ServerConfigurationTest {

    @Resource(name = "loadMasterBalance")
    private LoadBalance loadMasterBalance;

     @Resource(name = "loadSlaveBalance")
    private LoadBalance loadSlaveBalance;

     @Autowired
    private Server server;

     @Autowired
    private RedisServerMasterCluster redisServerMasterCluster;




    @Test
    public void consistentHashLoadBalance() {
        assertNotNull(loadMasterBalance);
        assertTrue(loadMasterBalance instanceof ConsistentHashLoadBalance);
    }

    @Test
    public void roundRobinLoadBalance() {
        assertNotNull(loadMasterBalance);
        assertTrue(loadMasterBalance instanceof ConsistentHashLoadBalance);
    }

    @Test
    public void redisServerMasterCluster() {
        assertNotNull(redisServerMasterCluster);
       List<RedisServerBean> masters = redisServerMasterCluster.getMasters();
        LoadBalance loadBalance = redisServerMasterCluster.getLoadMasterBalance();
        List<RedisServerBean> slaves = redisServerMasterCluster.getMasterFfanRedisServerBean(masters.get(0).getKey());

       assertEquals(2,masters.size());
       assertEquals(2,slaves.size());
       assertTrue(loadBalance instanceof  ConsistentHashLoadBalance);



    }

    @Test
    public void server() {
        assertNotNull(server);
        assertEquals(8092,server.getPort());
        assertEquals(10,server.getHeartTime());
        assertEquals(1000,server.getMaxCount());
        assertEquals("127.0.0.1",server.getIp());
        assertEquals("dev",server.getTag());
        assertEquals("redisProxy",server.getServiceId());
        assertEquals("dev",server.getTag());

    }


}