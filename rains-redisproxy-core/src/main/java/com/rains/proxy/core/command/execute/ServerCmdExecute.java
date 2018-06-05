package com.rains.proxy.core.command.execute;

import com.rains.proxy.core.bean.RedisServerMasterCluster;
import com.rains.proxy.core.client.impl.AbstractPoolClient;
import com.rains.proxy.core.command.ICmdExecute;
import com.rains.proxy.core.command.impl.RedisCommand;
import com.rains.proxy.core.enums.RedisCmdTypeEnums;
import com.rains.proxy.core.reply.impl.ErrorRedisReply;
import com.rains.proxy.core.utils.ProtoUtils;

import java.util.Map;

/**
 * @author dourx
 * 2018年 05 月  30日  16:54
 * @version V1.0
 * 服务器cmd
 */
public class ServerCmdExecute extends ThrooughCmdExecute implements ICmdExecute {

    public ServerCmdExecute(Map<String, AbstractPoolClient> redisServerBeanMap, RedisServerMasterCluster redisServerMasterCluster) {
        super(redisServerBeanMap, redisServerMasterCluster);
        cmdExecute.put(RedisCmdTypeEnums.Server, this);

    }


    @Override
    protected boolean doExecute(String cmd, RedisCommand redisCommand) {
        switch (cmd) {
            case "INFO":
                return handler(cmd, redisCommand);
            case "COMMAND":
                return handler(cmd, redisCommand);

        }
        return false;
    }

    private boolean handler(String cmd, RedisCommand redisCommand) {
        if (redisServerMasterCluster != null && redisServerMasterCluster.getMasters() != null) {
            String key = redisServerMasterCluster.getMasters().get(0).getKey();
            AbstractPoolClient ffanRedisClient = redisServerBeanMap.get(key);
            ffanRedisClient.write(redisCommand, ctx);
        } else {
            ctx.channel().writeAndFlush(new ErrorRedisReply(ProtoUtils.buildErrorReplyBytes("not support command:" + cmd)));
        }
        return true;
    }


}
