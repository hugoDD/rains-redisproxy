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

import com.rains.proxy.core.enums.Type;
import io.netty.buffer.ByteBuf;

/**
 * @author dourx
 * @version V1.0
 * @Description:	状态回复
 * 一个状态回复（或者单行回复，single line reply）是一段以 "+" 开始、 "\r\n" 结尾的单行字符串。
 *
 * 以下是一个状态回复的例子：
 *
 * +OK
 * 客户端库应该返回 "+" 号之后的所有内容。 比如在在上面的这个例子中， 客户端就应该返回字符串 "OK" 。
 *
 * 状态回复通常由那些不需要返回数据的命令返回，这种回复不是二进制安全的，它也不能包含新行。
 *
 * 状态回复的额外开销非常少，只需要三个字节（开头的 "+" 和结尾的 CRLF）
 *
 * @date 2018/5/24  0:53
 */
public class StatusRedisReply extends CommonRedisReply {

	public StatusRedisReply() {
		super(Type.STATUS);
	}

	public StatusRedisReply(byte[] value) {
		this();
		this.value = value;
	}


	@Override
	public void doEncode(ByteBuf out) {
		// TODO Auto-generated method stub
		  out.writeBytes(value);
		  writeCRLF(out);
	}

}
