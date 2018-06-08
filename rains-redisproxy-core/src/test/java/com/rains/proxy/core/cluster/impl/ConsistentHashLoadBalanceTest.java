package com.rains.proxy.core.cluster.impl;

import com.rains.proxy.core.bean.RedisServerMasterCluster;
import com.rains.proxy.core.bean.support.RedisServerBean;
import com.rains.proxy.core.bean.support.RedisServerClusterBean;
import com.rains.proxy.core.cluster.impl.support.RedisQuestBean;
import com.rains.proxy.core.command.impl.RedisCommand;
import com.rains.proxy.core.config.*;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * @author dourx
 * @version V1.0
 * @Description: TODO
 * @date 2018年 05 月  22日  9:40
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes=RedisProxyMockTest.class)
public class ConsistentHashLoadBalanceTest {

    @Autowired
    private RedisProxyConfiguration redisProxyConfiguration;

    RedisCommand request;
    RedisCommand requestSlave;
    @Before
    public void setUp() throws Exception {
        ByteBuf buf = Unpooled.buffer(1024);;
        ByteBuf slaveBuf = Unpooled.buffer(1024);;
        request = new RedisCommand();
        request.setArgCount(3);
        List<byte[]> args = new ArrayList<>();
        args.add("set".getBytes());
        args.add("mykey".getBytes());
        args.add("myvalue".getBytes());
        request.setArgs(args);

        //get mykey
        requestSlave = new RedisCommand();
        requestSlave.setArgCount(2);
        List<byte[]> slaveArgs = new ArrayList<>();
        args.add("get".getBytes());
        args.add("mykey".getBytes());
        requestSlave.setArgs(slaveArgs);



        request.encode(buf);

        requestSlave.encode(slaveBuf);
    }

    private RedisServerMasterCluster init(){

        List<RedisServerClusterBean> list = new ArrayList<>();

        RedisProxyPool poolConfig =redisProxyConfiguration.getRedisPool();
       // BeanUtils.copyProperties(redisProxyConfiguration.getRedisPool(),poolConfig);

       List<RedisProxyMaster> masters =redisProxyConfiguration.getGroupNode().get(0).getRedisMasters();

       for(RedisProxyMaster master : masters){
           RedisServerBean redisServerBean = new RedisServerBean();
           redisServerBean.setRedisPoolConfig(poolConfig);
           redisServerBean.setHost(master.getHost());
           redisServerBean.setPort(master.getPort());

           RedisServerClusterBean redisServerClusterBean=  new RedisServerClusterBean();
           redisServerClusterBean.setRedisServerMasterBean(redisServerBean);

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

        return redisServerMasterCluster;

    }

    @After
    public void tearDown() throws Exception {
    }


    @Test
    public void testRedisQuestBean(){
        //set mykey myvalue
        RedisQuestBean redisQuestBean=new RedisQuestBean(new String(request.getArgs().get(0)), request.getArgs().get(1),true );

        assertEquals("set",redisQuestBean.getCommand());
        assertEquals("mykey",new String(redisQuestBean.getKey()));
        assertTrue(redisQuestBean.isWrite());

    }

    @Test
    public void doSelect() {
        ConsistentHashLoadBalance loadBalance =new   ConsistentHashLoadBalance();
        RedisQuestBean redisQuestBean=new RedisQuestBean(new String(request.getArgs().get(0)), request.getArgs().get(1),true );


        loadBalance.setFfanRedisServerMasterCluster(init());

        RedisServerBean serverBean = loadBalance.select(redisQuestBean,null);
        RedisServerBean serverBean1 = loadBalance.select(redisQuestBean,null);
        RedisServerBean serverBean2 = loadBalance.select(redisQuestBean,null);

        assertNotNull(serverBean);
        assertNotNull(serverBean1);
        assertNotNull(serverBean2);

        assertSame(serverBean,serverBean1);
        assertSame(serverBean,serverBean2);
        assertSame(serverBean1,serverBean2);


    }


    @Test
    public void doSlaveSelect() {
        ConsistentHashLoadBalance loadBalance =new   ConsistentHashLoadBalance();
        RedisQuestBean redisQuestBean=new RedisQuestBean(new String(request.getArgs().get(0)), request.getArgs().get(1),false );


        loadBalance.setFfanRedisServerMasterCluster(init());
       RedisServerBean redisServerBean= loadBalance.getFfanRedisServerMasterCluster().getMasters().get(0);


        RedisServerBean serverBean = loadBalance.select(redisQuestBean,redisServerBean);

        List<RedisServerBean> slaves=loadBalance.getFfanRedisServerMasterCluster().getMasterFfanRedisServerBean(redisServerBean.getKey());

        assertNotNull(serverBean);
        assertTrue(slaves.contains(serverBean));


    }
}