package com.rains.proxy.core.protocol;

import com.rains.proxy.core.command.impl.RedisCommand;
import com.rains.proxy.core.constants.RedisConstants;
import com.rains.proxy.core.reply.impl.AbstractRedisReplyTest;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.embedded.EmbeddedChannel;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * @author dourx
 * @version V1.0
 * @Description: redisRequest单元测试
 * @date 2018年 05 月  24日  9:24
 */
public class RedisRequestEncoderTest extends AbstractRedisReplyTest {

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

        EmbeddedChannel channel = new EmbeddedChannel(new RedisRequestEncoder());

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