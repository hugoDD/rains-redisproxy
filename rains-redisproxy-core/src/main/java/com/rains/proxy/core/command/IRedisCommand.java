/**
 *
 */
package com.rains.proxy.core.command;

import io.netty.buffer.ByteBuf;

import java.io.Serializable;

/**
 * @author liubing
 *
 */
public interface IRedisCommand extends Serializable {
	/**
	 * 编码
	 * @param byteBuf
	 */
	void encode(ByteBuf byteBuf);

}
