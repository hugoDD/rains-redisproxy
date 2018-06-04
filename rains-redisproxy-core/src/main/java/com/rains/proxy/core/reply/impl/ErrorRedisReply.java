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
 * @Description:	错误回复
 * 错误回复和状态回复非常相似， 它们之间的唯一区别是， 错误回复的第一个字节是 "-" ， 而状态回复的第一个字节是 "+" 。
 *
 * 错误回复只在某些地方出现问题时发送： 比如说， 当用户对不正确的数据类型执行命令， 或者执行一个不存在的命令， 等等。
 *
 * 一个客户端库应该在收到错误回复时产生一个异常。
 *
 * 以下是两个错误回复的例子：
 *
 * -ERR unknown command 'foobar'
 * -WRONGTYPE Operation against a key holding the wrong kind of value
 * 在 "-" 之后，直到遇到第一个空格或新行为止，这中间的内容表示所返回错误的类型。
 *
 * ERR 是一个通用错误，而 WRONGTYPE 则是一个更特定的错误。 一个客户端实现可以为不同类型的错误产生不同类型的异常， 或者提供一种通用的方式， 让调用者可以通过提供字符串形式的错误名来捕捉（trap）不同的错误。
 *
 * 不过这些特性用得并不多， 所以并不是特别重要， 一个受限的（limited）客户端可以通过简单地返回一个逻辑假（false）来表示一个通用的错误条件。
 *
 * @date 2018/5/24  0:51
 */
public class ErrorRedisReply extends CommonRedisReply {

	public ErrorRedisReply() {
		super(Type.ERROR);
	}

	public ErrorRedisReply(byte[] value) {
		this();
		this.value = value;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.wanda.ffan.redis.proxy.core.reply.impl.AbstractRedisReply#doEncode
	 * (io.netty.buffer.ByteBuf)
	 */
	@Override
	public void doEncode(ByteBuf out) {
		out.writeBytes(value);
		writeCRLF(out);
	}

}
