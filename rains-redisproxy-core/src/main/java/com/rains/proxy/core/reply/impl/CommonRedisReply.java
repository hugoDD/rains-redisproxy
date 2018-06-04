/**
 * 
 */
package com.rains.proxy.core.reply.impl;


import com.rains.proxy.core.enums.Type;

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

}
