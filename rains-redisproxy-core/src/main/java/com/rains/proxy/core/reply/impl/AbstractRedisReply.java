/**
 * 
 */
package com.rains.proxy.core.reply.impl;

import com.rains.proxy.core.constants.RedisConstants;
import com.rains.proxy.core.enums.Type;
import com.rains.proxy.core.reply.IRedisReply;
import io.netty.buffer.ByteBuf;

/**
 * @author liubing
 *
 */
public abstract class AbstractRedisReply implements IRedisReply {

	private Type type;

	/**
	 * 
	 */
	public AbstractRedisReply() {
		super();
	}

	/**
	 * @param type
	 */
	public AbstractRedisReply(Type type) {
		super();
		this.type = type;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.wanda.ffan.redis.proxy.core.reply.IRedisReply#getType()
	 */
	@Override
	public Type getType() {

		return type;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.wanda.ffan.redis.proxy.core.reply.IRedisReply#setType(com.wanda.ffan
	 * .redis.proxy.core.enums.Type)
	 */
	@Override
	public void setType(Type type) {
		this.type = type;
	}

	public void writeCRLF(ByteBuf byteBuf) {
		byteBuf.writeByte(RedisConstants.CR_BYTE);
		byteBuf.writeByte(RedisConstants.LF_BYTE);
	}

	public void writeStart(ByteBuf byteBuf) {
		byteBuf.writeByte(type.getCode());
	}
	
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.wanda.ffan.redis.proxy.core.reply.IRedisReply#encode(io.netty.buffer
	 * .ByteBuf)
	 */
	@Override
	public void encode(ByteBuf out) {
		writeStart(out);
	    doEncode(out);
	}
	
	public abstract void doEncode(ByteBuf out);
}
