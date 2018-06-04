package com.rains.proxy.net.client.suppot;

import com.rains.proxy.core.command.impl.RedisCommand;
import com.rains.proxy.core.enums.Type;
import com.rains.proxy.core.protocol.RedisReplyDecoder;
import com.rains.proxy.core.protocol.RedisRequestEncoder;
import com.rains.proxy.core.reply.IRedisReply;
import com.rains.proxy.core.reply.impl.BulkRedisReply;
import com.rains.proxy.core.reply.impl.CommonRedisReply;
import com.rains.proxy.core.reply.impl.StatusRedisReply;
import com.rains.proxy.net.client.LBRedisConnection;
import com.rains.proxy.net.server.support.LBRedisServerHandler;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.embedded.EmbeddedChannel;
import org.junit.Test;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

/**
 * @author dourx
 * @version V1.0
 * @Description: TODO
 * @date 2018年 05 月  24日  19:02
 */
public class LBRedisClientOutHandlerTest {

    @Test
    public  void outhandler(){
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

        EmbeddedChannel channel = new EmbeddedChannel(new LBRedisClientOutHandler());

        assertTrue(channel.writeOutbound(redisCommand));

        assertTrue(channel.finish());

        RedisCommand msg = channel.readOutbound();
       assertNotNull(msg);
        assertEquals(msg.toString(),redisCommand.toString());
    }

    @Test
    public void outHandlerRequestEncoder(){

        //set mykey myvalue
        ByteBuf buf = Unpooled.buffer();;
        RedisCommand redisCommand = new RedisCommand();
        redisCommand.setArgCount(3);
        List<byte[]> args = new ArrayList<>();
        args.add("set".getBytes());
        args.add("mykey".getBytes());
        args.add("myvalue".getBytes());
        redisCommand.setArgs(args);



        EmbeddedChannel channel = new EmbeddedChannel(new RedisRequestEncoder(),new LBRedisClientOutHandler());



        assertTrue(channel.writeOutbound(redisCommand));


        assertTrue(channel.finish());

        ByteBuf msg = channel.readOutbound();
        assertNotNull(msg);
        assertEquals("*3\r\n$3\r\nset\r\n$5\r\nmykey\r\n$7\r\nmyvalue\r\n",msg.toString(Charset.forName("UTF-8")));




    }

    protected String readToStr(ByteBuf buf, int len){
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < len; i++) {
            sb.append((char)buf.readByte());
        }
        return sb.toString();
    }



}