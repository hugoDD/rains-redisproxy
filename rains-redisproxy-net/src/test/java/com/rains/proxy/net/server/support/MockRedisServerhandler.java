package com.rains.proxy.net.server.support;

import com.rains.proxy.core.command.impl.RedisCommand;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * @author dourx
 * 2018年 06 月  01日  17:09
 * @version V1.0
 * TODO
 */
@ChannelHandler.Sharable
public class MockRedisServerhandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        super.channelRead(ctx, msg);
    }


//    protected void channelRead0(ChannelHandlerContext channelHandlerContext, RedisCommand redisCommand) throws Exception {
//        channelHandlerContext.writeAndFlush(redisCommand);
//    }
}
