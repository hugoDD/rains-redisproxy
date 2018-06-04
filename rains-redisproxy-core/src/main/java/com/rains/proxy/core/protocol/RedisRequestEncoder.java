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
package com.rains.proxy.core.protocol;

import com.rains.proxy.core.command.impl.RedisCommand;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import io.netty.util.CharsetUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * @author dourx
 * @version V1.0
 * @Description:  request编码
 * @date 2018/5/24  9:26
 */
public class RedisRequestEncoder extends MessageToByteEncoder<RedisCommand> {
	private static final Logger logger = LoggerFactory.getLogger(RedisRequestEncoder.class);

	@Override
	protected void encode(ChannelHandlerContext ctx, RedisCommand msg,
                          ByteBuf out) throws Exception {
    	msg.encode(out);

    	if(logger.isDebugEnabled()){
    		logger.debug("request编码后协议文本内容:{}",out.toString(CharsetUtil.UTF_8).replaceAll("\r\n","\\\\r\\\\n"));
		}

	}

}
