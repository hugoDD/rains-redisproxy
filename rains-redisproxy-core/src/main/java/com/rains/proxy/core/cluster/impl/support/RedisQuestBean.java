/**
 * 
 */
package com.rains.proxy.core.cluster.impl.support;

/**
 * redisproxy 接受请求中间类
 *
 */
public class RedisQuestBean {
	/**命令*/
	private String command;
	/**关键字*/
	private byte[] key;
	 /**是否写*/
	private boolean isWrite;
	
	/**
	 * @param command
	 * @param key
	 * @param isWrite
	 */
	public RedisQuestBean(String command, byte[] key, boolean isWrite) {
		super();
		this.command = command;
		this.key = key;
		this.isWrite = isWrite;
	}

	/**
	 * @return the isWrite
	 */
	public boolean isWrite() {
		return isWrite;
	}



	/**
	 * @param isWrite the isWrite to set
	 */
	public void setWrite(boolean isWrite) {
		this.isWrite = isWrite;
	}



	/**
	 * 
	 */
	public RedisQuestBean() {
		super();
	}

	/**
	 * @return the command
	 */
	public String getCommand() {
		return command;
	}

	/**
	 * @param command the command to set
	 */
	public void setCommand(String command) {
		this.command = command;
	}

	/**
	 * @return the key
	 */
	public byte[] getKey() {
		return key;
	}

	/**
	 * @param key the key to set
	 */
	public void setKey(byte[] key) {
		this.key = key;
	}
	
	
}
