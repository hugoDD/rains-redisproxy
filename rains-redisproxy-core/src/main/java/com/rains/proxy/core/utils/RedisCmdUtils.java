package com.rains.proxy.core.utils;

import com.rains.proxy.core.command.impl.CommandParse;
import com.rains.proxy.core.command.impl.RedisCommand;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import java.util.ArrayList;
import java.util.List;

/**
 * @author dourx
 * 2018年 05 月  30日  15:22
 * @version V1.0
 * TODO
 */
public class RedisCmdUtils {
    public static RedisCommand createCmd(String cmd){
        ByteBuf buf = Unpooled.buffer();;
        RedisCommand redisCommand = new RedisCommand();

        List<byte[]> args = new ArrayList<>();
        String[] commands =cmd.split(" ");
        redisCommand.setArgCount(commands.length);
        for(String command : commands){
            args.add(command.getBytes());
        }

        redisCommand.setArgs(args);
        redisCommand.setPolicy(CommandParse.getPolicy(commands[0].toUpperCase()));

        redisCommand.encode(buf);

        return  redisCommand;
    }
}
