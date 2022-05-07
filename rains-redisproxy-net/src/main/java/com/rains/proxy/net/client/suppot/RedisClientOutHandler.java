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
package com.rains.proxy.net.client.suppot;

import com.rains.proxy.core.command.impl.RedisCommand;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * @author dourx
 * @version V1.0
 * 创建日期 2018/6/4
 * 解码处理
 */
@ChannelHandler.Sharable
public class RedisClientOutHandler extends ChannelOutboundHandlerAdapter {
	private static final Logger logger = LoggerFactory.getLogger(RedisClientOutHandler.class);
	
		
	@Override
	public void write(ChannelHandlerContext ctx, Object msg,
                      ChannelPromise promise) throws Exception {
		if(msg instanceof RedisCommand){
				ctx.writeAndFlush(msg, promise);
			if(logger.isDebugEnabled()){
				logger.debug("client request command :{} ",msg.toString());
			}
		}else{
			logger.error("write redis server msg not instanceof RedisCommand");
		}
		
		
	}
}
