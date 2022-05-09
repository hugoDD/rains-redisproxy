package com.rains.proxy.core.command.execute;

import com.rains.proxy.bolt.domain.ClusterDomain;
import com.rains.proxy.core.bean.RedisServerMasterCluster;
import com.rains.proxy.core.client.impl.AbstractPoolClient;
import com.rains.proxy.core.command.ICmdExecute;
import com.rains.proxy.core.command.impl.RedisCommand;
import com.rains.proxy.core.enums.RedisCmdTypeEnums;
import com.rains.proxy.core.reply.impl.ErrorRedisReply;
import com.rains.proxy.core.utils.ProtoUtils;
import com.rains.proxy.core.utils.StringUtils;

import java.util.Map;

/**
 * @author dourx
 * 2018年 05 月  30日  17:52
 * @version V1.0
 * TODO
 */
public class KeyCmdExecute extends ThrooughCmdExecute implements ICmdExecute {



    public KeyCmdExecute(ClusterDomain redisCluster) {
        super(redisCluster);
        cmdExecute.put(RedisCmdTypeEnums.Key,this);
    }

    @Override
    protected boolean doExecute(String cmd, RedisCommand redisCommand) {
        switch (cmd) {
            case "key":
                String host = redisCluster.select(redisCommand);
                if (StringUtils.isEmpty(host)) {
                    ctx.channel().writeAndFlush(new ErrorRedisReply(ProtoUtils.buildErrorReplyBytes("not support command:" + cmd)));
                }
               return true;
        }

        return false;
    }


}
