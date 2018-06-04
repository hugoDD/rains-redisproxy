package com.rains.proxy.core.protocol;

import com.rains.proxy.core.command.impl.RedisCommand;
import com.rains.proxy.core.reply.impl.AbstractRedisReplyTest;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.embedded.EmbeddedChannel;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author dourx
 * @version V1.0
 * @Description: request 解码单元测试
 * @date 2018年 05 月  24日  9:51
 */
public class RedisRequestDecoderTest extends AbstractRedisReplyTest {

    @Test
    public void decoderAsterisk(){
        //set mykey myvalue
        String command = "*3\r\n$3\r\nset\r\n$5\r\nmykey\r\n$7\r\nmyvalue\r\n";
        RedisCommand redisCommand = decoderCommon(command);
        assertEquals(redisCommand.getArgCount(),3);
        assertEquals("set mykey myvalue ",redisCommand.toString());


    }

    @Test
    public void decoderPing(){
        //ping
        String command = "PING\r\n";
        RedisCommand redisCommand = decoderCommon(command);
        assertEquals(redisCommand.getArgCount(),1);
        assertEquals("PING ",redisCommand.toString());
    }

    private RedisCommand decoderCommon(String command){
        ByteBuf buf = Unpooled.buffer();
        buf.writeBytes(command.getBytes());

        EmbeddedChannel channel = new EmbeddedChannel(new RedisRequestDecoder());

        //断言写入成功
        assertTrue(channel.writeInbound(buf));

        //断言写入写成
        assertTrue(channel.finish());

        //断言读取
        RedisCommand redisCommand = channel.readInbound();
        assertNotNull(redisCommand);

        return  redisCommand;
    }

}