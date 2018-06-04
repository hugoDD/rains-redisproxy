/**
 * 
 */
package com.rains.proxy.core.connection.impl;

import com.rains.proxy.core.connection.IConnectionCallBack;
import com.rains.proxy.core.reply.IRedisReply;
import io.netty.channel.Channel;

/**
 * 
 * @author liubing
 *
 */
public class RedisConnectionCallBack implements IConnectionCallBack {
	
	private Channel channel;
	/* (non-Javadoc)
	 * @see com.wanda.ffan.redis.proxy.core.connection.IConnectionCallBack#handleReply(com.wanda.ffan.redis.proxy.core.reply.IRedisReply)
	 */
	@Override
	public void handleReply(IRedisReply reply) {
	    channel.writeAndFlush(reply);
	}
	/**
	 * @param channel
	 */
	public RedisConnectionCallBack(Channel channel) {
		super();
		this.channel = channel;
	}
	
	
}
