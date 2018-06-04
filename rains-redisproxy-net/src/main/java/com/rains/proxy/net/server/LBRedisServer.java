/**
 * 
 */
package com.rains.proxy.net.server;


import com.rains.proxy.core.bean.LBRedisServerMasterCluster;
import com.rains.proxy.core.bean.RedisPoolConfig;
import com.rains.proxy.core.bean.support.LBRedisServerBean;
import com.rains.proxy.core.bean.support.LBRedisServerClusterBean;
import com.rains.proxy.core.pool.commons.LBRedisProxyPoolConfig;
import com.rains.proxy.core.protocol.RedisReplyEncoder;
import com.rains.proxy.core.protocol.RedisRequestDecoder;
import com.rains.proxy.net.client.LBRedisClient;
import com.rains.proxy.net.server.support.LBRedisServerHandler;
import com.rains.proxy.net.server.support.RedisServerHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Objects;

/**
 * @author liubing
 *
 */
public class LBRedisServer {
	
	private Logger logger = LoggerFactory.getLogger(LBRedisServer.class);

	private LBRedisServerMasterCluster ffanRedisServerMasterCluster;
	
	protected static final PooledByteBufAllocator BUF_ALLOCATOR = PooledByteBufAllocator.DEFAULT;
	
	// 线程组
	private static EventLoopGroup bossGroup = new NioEventLoopGroup(Runtime
			.getRuntime().availableProcessors());
	private static EventLoopGroup workerGroup = new NioEventLoopGroup(Runtime
			.getRuntime().availableProcessors());

	/**
	 *
	 * @author dourx
	 * @date 2018/5/23 19:10
	 * @param   ffanRedisServerMasterCluster 主机集群
	 * @return
	 */
	public LBRedisServer(LBRedisServerMasterCluster ffanRedisServerMasterCluster) {
		super();
		this.ffanRedisServerMasterCluster = ffanRedisServerMasterCluster;
		init();
	}
	
	 
	/**
	   * 销废
	   */
	 public void destroy(){
		  for(String key:ffanRedisServerMasterCluster.getRedisClientBeanMap().keySet()){
			  ffanRedisServerMasterCluster.getRedisClientBeanMap().get(key).close();
		  }
	  }
	  /**
	   * 初始化客户端
	   */
	  private void init(){
		  if(ffanRedisServerMasterCluster!=null&&ffanRedisServerMasterCluster.getRedisServerClusterBeans()!=null&&ffanRedisServerMasterCluster.getRedisServerClusterBeans().size()>0){
			  for(LBRedisServerClusterBean ffanRedisServerClusterBean:ffanRedisServerMasterCluster.getRedisServerClusterBeans()){
				  LBRedisServerBean lbRedisServerBean=ffanRedisServerClusterBean.getRedisServerMasterBean();
				  if(lbRedisServerBean!=null){//主
					  LBRedisProxyPoolConfig lbRedisProxyPoolConfig=convertLBRedisProxyPoolConfig(lbRedisServerBean);
					  LBRedisClient ffanRedisClient=new LBRedisClient(lbRedisProxyPoolConfig);
					  ffanRedisServerMasterCluster.getRedisClientBeanMap().put(lbRedisServerBean.getKey(), ffanRedisClient);
				  }
				  List<LBRedisServerBean> ffanRedisServerClusterBeans=ffanRedisServerClusterBean.getRedisServerSlaveBeans();
				  if(ffanRedisServerClusterBeans!=null&&ffanRedisServerClusterBeans.size()>0){
					  for(LBRedisServerBean ffanRedisServerSlave:ffanRedisServerClusterBeans){
						  
						  LBRedisProxyPoolConfig lbRedisProxyPoolConfig=convertLBRedisProxyPoolConfig(lbRedisServerBean);
						  LBRedisClient ffanRedisClient=new LBRedisClient(lbRedisProxyPoolConfig);
						  ffanRedisServerMasterCluster.getRedisClientBeanMap().put(ffanRedisServerSlave.getKey(), ffanRedisClient);
					  }
				  }
				  
			  }
		  }
	  }
	  
	  
	  /**
	   * 转换
	   * @param lbRedisServerBean
	   * @return
	   */
	  private LBRedisProxyPoolConfig convertLBRedisProxyPoolConfig(LBRedisServerBean lbRedisServerBean){
		  LBRedisProxyPoolConfig lbRedisProxyPoolConfig=new LBRedisProxyPoolConfig();
		  RedisPoolConfig redisPoolConfig= lbRedisServerBean.getRedisPoolConfig();
		  lbRedisProxyPoolConfig.setConnectionTimeout(redisPoolConfig.getConnectionTimeout());
		  lbRedisProxyPoolConfig.setHost(lbRedisServerBean.getHost());
		  lbRedisProxyPoolConfig.setInitialEntries(redisPoolConfig.getInitialConnection());
		  lbRedisProxyPoolConfig.setMaxActiveEntries(redisPoolConfig.getMaxActiveConnection());
		  lbRedisProxyPoolConfig.setMaxWaitMillisOnBorrow(redisPoolConfig.getMaxWaitMillisOnBorrow());
		  lbRedisProxyPoolConfig.setMinActiveEntries(redisPoolConfig.getMinConnection());
		  lbRedisProxyPoolConfig.setMinEvictableIdleTimeMillis(redisPoolConfig.getMinEvictableIdleTimeMillis());
		  lbRedisProxyPoolConfig.setMinIdleEntries(redisPoolConfig.getMinIdleEntries());
		  lbRedisProxyPoolConfig.setPort(lbRedisServerBean.getPort());
		  lbRedisProxyPoolConfig.setTestOnBorrow(redisPoolConfig.isTestOnBorrow());
		  lbRedisProxyPoolConfig.setTestOnReturn(redisPoolConfig.isTestOnReturn());
		  lbRedisProxyPoolConfig.setTestWhileIdle(redisPoolConfig.isTestWhileIdle());
		  lbRedisProxyPoolConfig.setTimeBetweenEvictionRunsMillis(redisPoolConfig.getTimeBetweenEvictionRunsMillis());
		  return lbRedisProxyPoolConfig;
	  }
	  
