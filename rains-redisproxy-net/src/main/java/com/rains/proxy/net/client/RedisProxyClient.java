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
package com.rains.proxy.net.client;


import com.rains.proxy.core.client.impl.AbstractPoolClient;
import com.rains.proxy.core.command.impl.RedisCommand;
import com.rains.proxy.core.config.RedisProxyPoolConfig;
import com.rains.proxy.core.connection.IConnection;
import com.rains.proxy.core.pool.IPoolEntry;
import com.rains.proxy.core.pool.PooledFactory;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



/**
 * @author dourx
 * @version V1.0
 * 创建日期 2018/6/4
 * redisProxy请求redis的客户端
 */
public class RedisProxyClient extends AbstractPoolClient {

	private Logger logger = LoggerFactory.getLogger(RedisProxyClient.class);
	
	private RedisProxyPoolConfig redisProxyPoolConfig;

	private int port;

	private String redisHost;

	/**
	 * @param redisProxyPoolConfig
	 */
	public RedisProxyClient(String redisHost,int port,RedisProxyPoolConfig redisProxyPoolConfig) {
		 super(redisProxyPoolConfig);
		 this.redisHost = redisHost;
		 this.port = port;
		 this.redisProxyPoolConfig = redisProxyPoolConfig;
		 super.initPool();//初始化连接池
	}
	
	/**
	 * 关闭
	 */
	@Override
	public synchronized void close() {
		 try {	
	        	super.pool.shutDown();//连接池关闭,所有都关闭
	        } catch (Exception e) {
	        	logger.error("NettyClient close Error,HOST:"+ redisProxyPoolConfig.getHost()+",PORT:"+ redisProxyPoolConfig.getPort(), e);
	        }
	}

	/**
	 * 创建对象
	 */
	@Override
	protected PooledFactory<IConnection> createChannelFactory() {
		return new RedisConnectionFactory(redisProxyPoolConfig.getHost(),redisProxyPoolConfig.getPort(),redisProxyPoolConfig.getConnectionTimeout());
	}
    
	@Override
	public void write(RedisCommand request, ChannelHandlerContext frontCtx) {
		IConnection connection=null;
		IPoolEntry<IConnection> entry=null;
		try{
			entry  = borrowObject();
        	if(entry==null||entry.getObject()==null){
        		logger.error("NettyClient borrowObject null");
                return ;
        	}
        	connection=entry.getObject();
            connection.write(request,frontCtx);
            
		}catch(Exception e){
			logger.error("NettyClient write request Error :" , e);
		}finally{
			returnObject(entry);
		}
		
	}
   
   
}
