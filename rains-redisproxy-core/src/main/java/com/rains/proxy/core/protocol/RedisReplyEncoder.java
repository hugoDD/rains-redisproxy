/**
 * 
 */
package com.rains.proxy.core.protocol;

import com.rains.proxy.core.reply.IRedisReply;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.Charset;

/**
 * @author liubing
 *
 */
public class RedisReplyEncoder extends MessageToByteEncoder<IRedisReply> {
	private static final Logger logger = LoggerFactory.getLogger(RedisReplyEncoder.class);

	@Override
	protected void encode(ChannelHandlerContext ctx, IRedisReply msg,
                          ByteBuf out) throws Exception {
    	msg.encode(out);
    	if(logger.isDebugEnabled()){
    		logger.debug("解码前协议文本内容:{}",out.copy().toString(Charset.forName("UTF-8")).replaceAll("\r\n","\\\\r\\\\n"));
		}

	}

}