	/**
	 * 启动系统，开启接收连接，处理业务
	 */
	public void start() {
		try{


		ServerBootstrap bootstrap = new ServerBootstrap();
		bootstrap.group(bossGroup, workerGroup)
				.channel(NioServerSocketChannel.class)
				.childOption(ChannelOption.RCVBUF_ALLOCATOR, new AdaptiveRecvByteBufAllocator(64, 1024, 65536)).childOption(ChannelOption.ALLOCATOR, BUF_ALLOCATOR)
				.childOption(ChannelOption.TCP_NODELAY, Boolean.TRUE)
				.childOption(ChannelOption.SO_KEEPALIVE, Boolean.TRUE)
				.childOption(ChannelOption.SO_SNDBUF ,1024).childOption(ChannelOption.SO_RCVBUF ,1024)
				.childOption(ChannelOption.SO_REUSEADDR, true)
				
				.childHandler(new ChannelInitializer<SocketChannel>() {
					@Override
					protected void initChannel(SocketChannel ch)
							throws Exception {
						ch.pipeline().addLast("RedisRequestDecoder",
								new RedisRequestDecoder());
						ch.pipeline().addLast("RedisReplyEncoder",
								new RedisReplyEncoder());
						ch.pipeline().addLast(
								"FfanRedisServerHandler",
								new RedisServerHandler(
										ffanRedisServerMasterCluster.getRedisClientBeanMap(),ffanRedisServerMasterCluster));
					}
				});
		ChannelFuture channelFuture;
		if(Objects.isNull(ffanRedisServerMasterCluster.getRedisProxyHost())){
			channelFuture = bootstrap.bind().sync();
		}else{
			 channelFuture = bootstrap.bind(
					ffanRedisServerMasterCluster.getRedisProxyHost(),
					ffanRedisServerMasterCluster.getRedisProxyPort());
		}

		channelFuture.syncUninterruptibly();
		logger.info("RedisProxy_Server 已经启动");


		}catch (Exception e){
			e.printStackTrace();
		}finally {

		}
	}
}
