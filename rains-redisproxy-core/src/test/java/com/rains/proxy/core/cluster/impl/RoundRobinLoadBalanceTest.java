package com.rains.proxy.core.cluster.impl;

import com.rains.proxy.core.bean.LBRedisServerMasterCluster;
import com.rains.proxy.core.bean.support.LBRedisServerBean;
import com.rains.proxy.core.cluster.impl.support.RedisQuestBean;
import com.rains.proxy.core.config.RedisProxyMockTest;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.junit.Assert.*;

/**
 * @author dourx
 * @version V1.0
 * @Description: TODO
 * @date 2018年 05 月  23日  15:11
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes=RedisProxyMockTest.class)
public class RoundRobinLoadBalanceTest extends BaseLoadBalanceTest {

    RoundRobinLoadBalance loadBalance;

    @Before
    public void setUp() throws Exception {
        super.setUp();
        loadBalance = new RoundRobinLoadBalance();
        loadBalance.onRefresh(init());
    }

    @Test
    public void onRefresh() {
        LBRedisServerMasterCluster masterCluster = loadBalance.getFfanRedisServerMasterCluster();

        LBRedisServerMasterCluster initMasterCluster = init();
        assertNotNull(masterCluster);
        assertNotSame(masterCluster, initMasterCluster);//断言刚初始化的主集群与loadbalace已存在的集群不一样

        //执行onrefresh后，断言刚初始化的主集群与loadbalace已存在的集群一样
        loadBalance.onRefresh(initMasterCluster);
        masterCluster = loadBalance.getFfanRedisServerMasterCluster();
        assertNotNull(masterCluster);
        assertSame(initMasterCluster,masterCluster);
    }

    @Test
    public void select() {
        //set mykey myvalue
        RedisQuestBean redisQuestBean=new RedisQuestBean(new String(request.getArgs().get(0)), request.getArgs().get(1),true );

        LBRedisServerBean serverBean = loadBalance.select(redisQuestBean,null);
        LBRedisServerBean serverBean1 = loadBalance.select(redisQuestBean,null);
        LBRedisServerBean serverBean2 = loadBalance.select(redisQuestBean,null);


        assertNotNull(serverBean);
        assertNotNull(serverBean1);
        assertNotNull(serverBean2);

        LBRedisServerMasterCluster masterCluster = loadBalance.getFfanRedisServerMasterCluster();
        List<LBRedisServerBean> masters =  masterCluster.getMasters();

        assertTrue(masters.contains(serverBean));
        assertTrue(masters.contains(serverBean1));
        assertTrue(masters.contains(serverBean2));

    }

    @Test
    public void selectSlave() {
        RedisQuestBean redisQuestBean=new RedisQuestBean(new String(request.getArgs().get(0)), request.getArgs().get(1),false );


        loadBalance.setFfanRedisServerMasterCluster(init());
        LBRedisServerBean redisServerBean= loadBalance.getFfanRedisServerMasterCluster().getMasters().get(0);


        LBRedisServerBean serverBean = loadBalance.select(redisQuestBean,redisServerBean);

        List<LBRedisServerBean> slaves=loadBalance.getFfanRedisServerMasterCluster().getMasterFfanRedisServerBean(redisServerBean.getKey());

        assertNotNull(serverBean);
        assertTrue(slaves.contains(serverBean));


    }





}