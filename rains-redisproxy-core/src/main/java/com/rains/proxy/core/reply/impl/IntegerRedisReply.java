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
 * @Description:	整数回复
 * 整数回复就是一个以 ":" 开头， CRLF 结尾的字符串表示的整数。
 *
 * 比如说， ":0\r\n" 和 ":1000\r\n" 都是整数回复。
 *
 * 返回整数回复的其中两个命令是 INCR 和 LASTSAVE 。 被返回的整数没有什么特殊的含义，
 * INCR 返回键的一个自增后的整数值， 而 LASTSAVE 则返回一个 UNIX 时间戳， 返回值的唯一限制是这些数必须能够用 64 位有符号整数表示。
 *
 * 整数回复也被广泛地用于表示逻辑真和逻辑假： 比如 EXISTS 和 SISMEMBER 都用返回值 1 表示真， 0 表示假。
 *
 * 其他一些命令， 比如 SADD 、 SREM 和 SETNX ， 只在操作真正被执行了的时候， 才返回 1 ， 否则返回 0 。
 *
 * 以下命令都返回整数回复： SETNX 、 DEL 、 EXISTS 、 INCR 、 INCRBY 、 DECR 、 DECRBY 、 DBSIZE 、 LASTSAVE 、 RENAMENX 、 MOVE 、 LLEN 、 SADD 、 SREM 、 SISMEMBER 、 SCARD
 *
 *
 * @date 2018/5/24  0:58
 */
public class IntegerRedisReply extends CommonRedisReply {

	public IntegerRedisReply() {
		super(Type.INTEGER);
	}

	public IntegerRedisReply(byte[] value) {
		this();
		this.value = value;
	}


	@Override
	public void doEncode(ByteBuf out) {
		out.writeBytes(value);
		writeCRLF(out); 
	}

}
