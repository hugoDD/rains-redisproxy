package com.rains.proxy.net.server.support;

import com.rains.proxy.core.bean.LBRedisServerMasterCluster;
import com.rains.proxy.core.command.impl.RedisCommand;
import com.rains.proxy.core.enums.RedisCmdEnums;
import com.rains.proxy.core.enums.Type;
import com.rains.proxy.core.protocol.RedisReplyEncoder;
import com.rains.proxy.core.protocol.RedisRequestDecoder;
import com.rains.proxy.core.reply.IRedisReply;
import com.rains.proxy.core.reply.impl.StatusRedisReply;
import com.rains.proxy.net.RedisProxyConfigurationTest;
import com.rains.proxy.net.server.LBRedisServer;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.embedded.EmbeddedChannel;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * @author dourx
 * @version V1.0
 * @Description: TODO
 * @date 2018年 05 月  24日  11:37
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = RedisProxyConfigurationTest.class)
public class LBRedisServerHandlerTest {

    @Autowired
    private LBRedisServerMasterCluster redisServerMasterCluster;

    @Before
    public void setUp() throws Exception {
        LBRedisServer redisServer = new LBRedisServer(redisServerMasterCluster);
    }



    @Test
    public void handler() {
        //set mykey myvalue
        ByteBuf buf = Unpooled.buffer();;
        RedisCommand redisCommand = getRedisCommand("ping");
//        RedisCommand redisCommand = getRedisCommand("info");


        EmbeddedChannel channel = new EmbeddedChannel(new RedisServerHandler(redisServerMasterCluster.getRedisClientBeanMap(),redisServerMasterCluster));
        assertTrue(channel.writeInbound(redisCommand));
        assertTrue(channel.finish());
        IRedisReply redisReply =channel.readInbound();
        assertNotNull(redisReply);
        assertTrue(redisReply instanceof StatusRedisReply);

         redisCommand = getRedisCommand("info");
         channel = new EmbeddedChannel(new RedisServerHandler(redisServerMasterCluster.getRedisClientBeanMap(),redisServerMasterCluster));
        assertTrue(channel.writeInbound(redisCommand));
        assertTrue(channel.finish());
         redisReply =channel.readInbound();
        assertNotNull(redisReply);
        assertTrue(redisReply instanceof StatusRedisReply);


       // assertEquals("ping ",redisCommand.toString());

//
//       ChannelFuture future= channel.writeAndFlush(redisCommand);
//       assertTrue(future.isDone());
//        future.addListener(new ChannelFutureListener() {
//            @Override
//            public void operationComplete(ChannelFuture channelFuture)
//                    throws Exception {
//                if (channelFuture.isSuccess()) {
//                    System.out.println("Connection established");
//                    IRedisReply out = channel.readInbound();
//                    assertNotNull(out);
//                    //assertTrue(out.getType()== Type.STATUS);
//                } else {
//                    System.err.println("Connection attempt failed");
//                    channelFuture.cause().printStackTrace();
//                }
//            }
//        });
 //      assertTrue(channel.finish());
//        //read
//        ByteBuf out = channel.readOutbound();
//        assertNotNull(out);


//        System.out.println(out.toString(Charset.forName("UTF-8")));
        //out.resetReaderIndex();
        //+OK
//        assertTrue(out.isReadable());
////        assertEquals('+',(char)out.readByte());
//        assertEquals('O',(char)out.readByte());
//        assertEquals('K',(char)out.readByte());
//        assertEquals(RedisConstants.CR_BYTE,out.readByte());
//        assertEquals(RedisConstants.LF_BYTE,out.readByte());
//
//        assertFalse(buf.isReadable());

    }

    private RedisCommand getRedisCommand(String command) {
        RedisCommand redisCommand = new RedisCommand();
        redisCommand.setArgCount(3);
        List<byte[]> args = new ArrayList<>();
        String[] subCmd = command.split(" ");
        redisCommand.setArgCount(subCmd.length);
        for(String sub : subCmd){
            args.add(sub.getBytes());

        }
        redisCommand.setArgs(args);
        String firstCmd = new String(args.get(0));

        redisCommand.setPolicy(RedisCmdEnums.getPolicy(firstCmd));
        return redisCommand;
    }


}