package com.rains.proxy.net.service.impl;

import com.rains.proxy.net.model.Server;
import com.rains.proxy.net.service.NettyServerService;
import com.rains.proxy.net.service.SocketService;
import com.rains.proxy.net.socket.SocketServerChannelInitializer;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

/**
 * Created by lorne on 2017/4/12.
 */
@Service
public class NettyServerServiceImpl implements NettyServerService {

    @Autowired
    private Server nettyServerConfig;




    private EventLoopGroup bossGroup;
    private EventLoopGroup workerGroup;

    private ServerBootstrap b;




    private Logger logger = LoggerFactory.getLogger(NettyServerServiceImpl.class);




    @Override
    public synchronized void start() {
        bossGroup = new NioEventLoopGroup(); // (1)
        workerGroup = new NioEventLoopGroup();
        try {
            b = new ServerBootstrap(); // (2)
            b.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG, 100)
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .childHandler(new SocketServerChannelInitializer(nettyServerConfig.getHeartTime(),null,null));
            // Bind and start to accept incoming connections.
            b.bind(nettyServerConfig.getPort());

            logger.info("socket: "+nettyServerConfig.getPort()+" starting....");
            // Wait until the server socket is closed.
            // In this example, this does not happen, but you can do that to gracefully
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
        }
    }

    public synchronized void close() {
        if (workerGroup != null)
            workerGroup.shutdownGracefully();
        if (bossGroup != null)
            bossGroup.shutdownGracefully();
        logger.info("socket closing....");
    }

}

