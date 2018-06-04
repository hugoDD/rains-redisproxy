package com.rains.proxy.core.command;

import com.rains.proxy.core.command.impl.RedisCommand;
import com.rains.proxy.core.reply.IRedisReply;

/**
 * @author dourx
 * 2018年 05 月  30日  14:16
 * @version V1.0
 * TODO
 */
public interface ICmdExecute {
    IRedisReply execute(RedisCommand redisCommand);
}
