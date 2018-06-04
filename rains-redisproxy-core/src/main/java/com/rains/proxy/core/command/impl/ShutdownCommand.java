/**
 * 
 */
package com.rains.proxy.core.command.impl;

import com.rains.proxy.core.command.IRedisCommand;
import io.netty.buffer.ByteBuf;

/**
 * @author liubing
 *
 */
public class ShutdownCommand implements IRedisCommand {

	/* (non-Javadoc)
	 * @see com.wanda.ffan.redis.proxy.core.command.IRedisCommand#encode(io.netty.buffer.ByteBuf)
	 */
	@Override
	public void encode(ByteBuf byteBuf) {
		// TODO Auto-generated method stub

	}

}
