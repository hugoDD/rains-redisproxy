/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.rains.proxy.net.client;


import com.rains.proxy.core.command.impl.RedisCommand;
import com.rains.proxy.core.command.impl.ReidisAuthCommand;
import com.rains.proxy.core.connection.IConnection;
import com.rains.proxy.core.constants.RedisProxyErrorMsgConstant;
import com.rains.proxy.core.enums.ChannelState;
import com.rains.proxy.core.exception.RedisProxyFrameworkException;
import com.rains.proxy.core.protocol.RedisReplyDecoder;
import com.rains.proxy.core.protocol.RedisRequestEncoder;
import com.rains.proxy.core.reply.IRedisReply;
import com.rains.proxy.net.client.suppot.RedisClientOutHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.util.concurrent.TimeUnit;

/**
 * @author dourx
 * @version V1.0
 * 创建日期 2018/6/4
 *
 * redis客户端连接
 *
 */
public class RedisConnection implements IConnection {

    private Logger logger = LoggerFactory.getLogger(RedisConnection.class);

    private volatile ChannelState state = ChannelState.UNINIT;


    private Channel backChannel = null;


    private Bootstrap bootstrap;

    private ChannelHandlerContext frontCtx;

    private String host = "127.0.0.1";

    private int port = 6379;

    private int timeout = 0;


    public RedisConnection(String host, int port, int timeout) {
        super();
        this.host = host;
        this.port = port;
        this.timeout = timeout;
        initClientBootstrap();
        open();
    }

    /**
     * 初始化 bootstrap
     */
    private void initClientBootstrap() {
        bootstrap = new Bootstrap();

        NioEventLoopGroup group = new NioEventLoopGroup(Runtime.getRuntime().availableProcessors());
        bootstrap.group(group).channel(NioSocketChannel.class).handler(new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel ch) {
                ch.pipeline().addLast("RedisReplyDecoder", new RedisReplyDecoder());
                ch.pipeline().addLast("RedisRequestEncoder", new RedisRequestEncoder());
                ch.pipeline().addLast("ClientInHandler", new RedisClientInHandler());
               // ch.pipeline().addLast("ClientOutHandler", new RedisClientOutHandler());
            }

        });
        bootstrap.option(ChannelOption.TCP_NODELAY, true);
        bootstrap.option(ChannelOption.SO_KEEPALIVE, true);
		/* 实际上，极端情况下，connectTimeout会达到500ms，因为netty nio的实现中，是依赖BossThread来控制超时，
         如果为了严格意义的timeout，那么需要应用端进行控制。
		 */
//        int timeout = redisProxyPoolConfig.getConnectionTimeout();
        if (timeout <= 0) {
            throw new RedisProxyFrameworkException("NettyClient init Error: timeout(" + timeout + ") <= 0 is forbid.",
                    RedisProxyErrorMsgConstant.FRAMEWORK_INIT_ERROR);
        }
        bootstrap.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, timeout);
        open();
    }

    /**
     * 发送消息
     */
    @Override
    public void write(final RedisCommand request, ChannelHandlerContext frontCtx) {
        if (!isAvailable()) {
            open();
        }
        this.frontCtx = frontCtx;
        backChannel.writeAndFlush(request);

    }

    @Override
    public boolean open() {

        try {
            ChannelFuture channelFuture = bootstrap.connect(new InetSocketAddress(host, port));

            long start = System.currentTimeMillis();

//			int timeout = redisProxyPoolConfig.getConnectionTimeout();
            if (timeout <= 0) {
                throw new RedisProxyFrameworkException("Netty4Client init Error: timeout(" + timeout + ") <= 0 is forbid.",
                        RedisProxyErrorMsgConstant.FRAMEWORK_INIT_ERROR);
            }
            // 不去依赖于connectTimeout
            boolean result = channelFuture.awaitUninterruptibly(timeout, TimeUnit.MILLISECONDS);
            boolean success = channelFuture.isSuccess();

            if (result && success) {
                backChannel = channelFuture.channel();
                // todo 加入登录密码
                state = ChannelState.ALIVE;
                return true;
            }
            boolean connected = false;
            if (channelFuture.channel() != null) {
                connected = channelFuture.channel().isOpen();
            }

            if (channelFuture.cause() != null) {
                channelFuture.cancel(true);
                throw new RedisProxyFrameworkException("NettyChannel failed to connect to server,,HOST:" + host + ",port:" + port + ", result: " + result + ", success: " + success + ", connected: " + connected, channelFuture.cause());
            } else {
                channelFuture.cancel(true);
                throw new RedisProxyFrameworkException("NettyChannel connect to server timeout ,HOST:" + host + ",port:" + port + ", cost: " + (System.currentTimeMillis() - start) + ", result: " + result + ", success: " + success + ", connected: " + connected);
            }
        } catch (RedisProxyFrameworkException e) {
            throw e;
        } catch (Exception e) {
            throw new RedisProxyFrameworkException("NettyChannel failed to connect to server ", e);
        }
    }

    @Override
    public void close() {
        close(0);
    }

    @Override
    public void close(int timeout) {
        try {
            if (state.isAliveState()) {
                if (backChannel != null && backChannel.isOpen()) {
                    backChannel.close();
                }
                if (frontCtx != null && frontCtx.channel().isOpen()) {
                    frontCtx.close();
                }
                state = ChannelState.CLOSE;

            }

        } catch (Exception e) {
            logger.error("NettyChannel close Error,HOST:{},port:{}", host, port, e);
        }
    }

    @Override
    public boolean isClosed() {
        return state.isCloseState();
    }

    @Override
    public boolean isAvailable() {
        if (backChannel != null) {//判断通道状态，防止通道假活
            return state.isAliveState() && backChannel.isActive();
        }
        return state.isAliveState();
    }


    public Channel getBackChannel() {
        return backChannel;
    }

    /**
     * @author dourx
     * @version V1.0
     * 创建日期 2018/6/4
     * 目标服务器写入客户端通道
     */
    private class RedisClientInHandler extends SimpleChannelInboundHandler<IRedisReply> {


        public RedisClientInHandler() {
            super();
        }

        @Override
        protected void channelRead0(ChannelHandlerContext ctx, final IRedisReply msg) {
            if (logger.isDebugEnabled()) {
                logger.debug("RedisClientInHandler");
            }
            if(frontCtx==null){
                return;
            }

            //	frontCtx.writeAndFlush(msg, frontCtx.channel().voidPromise());

            // Always write from the event loop, minimize the wakeup events
            frontCtx.channel().eventLoop().execute(() -> {
                // Not interested in the channel promise
                frontCtx.writeAndFlush(msg, frontCtx.channel().voidPromise());
            });


        }

        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
                throws Exception {
            super.exceptionCaught(ctx, cause);
        }
    }
}
