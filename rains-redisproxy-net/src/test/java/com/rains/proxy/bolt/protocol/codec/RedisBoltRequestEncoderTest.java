package com.rains.proxy.bolt.protocol.codec;

import com.alipay.remoting.Connection;
import com.alipay.remoting.Protocol;
import com.alipay.remoting.ProtocolCode;
import com.alipay.remoting.ProtocolManager;
import com.alipay.remoting.codec.Codec;
import com.rains.proxy.bolt.protocol.RedisClientProtocol;
import com.rains.proxy.bolt.remoting.RedisBoltCodec;
import com.rains.proxy.core.command.impl.RedisCommand;
import com.rains.proxy.core.constants.RedisConstants;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.embedded.EmbeddedChannel;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;

public class RedisBoltRequestEncoderTest extends AbstractRedisReplyTest{



    @Test
    public void testRedisProtocol(){
        Protocol redisProtocol = ProtocolManager.getProtocol(ProtocolCode.fromBytes(RedisClientProtocol.PROTOCOL_CODE));
        Assert.assertNotNull(redisProtocol);
        assertTrue(redisProtocol instanceof RedisClientProtocol);

        assertNotNull(redisProtocol.getEncoder());
        assertTrue(redisProtocol.getEncoder() instanceof RedisBoltRequestEncoder);

        Codec codec = new RedisBoltCodec();
        assertNotNull(codec);
        assertNotNull(codec.newEncoder());

    }

    @Test
    public void encode() {
        //set mykey myvalue
        ByteBuf buf = Unpooled.buffer();;
        RedisCommand redisCommand = new RedisCommand();
        redisCommand.setArgCount(3);
        List<byte[]> args = new ArrayList<>();
        args.add("set".getBytes());
        args.add("mykey".getBytes());
        args.add("myvalue".getBytes());
        redisCommand.setArgs(args);

        // redisCommand.encode(buf);
        Codec codec = new RedisBoltCodec();
        EmbeddedChannel channel = new EmbeddedChannel(codec.newEncoder());
        channel.attr(Connection.PROTOCOL).set(ProtocolCode.fromBytes(RedisClientProtocol.PROTOCOL_CODE));
        assertTrue(channel.writeOutbound(redisCommand));

        assertTrue(channel.finish());

        //read
        ByteBuf out = channel.readOutbound();
        assertNotNull(out);


        assertEquals("*3",readToStr(out,2));
        assertEquals(RedisConstants.CR_BYTE,(char)out.readByte());
        assertEquals(RedisConstants.LF_BYTE,(char)out.readByte());

        assertEquals("$3",readToStr(out,2));
        assertEquals(RedisConstants.CR_BYTE,(char)out.readByte());
        assertEquals(RedisConstants.LF_BYTE,(char)out.readByte());

        assertEquals("set",readToStr(out,3));
        assertEquals(RedisConstants.CR_BYTE,(char)out.readByte());
        assertEquals(RedisConstants.LF_BYTE,(char)out.readByte());

        assertEquals("$5",readToStr(out,2));
        assertEquals(RedisConstants.CR_BYTE,(char)out.readByte());
        assertEquals(RedisConstants.LF_BYTE,(char)out.readByte());

        assertEquals("mykey",readToStr(out,5));
        assertEquals(RedisConstants.CR_BYTE,(char)out.readByte());
        assertEquals(RedisConstants.LF_BYTE,(char)out.readByte());


        assertEquals("$7",readToStr(out,2));
        assertEquals(RedisConstants.CR_BYTE,(char)out.readByte());
        assertEquals(RedisConstants.LF_BYTE,(char)out.readByte());


        assertEquals("myvalue",readToStr(out,7));
        assertEquals(RedisConstants.CR_BYTE,(char)out.readByte());
        assertEquals(RedisConstants.LF_BYTE,(char)out.readByte());

    }
}
