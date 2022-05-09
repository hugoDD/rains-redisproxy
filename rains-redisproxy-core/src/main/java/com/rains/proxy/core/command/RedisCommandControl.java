package com.rains.proxy.core.command;

import com.rains.proxy.bolt.domain.ClusterDomain;
import com.rains.proxy.core.bean.RedisServerMasterCluster;
import com.rains.proxy.core.client.impl.AbstractPoolClient;
import com.rains.proxy.core.command.handler.NoThrooughCmdHandler;
import com.rains.proxy.core.command.handler.ThrooughCmdHandler;
import com.rains.proxy.core.command.impl.RedisCommand;
import com.rains.proxy.core.command.impl.RedisRequestPolicy;
import com.rains.proxy.core.log.impl.LoggerUtils;
import com.rains.proxy.core.reply.IRedisReply;
import com.rains.proxy.core.reply.impl.ErrorRedisReply;
import com.rains.proxy.core.utils.ProtoUtils;
import io.netty.channel.ChannelHandlerContext;
import org.springframework.util.Assert;

import java.util.Map;

import static com.rains.proxy.core.command.impl.CommandParse.DISABLED_CMD;

/**
 * @author dourx
 * 2018年 05 月  30日  10:46
 * @version V1.0
 * 命令模式调用者
 */
public class RedisCommandControl {
    private  IRedisCommandHander<RedisCommand> noThrooughCmdHandler ;
    private  IRedisCommandHander<RedisCommand> throoughCmdHandler;

    public RedisCommandControl() {
        noThrooughCmdHandler = new NoThrooughCmdHandler();
        throoughCmdHandler = new ThrooughCmdHandler();

    }

    public RedisCommandControl(ClusterDomain redisCluster) {
        noThrooughCmdHandler = new NoThrooughCmdHandler();
        throoughCmdHandler = new ThrooughCmdHandler(redisCluster);

    }
    public IRedisReply action(RedisCommand command){
       return action(command,null);
    }

    public IRedisReply action(RedisCommand command,ChannelHandlerContext ctx){
        Assert.notNull(command,"redis request command must not null");
        RedisRequestPolicy policy= command.getPolicy();
        IRedisReply redisReply;
        if(LoggerUtils.isDebugEnabled()){
            LoggerUtils.debug("redis command :{}",command.toString());
        }

        if(policy.getCategory() == DISABLED_CMD){
            return  new ErrorRedisReply(ProtoUtils.buildErrorReplyBytes("not support command:"+command));
        }

        if(policy.isNotThrough()){
            redisReply= noThrooughCmdHandler.hander(command);
        }else {
            setCtx(ctx);
            redisReply = throoughCmdHandler.hander(command);
        }

        return redisReply;
    }

    private void setCtx(ChannelHandlerContext ctx){
        Assert.notNull(throoughCmdHandler,"throoughCmdHandler must not null");
        Assert.notNull(ctx,"throoughCmdHandler must not set null ctx");
        ((ThrooughCmdHandler)throoughCmdHandler).setCtx(ctx);
    }

}
