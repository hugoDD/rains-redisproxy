/**
 *
 */
package com.rains.proxy.core.command.impl;

import com.rains.proxy.core.command.IRedisCommand;
import io.netty.buffer.ByteBuf;


public class ShutdownCommand implements IRedisCommand {


	@Override
	public void encode(ByteBuf byteBuf) {
		// TODO Auto-generated method stub

	}

	@Override
	public RedisRequestPolicy getPolicy() {
		return CommandParse.getPolicy("SHUTDOWN");
	}

}
