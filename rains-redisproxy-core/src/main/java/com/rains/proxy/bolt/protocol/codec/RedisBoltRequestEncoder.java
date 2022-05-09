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
package com.rains.proxy.bolt.protocol.codec;

import com.alipay.remoting.CommandEncoder;
import com.alipay.remoting.rpc.HeartbeatCommand;
import com.alipay.remoting.rpc.protocol.RpcCommandEncoder;
import com.rains.proxy.bolt.remoting.RedisRequestCommand;
import com.rains.proxy.core.command.IRedisCommand;
import com.rains.proxy.core.command.impl.RedisCommand;
import com.rains.proxy.core.utils.RedisCmdUtils;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.CharsetUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;


/**
 * @author dourx
 * @version V1.0
 * @Description:  request编码
 * @date 2018/5/24  9:26
 */
public class RedisBoltRequestEncoder extends RpcCommandEncoder implements CommandEncoder {
	private static final Logger logger = LoggerFactory.getLogger(RedisBoltRequestEncoder.class);

	@Override
	public void encode(ChannelHandlerContext channelHandlerContext, Serializable msg, ByteBuf byteBuf) throws Exception {
		if(msg instanceof RedisRequestCommand){
			IRedisCommand redisCommand=	((RedisRequestCommand) msg).getRequestObject();
			redisCommand.encode(byteBuf);

		}else if(msg instanceof HeartbeatCommand) {
			RedisCommand ping = RedisCmdUtils.createCmd("ping");
			ping.encode(byteBuf);

		}
		if(logger.isDebugEnabled()){
			logger.debug("request编码后协议文本内容:{}",byteBuf.toString(CharsetUtil.UTF_8).replaceAll("\r\n","\\\\r\\\\n"));
		}

	}
}
