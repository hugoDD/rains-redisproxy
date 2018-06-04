package com.rains.proxy.core.command.handler;

import com.rains.proxy.core.command.ICmdExecute;
import com.rains.proxy.core.command.IRedisCommandHander;
import com.rains.proxy.core.command.execute.ConnectionCmdExecute;
import com.rains.proxy.core.command.impl.RedisCommand;
import com.rains.proxy.core.reply.IRedisReply;

/**
 * @author dourx
 * 2018年 05 月  30日  11:03
 * @version V1.0
 * 由中间处理，直接返回结果，不透传到redis处理
 * 有如下主要命令:
 *	// Connection
 * 		_cmds.put("AUTH", 				new RedisRequestPolicy(COMMON_CMD, NO_THROUGH_CMD, READ_CMD));
 * 		_cmds.put("ECHO", 				new RedisRequestPolicy(COMMON_CMD, NO_THROUGH_CMD, READ_CMD));
 * 		_cmds.put("PING", 				new RedisRequestPolicy(COMMON_CMD, NO_THROUGH_CMD, READ_CMD));
 * 		_cmds.put("QUIT", 				new RedisRequestPolicy(COMMON_CMD, NO_THROUGH_CMD, READ_CMD));
 * 		_cmds.put("SELECT", 			new RedisRequestPolicy(COMMON_CMD, NO_THROUGH_CMD, READ_CMD));
 */
public class NoThrooughCmdHandler implements IRedisCommandHander<RedisCommand> {
    private ICmdExecute connectionCmdExecute = new ConnectionCmdExecute();

    @Override
    public IRedisReply hander(RedisCommand command) {
        return connectionCmdExecute.execute(command);
    }
}
