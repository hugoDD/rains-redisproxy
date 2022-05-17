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
package com.rains.proxy.core.reply.impl;

import com.rains.proxy.bolt.protocol.codec.ReplyFactory;
import com.rains.proxy.core.constants.RedisConstants;
import com.rains.proxy.core.enums.Type;
import com.rains.proxy.core.reply.IRedisReply;
import com.rains.proxy.core.utils.ProtoUtils;
import io.netty.buffer.ByteBuf;
import io.netty.util.CharsetUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * @author dourx
 * @version V1.0
 * @Description:	多条批量回复
 * 像 LRANGE 这样的命令需要返回多个值， 这一目标可以通过多条批量回复来完成。
 *
 * 多条批量回复是由多个回复组成的数组， 数组中的每个元素都可以是任意类型的回复， 包括多条批量回复本身。
 *
 * 多条批量回复的第一个字节为 "*" ， 后跟一个字符串表示的整数值， 这个值记录了多条批量回复所包含的回复数量， 再后面是一个 CRLF 。
 *
 * 客户端： LRANGE mylist 0 3
 * 服务器： *4
 * 服务器： $3
 * 服务器： foo
 * 服务器： $3
 * 服务器： bar
 * 服务器： $5
 * 服务器： Hello
 * 服务器： $5
 * 服务器： World
 * 在上面的示例中，服务器发送的所有字符串都由 CRLF 结尾。
 *
 * 正如你所见到的那样， 多条批量回复所使用的格式， 和客户端发送命令时使用的统一请求协议的格式一模一样。 它们之间的唯一区别是：
 *
 * 统一请求协议只发送批量回复。
 * 而服务器应答命令时所发送的多条批量回复，则可以包含任意类型的回复。
 * 以下例子展示了一个多条批量回复， 回复中包含四个整数值， 以及一个二进制安全字符串：
 *
 * *5\r\n
 * :1\r\n
 * :2\r\n
 * :3\r\n
 * :4\r\n
 * $6\r\n
 * foobar\r\n
 * 在回复的第一行， 服务器发送 *5\r\n ， 表示这个多条批量回复包含 5 条回复， 再后面跟着的则是 5 条回复的正文。
 *
 * 多条批量回复也可以是空白的（empty）， 就像这样：
 *
 * 客户端： LRANGE nokey 0 1
 * 服务器： *0\r\n
 * 无内容的多条批量回复（null multi bulk reply）也是存在的， 比如当 BLPOP 命令的阻塞时间超过最大时限时， 它就返回一个无内容的多条批量回复， 这个回复的计数值为 -1 ：
 *
 * 客户端： BLPOP key 1
 * 服务器： *-1\r\n
 * 客户端库应该区别对待空白多条回复和无内容多条回复： 当 Redis 返回一个无内容多条回复时， 客户端库应该返回一个 null 对象， 而不是一个空数组。
 *
 * @date 2018/5/24  1:06
 */
public class MultyBulkRedisReply extends CommonRedisReply implements Serializable {
	private static final Logger logger = LoggerFactory.getLogger(MultyBulkRedisReply.class);

	/**
	 *
	 */
	private static final long serialVersionUID = 5695076544459040408L;

	protected List<IRedisReply> list = new ArrayList<IRedisReply>();

	private int count;

	public void setCount(int count) {
		this.count = count;
	}


	/**
	 * @return the list
	 */
	public List<IRedisReply> getList() {
		return list;
	}


	/**
	 * @return the count
	 */
	public int getCount() {
		return count;
	}


	public MultyBulkRedisReply() {
		super(Type.MULTYBULK);
	}


	@Override
	public void doEncode(ByteBuf out) {

		out.writeBytes(ProtoUtils.convertIntToByteArray(count));
		writeCRLF(out);
		for (IRedisReply reply : this.list) {
			if (reply.getType()==Type.INTEGER) {
//				if (value == null&&count==0) {
//					out.writeByte(RedisConstants.COLON_BYTE);
//					out.writeBytes(ProtoUtils.convertIntToByteArray(-1));
//					writeCRLF(out);
//				} else {
				out.writeByte(RedisConstants.COLON_BYTE);
//					out.writeBytes(ProtoUtils
//							.convertIntToByteArray(((IntegerRedisReply) reply).value.length));
//					writeCRLF(out);
				out.writeBytes(((IntegerRedisReply) reply).value);
				writeCRLF(out);
//				}

			} else if (reply.getType()==Type.BULK) {
				if (((BulkRedisReply) reply).value == null) {
					out.writeByte(RedisConstants.DOLLAR_BYTE);
					out.writeBytes(ProtoUtils.convertIntToByteArray(-1));
					writeCRLF(out);
				} else {
					out.writeByte(RedisConstants.DOLLAR_BYTE);
					out.writeBytes(ProtoUtils
							.convertIntToByteArray(((BulkRedisReply) reply).value.length));
					writeCRLF(out);
					out.writeBytes(((BulkRedisReply) reply).value);
					writeCRLF(out);
				}

			}else if(reply.getType()==Type.MULTYBULK){
				reply.encode(out);
				//	doEncodeMultybulk(out,(MultyBulkRedisReply)reply);
			}
		}
		if(logger.isDebugEnabled()){
			logger.debug("reply解码后再编码的协议文本内容 :{}",out.toString(CharsetUtil.UTF_8).replaceAll("\r\n","\\\\r\\\\n"));
		}
	}

	private void doEncodeMultybulk(ByteBuf out , MultyBulkRedisReply redisReply){


	}

	public void addReply(IRedisReply reply) {
		list.add(reply);
	}

	@Override
	public boolean handler(ByteBuf in) {
		try {
			readArrayReply(in,this);
		} catch (UnsupportedEncodingException e) {
			logger.error(e.getMessage(),e);
			return false;
		}
		return true;
	}
	private void readArrayReply(ByteBuf buffer, MultyBulkRedisReply multyBulkRedisReply) throws UnsupportedEncodingException {
		int count = readInt(buffer);
		//已经有值，防止多次,netty在读取85次会多次解析
		if (multyBulkRedisReply != null && multyBulkRedisReply.getCount() == count) {
			multyBulkRedisReply.getList().clear();
		}
		multyBulkRedisReply.setCount(count);
		for (int i = 0; i < count; i++) {
			char type = (char) buffer.readByte();
			IRedisReply iRedisReply = ReplyFactory.buildReply(type);
			boolean done = iRedisReply.handler(buffer);
			if(done){
				multyBulkRedisReply.addReply(iRedisReply);
			}
			/*if (type == RedisConstants.COLON_BYTE) {
				IntegerRedisReply reply = new IntegerRedisReply();
				reply.setValue(readLine(buffer).getBytes(RedisConstants.DEFAULT_CHARACTER));
				multyBulkRedisReply.addReply(reply);
			} else if (type == RedisConstants.DOLLAR_BYTE) {
				BulkRedisReply bulkReply = new BulkRedisReply();
				bulkReply.handler(buffer);
//				readBulkReply(buffer, bulkReply);
				multyBulkRedisReply.addReply(bulkReply);
			} else if (type == RedisConstants.ASTERISK_BYTE) {
				MultyBulkRedisReply mbRedisReply = new MultyBulkRedisReply();
				readArrayReply(buffer, mbRedisReply);
				multyBulkRedisReply.addReply(mbRedisReply);
			}*/
		}

	}

}
