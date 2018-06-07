/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.rains.proxy.net.server;


import com.rains.proxy.core.bean.RedisServerMasterCluster;
import com.rains.proxy.core.bean.RedisPoolConfig;
import com.rains.proxy.core.bean.support.RedisServerBean;
import com.rains.proxy.core.bean.support.RedisServerClusterBean;
import com.rains.proxy.core.pool.commons.RedisProxyPoolConfig;
import com.rains.proxy.core.protocol.RedisReplyEncoder;
import com.rains.proxy.core.protocol.RedisRequestDecoder;
import com.rains.proxy.net.client.RedisProxyClient;
import com.rains.proxy.net.server.support.RedisServerHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.util.List;
import java.util.Objects;

/**
 * @author dourx
 * @version V1.0
 * 创建日期 2018/6/5
 */
public class RedisProxyServer {
	
	private Logger logger = LoggerFactory.getLogger(RedisProxyServer.class);

	private RedisServerMasterCluster redisServerMasterCluster;
	
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
	 * @param   redisServerMasterCluster 主机集群
	 * @return
	 */
	public RedisProxyServer(RedisServerMasterCluster redisServerMasterCluster) {
		super();
		this.redisServerMasterCluster = redisServerMasterCluster;
		init();
	}
	
	 
	/**
	   * 销废
	   */
	 public void destroy(){
		  for(String key: redisServerMasterCluster.getRedisClientBeanMap().keySet()){
			  redisServerMasterCluster.getRedisClientBeanMap().get(key).close();
		  }
	  }
	  /**
	   * 初始化客户端
	   */
	  private void init(){
		  if(redisServerMasterCluster !=null&& redisServerMasterCluster.getRedisServerClusterBeans()!=null&& redisServerMasterCluster.getRedisServerClusterBeans().size()>0){
			  for(RedisServerClusterBean ffanRedisServerClusterBean: redisServerMasterCluster.getRedisServerClusterBeans()){
				  RedisServerBean redisServerBean =ffanRedisServerClusterBean.getRedisServerMasterBean();
				  if(redisServerBean !=null){//主
					  RedisProxyPoolConfig redisProxyPoolConfig =convertLBRedisProxyPoolConfig(redisServerBean);
					  RedisProxyClient ffanRedisClient=new RedisProxyClient(redisProxyPoolConfig);
					  redisServerMasterCluster.getRedisClientBeanMap().put(redisServerBean.getKey(), ffanRedisClient);
				  }
				  List<RedisServerBean> ffanRedisServerClusterBeans=ffanRedisServerClusterBean.getRedisServerSlaveBeans();
				  if(ffanRedisServerClusterBeans!=null&&ffanRedisServerClusterBeans.size()>0){
					  for(RedisServerBean ffanRedisServerSlave:ffanRedisServerClusterBeans){
						  
						  RedisProxyPoolConfig redisProxyPoolConfig =convertLBRedisProxyPoolConfig(redisServerBean);
						  RedisProxyClient ffanRedisClient=new RedisProxyClient(redisProxyPoolConfig);
						  redisServerMasterCluster.getRedisClientBeanMap().put(ffanRedisServerSlave.getKey(), ffanRedisClient);
					  }
				  }
				  
			  }
		  }
	  }
	  
	  
	  /**
	   * 转换
	   * @param redisServerBean
	   * @return
	   */
	  private RedisProxyPoolConfig convertLBRedisProxyPoolConfig(RedisServerBean redisServerBean){
		  RedisProxyPoolConfig redisProxyPoolConfig =new RedisProxyPoolConfig();
		  RedisPoolConfig redisPoolConfig= redisServerBean.getRedisPoolConfig();
		  redisProxyPoolConfig.setConnectionTimeout(redisPoolConfig.getConnectionTimeout());
		  redisProxyPoolConfig.setHost(redisServerBean.getHost());
		  redisProxyPoolConfig.setInitialEntries(redisPoolConfig.getInitialConnection());
		  redisProxyPoolConfig.setMaxActiveEntries(redisPoolConfig.getMaxActiveConnection());
		  redisProxyPoolConfig.setMaxWaitMillisOnBorrow(redisPoolConfig.getMaxWaitMillisOnBorrow());
		  redisProxyPoolConfig.setMinActiveEntries(redisPoolConfig.getMinConnection());
		  redisProxyPoolConfig.setMinEvictableIdleTimeMillis(redisPoolConfig.getMinEvictableIdleTimeMillis());
		  redisProxyPoolConfig.setMinIdleEntries(redisPoolConfig.getMinIdleEntries());
		  redisProxyPoolConfig.setPort(redisServerBean.getPort());
		  redisProxyPoolConfig.setTestOnBorrow(redisPoolConfig.isTestOnBorrow());
		  redisProxyPoolConfig.setTestOnReturn(redisPoolConfig.isTestOnReturn());
		  redisProxyPoolConfig.setTestWhileIdle(redisPoolConfig.isTestWhileIdle());
		  redisProxyPoolConfig.setTimeBetweenEvictionRunsMillis(redisPoolConfig.getTimeBetweenEvictionRunsMillis());
		  return redisProxyPoolConfig;
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
										redisServerMasterCluster.getRedisClientBeanMap(), redisServerMasterCluster));
					}
				});
		ChannelFuture channelFuture;
		if(Objects.isNull(redisServerMasterCluster.getRedisProxyHost())){
			channelFuture = bootstrap.bind(new InetSocketAddress(redisServerMasterCluster.getRedisProxyPort())).sync();
		}else{
			 channelFuture = bootstrap.bind(
					redisServerMasterCluster.getRedisProxyHost(),
					redisServerMasterCluster.getRedisProxyPort());
		}

		channelFuture.syncUninterruptibly();
		logger.info("RedisProxy_Server 已经启动");


		}catch (Exception e){
			e.printStackTrace();
		}finally {

		}
	}
}
