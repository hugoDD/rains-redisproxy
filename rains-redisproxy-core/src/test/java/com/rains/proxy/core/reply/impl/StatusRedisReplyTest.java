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
 * @Description: 状态回复单元测试
 * @date 2018年 05 月  24日  0:55
 */
public class StatusRedisReplyTest extends AbstractRedisReplyTest{

    /**
     * 以下是一个状态回复的例子：
     *
     * +OK
     */
    @Test
    public void encode() {
        //+OK

        StatusRedisReply redisReply = new StatusRedisReply("OK".getBytes());

        ByteBuf buf = Unpooled.buffer();
        redisReply.encode(buf);



        //  byte[] bytes= ProtoUtils.convertIntToByteArray(3);
        assertEquals(Type.STATUS,redisReply.getType());
        assertTrue(buf.isReadable());
        assertEquals('+',(char)buf.readByte());
        assertEquals("OK",readToStr(buf,"OK".length()));
        assertEquals(RedisConstants.CR_BYTE,(char)buf.readByte());
        assertEquals(RedisConstants.LF_BYTE,(char)buf.readByte());
        assertFalse(buf.isReadable());
    }
}