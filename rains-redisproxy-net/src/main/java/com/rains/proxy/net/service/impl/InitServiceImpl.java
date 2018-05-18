package com.rains.proxy.net.service.impl;

import com.rains.proxy.net.model.Server;
import com.rains.proxy.net.service.NettyServerService;
import com.rains.proxy.net.service.InitService;
import com.rains.proxy.net.utils.SocketManager;
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
    private NettyServerService nettyServerService;

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
        nettyServerService.start();
        SocketManager.getInstance().setMaxConnection(serverConfig.getMaxCount());
    }


    @Override
    public void close() {
        nettyServerService.close();
    }
}
