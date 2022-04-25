/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.rains.proxy.bolt.protocol.codec;


import com.alipay.remoting.CommandDecoder;
import com.rains.proxy.core.constants.RedisConstants;
import com.rains.proxy.core.enums.Type;
import com.rains.proxy.core.reply.IRedisReply;
import com.rains.proxy.core.reply.impl.*;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.CharsetUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.util.List;

/**
 * @author dourx
 * @version V1.0
 * 创建日期 2018/6/1
 */
public class RedisBoltReplyDecoder implements CommandDecoder  {
    private static final Logger logger = LoggerFactory.getLogger(RedisBoltReplyDecoder.class);


    public RedisBoltReplyDecoder() {

    }


    @Override
    public void decode(ChannelHandlerContext ctx, ByteBuf in,
                       List<Object> out) throws Exception {

        IRedisReply redisReply = null;
//		switch (state()) {
//	      case READ_INIT:

        char ch = (char) in.readByte();
        if (ch == RedisConstants.ASTERISK_BYTE) {
            redisReply = new MultyBulkRedisReply();
        } else if (ch == RedisConstants.DOLLAR_BYTE) {
            redisReply = new BulkRedisReply();
        } else if (ch == RedisConstants.COLON_BYTE) {
            redisReply = new IntegerRedisReply();
        } else if (ch == RedisConstants.OK_BYTE) {
            redisReply = new StatusRedisReply();
        } else if (ch == RedisConstants.ERROR_BYTE) {
            redisReply = new ErrorRedisReply();
        }
//	        checkpoint(ReplyState.READ_REPLY);
//	      case READ_REPLY:
        if (redisReply == null) {
            //checkpoint(ReplyState.READ_INIT);
            return;
        }
        Type type = redisReply.getType();
        if (type == Type.INTEGER) {
            byte[] value = readLine(in).getBytes(RedisConstants.DEFAULT_CHARACTER);
            ((IntegerRedisReply) redisReply).setValue(value);
        } else if (type == Type.STATUS) {
            byte[] value = readLine(in).getBytes(RedisConstants.DEFAULT_CHARACTER);
            ((StatusRedisReply) redisReply).setValue(value);
        } else if (type == Type.ERROR) {
            byte[] value = readLine(in).getBytes(RedisConstants.DEFAULT_CHARACTER);
            ((ErrorRedisReply) redisReply).setValue(value);
        } else if (type == Type.BULK) {
            readBulkReply(in, (BulkRedisReply) redisReply);
        } else if (type == Type.MULTYBULK) {
            readArrayReply(in, (MultyBulkRedisReply) redisReply);
        }

        out.add(redisReply);
        redisReply = null;
        //checkpoint(ReplyState.READ_INIT);
        if (logger.isDebugEnabled()) {
            logger.debug("reply解码前协议文本内容:{}", in.slice(0, in.readerIndex()).toString(CharsetUtil.UTF_8).replaceAll("\r\n", "\\\\r\\\\n"));
        }

        return;
//	      default:
//	        throw new Error("can't reach there!");
    }


    private void readArrayReply(ByteBuf buffer, MultyBulkRedisReply multyBulkRedisReply) throws UnsupportedEncodingException {
        int count = readInt(buffer);
        if (multyBulkRedisReply != null && multyBulkRedisReply.getCount() == count) {//已经有值，防止多次,netty在读取85次会多次解析
            multyBulkRedisReply.getList().clear();
        }
        multyBulkRedisReply.setCount(count);
        for (int i = 0; i < count; i++) {
            char type = (char) buffer.readByte();
            if (type == RedisConstants.COLON_BYTE) {
                IntegerRedisReply reply = new IntegerRedisReply();
                reply.setValue(readLine(buffer).getBytes(RedisConstants.DEFAULT_CHARACTER));
                multyBulkRedisReply.addReply(reply);
            } else if (type == RedisConstants.DOLLAR_BYTE) {
                BulkRedisReply bulkReply = new BulkRedisReply();
                readBulkReply(buffer, bulkReply);
                multyBulkRedisReply.addReply(bulkReply);
            } else if (type == RedisConstants.ASTERISK_BYTE) {
                MultyBulkRedisReply mbRedisReply = new MultyBulkRedisReply();
                readArrayReply(buffer, mbRedisReply);
                multyBulkRedisReply.addReply(mbRedisReply);
            }
        }

    }

    private void readBulkReply(ByteBuf buffer, BulkRedisReply bulkReply) {
        int length = readInt(buffer);
        bulkReply.setLength(length);
        if (length == -1) {//read null

        } else if (length == 0) {//read ""
            buffer.skipBytes(2);
        } else {
            byte[] value = new byte[length];

            buffer.readBytes(value);

            bulkReply.setValue(value);

            buffer.skipBytes(2);
        }
    }

    private int readInt(ByteBuf buffer) {
        return Integer.parseInt(readLine(buffer));
    }

    private String readLine(ByteBuf byteBuf) {
        StringBuilder sb = new StringBuilder();
        char ch = (char) byteBuf.readByte();
        while (ch != RedisConstants.CR_BYTE) {
            sb.append(ch);
            ch = (char) byteBuf.readByte();//\r
        }
        byteBuf.readByte();// \n
        return sb.toString();
    }
}
