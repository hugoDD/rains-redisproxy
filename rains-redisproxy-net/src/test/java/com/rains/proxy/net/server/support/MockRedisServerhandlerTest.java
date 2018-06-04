package com.rains.proxy.net.server.support;

import com.rains.proxy.core.command.impl.RedisCommand;
import com.rains.proxy.core.enums.RedisCmdEnums;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.embedded.EmbeddedChannel;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * @author dourx
 * 2018年 06 月  01日  17:11
 * @version V1.0
 * TODO
 */
public class MockRedisServerhandlerTest {

    @Test
    public void handlerMock(){
        ByteBuf buf = Unpooled.buffer();;
        RedisCommand redisCommand = getRedisCommand("ping");
        EmbeddedChannel channel = new EmbeddedChannel(new MockRedisServerhandler());
        assertTrue(channel.writeInbound(redisCommand));
        assertTrue(channel.finish());
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