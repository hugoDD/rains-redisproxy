/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.rains.proxy.core.command.impl;

import com.rains.proxy.core.command.IRedisCommand;
import com.rains.proxy.core.constants.RedisConstants;
import com.rains.proxy.core.utils.ProtoUtils;
import io.netty.buffer.ByteBuf;

import java.util.List;

/**
 * @author dourx
 * @version V1.0
 * @Description: TODO
 * @date 2018/5/28  15:43
 */
public class RedisCommand implements IRedisCommand {

	private boolean inline = false;

	private RedisRequestPolicy policy;

	private int argCount;
	private List<byte[]> args;

	@Override
	public void encode(ByteBuf byteBuf) {
		byteBuf.writeByte((byte) RedisConstants.ASTERISK_BYTE);
	    byteBuf.writeBytes(ProtoUtils.convertIntToByteArray(args.size()));
	    writeCRLF(byteBuf);
	    for (byte[] arg : args) {
	      byteBuf.writeByte((byte) RedisConstants.DOLLAR_BYTE);
	      byteBuf.writeBytes(ProtoUtils.convertIntToByteArray(arg.length));
	      writeCRLF(byteBuf);
	      byteBuf.writeBytes(arg);
	      writeCRLF(byteBuf);
	    }
	}

	/**
	 * @return the argCount
	 */
	public int getArgCount() {
		return argCount;
	}

	/**
	 * @param argCount
	 *            the argCount to set
	 */
	public void setArgCount(int argCount) {
		this.argCount = argCount;
	}

	/**
	 * @return the args
	 */
	public List<byte[]> getArgs() {
		return args;
	}

	/**
	 * @param args
	 *            the args to set
	 */
	public void setArgs(List<byte[]> args) {
		this.args = args;
	}


	private void writeCRLF(ByteBuf byteBuf) {
		byteBuf.writeByte(RedisConstants.CR_BYTE);
		byteBuf.writeByte(RedisConstants.LF_BYTE);
	}

	public boolean isInline() {
		return inline;
	}

	public void setInline(boolean inline) {
		this.inline = inline;
	}

	public RedisRequestPolicy getPolicy() {
		return policy;
	}

	public void setPolicy(RedisRequestPolicy policy) {
		this.policy = policy;
	}

	public String getKey(){
		if(getArgCount()>1){
			return  new String(args.get(1));
		}else {
			return null;
		}
	}

	@Override
	public String toString() {
		final StringBuilder builder = new StringBuilder();
		if(getArgCount()>0){
			for(byte[] arg : args){
				builder.append(new String(arg)).append(" ");
			}
			return builder.toString();
		}
		return super.toString();
	}
}
