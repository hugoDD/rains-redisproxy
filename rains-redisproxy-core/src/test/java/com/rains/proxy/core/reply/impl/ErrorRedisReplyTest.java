package com.rains.proxy.core.reply.impl;

import com.rains.proxy.core.constants.RedisConstants;
import com.rains.proxy.core.enums.Type;
import com.rains.proxy.core.reply.IRedisReply;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author dourx
 * @version V1.0
 * @Description: TODO
 * @date 2018年 05 月  24日  0:10
 */
public class ErrorRedisReplyTest extends AbstractRedisReplyTest{

    @Test
    public void encode() {
        //-ERR unknown command 'foobar'

        String errMsg ="ERR unknown command 'foobar'";
        ErrorRedisReply redisReply = new ErrorRedisReply(errMsg.getBytes());

        ByteBuf buf = Unpooled.buffer();
        redisReply.encode(buf);



        //  byte[] bytes= ProtoUtils.convertIntToByteArray(3);
        assertEquals(Type.ERROR,redisReply.getType());
        assertTrue(buf.isReadable());
        assertEquals('-',(char)buf.readByte());
        assertEquals(errMsg,readToStr(buf,errMsg.length()));
        assertEquals(RedisConstants.CR_BYTE,(char)buf.readByte());
        assertEquals(RedisConstants.LF_BYTE,(char)buf.readByte());
        assertFalse(buf.isReadable());


        //-WRONGTYPE Operation against a key holding the wrong kind of value
        errMsg="WRONGTYPE Operation against a key holding the wrong kind of value";
         redisReply = new ErrorRedisReply(errMsg.getBytes());
         buf.clear();
         redisReply.encode(buf);

        assertEquals(Type.ERROR,redisReply.getType());
        assertTrue(buf.isReadable());
        assertEquals('-',(char)buf.readByte());
        assertEquals("WRONGTYPE Operation against a key holding the wrong kind of value",readToStr(buf,errMsg.length()));
        assertEquals(RedisConstants.CR_BYTE,(char)buf.readByte());
        assertEquals(RedisConstants.LF_BYTE,(char)buf.readByte());
        assertFalse(buf.isReadable());
    }
}