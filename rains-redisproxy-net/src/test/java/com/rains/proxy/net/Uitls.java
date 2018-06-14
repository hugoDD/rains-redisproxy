package com.rains.proxy.net;

import com.rains.proxy.core.command.impl.RedisCommand;
import com.rains.proxy.core.enums.RedisCmdEnums;

import java.util.ArrayList;
import java.util.List;

/**
 * @author dourx
 * 2018年 06 月  13日  15:04
 * @version V1.0
 * TODO
 */
public class Uitls {

    public static RedisCommand getRedisCommand(String command) {
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
