/**
 *
 */
package com.rains.proxy.core.reply.impl;


import com.rains.proxy.core.constants.RedisConstants;
import com.rains.proxy.core.enums.Type;
import io.netty.buffer.ByteBuf;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;

/**
 * @author liubing
 *
 */
public abstract class CommonRedisReply extends AbstractRedisReply {

	protected byte[] value;

	public CommonRedisReply(Type type) {
		super(type);
	}

	public void setValue(byte[] value) {
		this.value = value;
	}

	@Override
	public boolean handler(ByteBuf in) {
		try {
			byte[] value = readLine(in).getBytes(RedisConstants.DEFAULT_CHARACTER);
			this.setValue(value);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return false;

		}

		return true;
	}

	protected String readLine(ByteBuf byteBuf) {
		StringBuilder sb = new StringBuilder();
		char ch = (char) byteBuf.readByte();
		while (ch != RedisConstants.CR_BYTE) {
			sb.append(ch);
			//\r
			ch = (char) byteBuf.readByte();
		}
		// \n
		byteBuf.readByte();
		return sb.toString();
	}

	protected int readInt(ByteBuf buffer) {
		return Integer.parseInt(readLine(buffer));
	}
	@Override
	public String toString() {
		return getClass().getName()+"{" +
				"type=" + getType() +
				",value=" +(value==null?null: new String(value)) +
				'}';
	}
}
