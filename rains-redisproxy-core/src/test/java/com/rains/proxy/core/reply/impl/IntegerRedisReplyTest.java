package com.rains.proxy.core.reply.impl;

import com.rains.proxy.core.constants.RedisConstants;
import com.rains.proxy.core.enums.Type;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author dourx
 * @version V1.0
 * @Description: 整数回复单元测试
 * @date 2018年 05 月  24日  0:34
 */
public class IntegerRedisReplyTest {

    /**
     * 比如说， ":0\r\n" 和 ":1000\r\n" 都是整数回复。
     */
    @Test
    public void encode() {
        //测试 “:0\r\n”
        IntegerRedisReply redisReply = new IntegerRedisReply("0".getBytes());
        ByteBuf buf = Unpooled.buffer();
        redisReply.encode(buf);

        assertTrue(buf.isReadable());
        assertEquals(Type.INTEGER,redisReply.getType());

        assertEquals(':',(char)buf.readByte());
        assertEquals('0',(char)buf.readByte());
        assertEquals(RedisConstants.CR_BYTE,(char)buf.readByte());
        assertEquals(RedisConstants.LF_BYTE,(char)buf.readByte());
        assertFalse(buf.isReadable());

        //测试 ":1000\r\n"
        buf.clear();
        redisReply.setValue("1000".getBytes());
        redisReply.encode(buf);

        assertTrue(buf.isReadable());
        assertEquals(Type.INTEGER,redisReply.getType());

        assertEquals(':',(char)buf.readByte());
        assertEquals('1',(char)buf.readByte());
        assertEquals('0',(char)buf.readByte());
        assertEquals('0',(char)buf.readByte());
        assertEquals('0',(char)buf.readByte());
        assertEquals(RedisConstants.CR_BYTE,(char)buf.readByte());
        assertEquals(RedisConstants.LF_BYTE,(char)buf.readByte());
        assertFalse(buf.isReadable());

    }
}