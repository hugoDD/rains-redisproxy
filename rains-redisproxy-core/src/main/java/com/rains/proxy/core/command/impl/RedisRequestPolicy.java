package com.rains.proxy.core.command.impl;


public class RedisRequestPolicy {
	/**类别 */
	private byte categroy;
	/**处理类型*/
	private byte handleType;
	
	private byte rw = -1;
	
	public RedisRequestPolicy(byte category, byte type, byte rw) {
		super();
		this.categroy = category;
		this.handleType = type;
		this.rw = rw;
	}

	public byte getCategory() {
		return categroy;
	}
	
	public byte getHandleType() {
		return handleType;
	}

	public boolean isRead() {
		 return rw == CommandParse.READ_CMD;
	}
	
	public boolean isNotThrough() {
		return handleType == CommandParse.NO_THROUGH_CMD;
	}
	
}