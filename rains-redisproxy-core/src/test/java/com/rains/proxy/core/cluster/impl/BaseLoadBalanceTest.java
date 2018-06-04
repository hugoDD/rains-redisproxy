package com.rains.proxy.core.cluster.impl;

import com.rains.proxy.core.bean.LBRedisServerMasterCluster;
import com.rains.proxy.core.bean.RedisPoolConfig;
import com.rains.proxy.core.bean.support.LBRedisServerBean;
import com.rains.proxy.core.bean.support.LBRedisServerClusterBean;
import com.rains.proxy.core.command.impl.RedisCommand;
import com.rains.proxy.core.config.RedisProxyConfiguration;
import com.rains.proxy.core.config.RedisProxyMaster;
import com.rains.proxy.core.config.RedisProxySlave;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import org.junit.Before;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

/**
 * @author dourx
 * @version V1.0
 * @Description: TODO
 * @date 2018年 05 月  23日  15:13
 */
public class BaseLoadBalanceTest {

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

    protected LBRedisServerMasterCluster init(){

        List<LBRedisServerClusterBean> list = new ArrayList<>();

        RedisPoolConfig poolConfig = new RedisPoolConfig();
        BeanUtils.copyProperties(redisProxyConfiguration.getRedisPool(),poolConfig);

        List<RedisProxyMaster> masters =redisProxyConfiguration.getGroupNode().get(0).getRedisMasters();

        for(RedisProxyMaster master : masters){
            LBRedisServerBean redisServerBean = new LBRedisServerBean();
            redisServerBean.setRedisPoolConfig(poolConfig);
            redisServerBean.setHost(master.getHost());
            redisServerBean.setPort(master.getPort());

            LBRedisServerClusterBean redisServerClusterBean=  new LBRedisServerClusterBean();
            redisServerClusterBean.setRedisServerMasterBean(redisServerBean);

            //slave
            List<LBRedisServerBean> slaves = new ArrayList<>();
            for(RedisProxySlave slave :master.getRedisSlaves()){
                LBRedisServerBean redisSlaveServerBean = new LBRedisServerBean();
                redisSlaveServerBean.setRedisPoolConfig(poolConfig);
                redisSlaveServerBean.setHost(slave.getHost());
                redisSlaveServerBean.setPort(slave.getPort());
                slaves.add(redisSlaveServerBean);
            }
            redisServerClusterBean.setRedisServerSlaveBeans(slaves);
            list.add(redisServerClusterBean);
        }




        LBRedisServerMasterCluster redisServerMasterCluster = new LBRedisServerMasterCluster(list);

        return redisServerMasterCluster;

    }
}
