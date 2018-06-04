package com.rains.proxy.core.protocol;

import com.rains.proxy.core.constants.RedisConstants;
import com.rains.proxy.core.enums.Type;
import com.rains.proxy.core.reply.IRedisReply;
import com.rains.proxy.core.reply.impl.AbstractRedisReply;
import com.rains.proxy.core.reply.impl.AbstractRedisReplyTest;
import com.rains.proxy.core.reply.impl.BulkRedisReply;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.embedded.EmbeddedChannel;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Objects;

import static org.junit.Assert.*;

/**
 * @author dourx
 * @version V1.0
 * @Description: TODO
 * @date 2018年 05 月  23日  20:51
 */
public class RedisReplyEncoderTest extends AbstractRedisReplyTest {

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void encode() {
        //(1) 创建一个 ByteBuf，并且写入 9 个负整数
        // String msgStr = "$6\r\nfoobar\r\n";
        IRedisReply redisReply = new BulkRedisReply();
        ((BulkRedisReply) redisReply).setLength(6);
        ((BulkRedisReply) redisReply).setValue("foobar".getBytes());

        //(2) 创建一个EmbeddedChannel，并安装一个要测试的 AbsIntegerEncoder

        EmbeddedChannel channel = new EmbeddedChannel(
                new RedisReplyEncoder());

        //(3) 写入 ByteBuf，并断言调用 readOutbound()方法将会产生数据
        assertTrue(channel.writeOutbound(redisReply));

        //(4) 将该 Channel 标记为已完成状态
        assertTrue(channel.finish());

      ByteBuf buf = channel.readOutbound();
      assertNotNull(buf);


        assertEquals(Type.BULK,redisReply.getType());
        assertTrue(buf.isReadable());
        assertEquals('$',(char)buf.readByte());
        assertEquals('6',(char)buf.readByte());
        assertEquals(RedisConstants.CR_BYTE,buf.readByte());
        assertEquals(RedisConstants.LF_BYTE,buf.readByte());
        assertEquals("foobar",readToStr(buf,6));
        assertEquals(RedisConstants.CR_BYTE,buf.readByte());
        assertEquals(RedisConstants.LF_BYTE,buf.readByte());
        assertFalse(buf.isReadable());


        assertNull(channel.readInbound());
    }
}