package com.rains.proxy.core.protocol.request.decoder;

import com.rains.proxy.core.command.impl.CommandParse;
import com.rains.proxy.core.command.impl.RedisCommand;
import com.rains.proxy.core.exception.RedisRequestException;
import com.rains.proxy.core.protocol.RedisRequestDecoder;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.embedded.EmbeddedChannel;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author dourx
 * @version V1.0
 * @Description: TODO
 * @date 2018年 05 月  29日  15:25
 */
public class BaseRedisRequestProtocolTest {
    @Test
    public void decoderAsterisk(){
        //set mykey myvalue
        String command = "*3\r\n$3\r\nset\r\n$5\r\nmykey\r\n$7\r\nmyvalue\r\n";
        RedisCommand redisCommand = decoderCommon(command);
        assertEquals(redisCommand.getArgCount(),3);
        assertEquals("set mykey myvalue ",redisCommand.toString());
        assertFalse(redisCommand.isInline());
        assertEquals(redisCommand.getPolicy().getCategory(), CommandParse.COMMON_CMD);
        assertEquals(redisCommand.getPolicy().getHandleType(), CommandParse.THROUGH_CMD);
        assertFalse(redisCommand.getPolicy().isRead());


    }
    @Test
    public void decoderPiplie(){
        //set mykey myvalue
        String command = "  *1\r\n$5\r\nMULTI\r\n*2\r\n$4\r\nLLEN\r\n$6\r\ncelery\r\n*1\r\n$4\r\nEXEC\r\n";
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
        assertEquals(redisCommand.getArgCount(),1);
        assertEquals("MULTI ",redisCommand.toString());
        assertFalse(redisCommand.isInline());
        assertEquals(redisCommand.getPolicy().getCategory(), CommandParse.DISABLED_CMD);
        assertEquals(redisCommand.getPolicy().getHandleType(), CommandParse.THROUGH_CMD);
        assertFalse(redisCommand.getPolicy().isRead());


         redisCommand = channel.readInbound();
        assertNotNull(redisCommand);
        assertEquals(redisCommand.getArgCount(),2);
        assertEquals("LLEN celery ",redisCommand.toString());
        assertFalse(redisCommand.isInline());
        assertEquals(redisCommand.getPolicy().getCategory(), CommandParse.COMMON_CMD);
        assertEquals(redisCommand.getPolicy().getHandleType(), CommandParse.THROUGH_CMD);
        assertTrue(redisCommand.getPolicy().isRead());

        redisCommand = channel.readInbound();
        assertNotNull(redisCommand);
        assertEquals(redisCommand.getArgCount(),1);
        assertEquals("EXEC ",redisCommand.toString());
        assertFalse(redisCommand.isInline());
        assertEquals(redisCommand.getPolicy().getCategory(), CommandParse.DISABLED_CMD);
        assertEquals(redisCommand.getPolicy().getHandleType(), CommandParse.THROUGH_CMD);
        assertFalse(redisCommand.getPolicy().isRead());


    }

    @Test
    public void decoderPing(){
        //ping
        String command = "ping\r\n";
        RedisCommand redisCommand = decoderCommon(command);
        assertEquals(redisCommand.getArgCount(),1);
        assertEquals("ping ",redisCommand.toString());
        assertTrue(redisCommand.isInline());
        assertEquals(redisCommand.getPolicy().getCategory(), CommandParse.COMMON_CMD);
        assertEquals(redisCommand.getPolicy().getHandleType(), CommandParse.NO_THROUGH_CMD);
        assertEquals(redisCommand.getPolicy().isRead(),true);

    }


    @Test
    public void decoderProtocolCmd() throws RedisRequestException {

        String command = "set test 4\r\ntest\r\n";
       // String command = "set test test\r\n";
        RedisCommand redisCommand = decoderCommon(command);
        assertEquals(redisCommand.getArgCount(),3);
        assertEquals("set test test ",redisCommand.toString());
        assertTrue(redisCommand.isInline());
        assertEquals(redisCommand.getPolicy().getCategory(), CommandParse.COMMON_CMD);
        assertEquals(redisCommand.getPolicy().getHandleType(), CommandParse.THROUGH_CMD);
        assertEquals(redisCommand.getPolicy().isRead(),false);


//        command = "set test 4 test\r\n";
//        byteBuf.writeBytes(command.getBytes());
//
//        redisCommand = protocol.decoderProtocol(byteBuf);
//        assertEquals(redisCommand.getArgCount(),1);
//        assertEquals("set test test ",redisCommand.toString());
//        assertTrue(redisCommand.isInline());
//        assertEquals(redisCommand.getPolicy().getCategory(), CommandParse.COMMON_CMD);
//        assertEquals(redisCommand.getPolicy().getHandleType(), CommandParse.THROUGH_CMD);
//        assertEquals(redisCommand.getPolicy().isRead(),false);
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