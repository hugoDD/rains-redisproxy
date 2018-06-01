package com.rains.proxy.net.service.impl;

import com.rains.proxy.core.bean.LBRedisServerMasterCluster;
import com.rains.proxy.net.model.Server;
import com.rains.proxy.net.server.LBRedisServer;
import com.rains.proxy.net.service.InitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

/**
 * create by lorne on 2017/9/20
 */
@Service
@Lazy(false)
public class InitServiceImpl implements InitService {

    @Autowired
    private LBRedisServerMasterCluster redisServerMasterCluster;

//    @Autowired
//    private NettyServerService nettyServerService;

   private LBRedisServer ffanRedisServer;

    @Autowired
    private Server serverConfig;

    @PostConstruct
    public void init(){
        start();
    }

    @PreDestroy
    public void destroy(){
        close();
    }


    @Override
    public void start() {
//        nettyServerService.start();
//        SocketManager.getInstance().setMaxConnection(serverConfig.getMaxCount());

         ffanRedisServer=new LBRedisServer(redisServerMasterCluster);

        ffanRedisServer.start();
    }


    @Override
    public void close() {
        ffanRedisServer.destroy();
       // nettyServerService.close();
    }
}
