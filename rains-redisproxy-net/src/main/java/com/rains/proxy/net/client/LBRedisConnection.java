package com.rains.proxy.net.client;


import com.rains.proxy.core.command.impl.RedisCommand;
import com.rains.proxy.core.connection.IConnection;
import com.rains.proxy.core.constants.LBRedisProxyErrorMsgConstant;
import com.rains.proxy.core.enums.ChannelState;
import com.rains.proxy.core.exception.LBRedisProxyFrameworkException;
import com.rains.proxy.core.pool.commons.LBRedisProxyPoolConfig;
import com.rains.proxy.core.protocol.RedisReplyDecoder;
import com.rains.proxy.core.protocol.RedisRequestEncoder;
import com.rains.proxy.core.reply.IRedisReply;
import com.rains.proxy.net.client.suppot.LBRedisClientOutHandler;
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
 * 
 * @author liubing
 *
 */
public class LBRedisConnection implements IConnection {
	
	private Logger logger = LoggerFactory.getLogger(LBRedisConnection.class);
	
	private volatile ChannelState state = ChannelState.UNINIT;

	private Channel backChannel = null;

	private LBRedisProxyPoolConfig lbRedisProxyPoolConfig;
	
	private Bootstrap bootstrap;
	
	private ChannelHandlerContext frontCtx;

	/**
	 * for test
	 */
	public LBRedisConnection() {
	}

	/**
	 * @param lbRedisProxyPoolConfig
	 */
	public LBRedisConnection(LBRedisProxyPoolConfig lbRedisProxyPoolConfig) {
		super();
		this.lbRedisProxyPoolConfig = lbRedisProxyPoolConfig;
		initClientBootstrap();
		open();
	}
	
	/**
	 * 初始化 bootstrap
	 */
	private void initClientBootstrap() {
		bootstrap = new Bootstrap();

        NioEventLoopGroup group = new NioEventLoopGroup(Runtime.getRuntime().availableProcessors());
        bootstrap.group(group).channel(NioSocketChannel.class).handler(new  ChannelInitializer<SocketChannel>() {
			@Override
			protected void initChannel(SocketChannel ch) throws Exception {
				ch.pipeline().addLast("RedisReplyDecoder",new RedisReplyDecoder());
				ch.pipeline().addLast("RedisRequestEncoder",new RedisRequestEncoder());
				ch.pipeline().addLast("ClientInHandler",new LBRedisClientInHandler());
				ch.pipeline().addLast("ClientOutHandler",new LBRedisClientOutHandler());
			}
        	
        });
        bootstrap.option(ChannelOption.TCP_NODELAY, true);
        bootstrap.option(ChannelOption.SO_KEEPALIVE, true);
		/* 实际上，极端情况下，connectTimeout会达到500ms，因为netty nio的实现中，是依赖BossThread来控制超时，
         如果为了严格意义的timeout，那么需要应用端进行控制。
		 */
        int timeout = lbRedisProxyPoolConfig.getConnectionTimeout();
        if (timeout <= 0) {
            throw new LBRedisProxyFrameworkException("NettyClient init Error: timeout(" + timeout + ") <= 0 is forbid.",
                    LBRedisProxyErrorMsgConstant.FRAMEWORK_INIT_ERROR);
        }
        bootstrap.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, timeout);
        open();
	}
	
	/**
	 * 发送消息
	 */
	public void write(final RedisCommand request, ChannelHandlerContext frontCtx) {
		if(!isAvailable()){
			open();
		}
		this.frontCtx=frontCtx;
		backChannel.writeAndFlush(request);
		
	}
	
	@Override
	public boolean open() {
		
		try {
			ChannelFuture channelFuture = bootstrap.connect(new InetSocketAddress(lbRedisProxyPoolConfig.getHost(),lbRedisProxyPoolConfig.getPort()));

			long start = System.currentTimeMillis();

			int timeout = lbRedisProxyPoolConfig.getConnectionTimeout();
			if (timeout <= 0) {
	            throw new LBRedisProxyFrameworkException("Netty4Client init Error: timeout(" + timeout + ") <= 0 is forbid.",
	                    LBRedisProxyErrorMsgConstant.FRAMEWORK_INIT_ERROR);
			}
			// 不去依赖于connectTimeout
			boolean result = channelFuture.awaitUninterruptibly(timeout, TimeUnit.MILLISECONDS);
            boolean success = channelFuture.isSuccess();

			if (result && success) {
				backChannel = channelFuture.channel();
				state = ChannelState.ALIVE;
				return true;
			}
            boolean connected = false;
            if(channelFuture.channel() != null){
                connected = channelFuture.channel().isOpen();
            }

			if (channelFuture.cause() != null) {
				channelFuture.cancel(true);
				throw new LBRedisProxyFrameworkException("NettyChannel failed to connect to server,,HOST:"+lbRedisProxyPoolConfig.getHost()+",port:"+lbRedisProxyPoolConfig.getPort()+ ", result: " + result + ", success: " + success + ", connected: " + connected, channelFuture.cause());
			} else {
				channelFuture.cancel(true);
                throw new LBRedisProxyFrameworkException("NettyChannel connect to server timeout ,HOST:"+lbRedisProxyPoolConfig.getHost()+",port:"+lbRedisProxyPoolConfig.getPort()+ ", cost: " + (System.currentTimeMillis() - start) + ", result: " + result + ", success: " + success + ", connected: " + connected);
            }
		} catch (LBRedisProxyFrameworkException e) {
			throw e;
		} catch (Exception e) {
			throw new LBRedisProxyFrameworkException("NettyChannel failed to connect to server ", e);
		}
	}

	@Override
	public void close() {
		close(0);
	}

	@Override
	public void close(int timeout) {
		try {
			if(state.isAliveState()){
				if (backChannel != null&&backChannel.isOpen()) {
					backChannel.close();
				}
				if (frontCtx != null&&frontCtx.channel().isOpen()) {
					frontCtx.close();
				}
				state = ChannelState.CLOSE;
				
			}
			
		} catch (Exception e) {
			logger.error("NettyChannel close Error,HOST:"+lbRedisProxyPoolConfig.getHost()+",port:"+lbRedisProxyPoolConfig.getPort(), e);
		}
	}

	@Override
	public boolean isClosed() {
		return state.isCloseState();
	}

	@Override
	public boolean isAvailable() {
		if(backChannel!=null){//判断通道状态，防止通道假活
			return state.isAliveState()&&backChannel.isActive();
		}
		return state.isAliveState();
	}

	public  LBRedisClientInHandler getRedisClientInHandler(){
		return new LBRedisClientInHandler();
	}

	/**
	 * 目标服务器写入客户端通道
	 * @author liubing
	 *
	 */
	private class LBRedisClientInHandler extends SimpleChannelInboundHandler<IRedisReply> {
		

		public LBRedisClientInHandler() {
			super();
		}
		
		@Override
		protected void channelRead0(ChannelHandlerContext ctx, final IRedisReply msg)
				throws Exception {
			logger.info("read");
			frontCtx.writeAndFlush(msg, frontCtx.channel().voidPromise());

			// Always write from the event loop, minimize the wakeup events
//			frontCtx.channel().eventLoop().execute(new Runnable() {
//
//		        @Override
//		        public void run() {
//		          // Not interested in the channel promise
//
//		        	frontCtx.writeAndFlush(msg, frontCtx.channel().voidPromise());
//		        }
//		      });

		}
		
		@Override
		public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
				throws Exception {
			
			super.exceptionCaught(ctx, cause);
		}
	}
}
