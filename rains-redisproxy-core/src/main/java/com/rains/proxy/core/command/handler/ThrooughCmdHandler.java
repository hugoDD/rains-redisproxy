package com.rains.proxy.core.command.handler;

import com.rains.proxy.core.bean.LBRedisServerMasterCluster;
import com.rains.proxy.core.bean.support.LBRedisServerBean;
import com.rains.proxy.core.client.impl.AbstractPoolClient;
import com.rains.proxy.core.cluster.LoadBalance;
import com.rains.proxy.core.cluster.impl.support.RedisQuestBean;
import com.rains.proxy.core.command.IRedisCommandHander;
import com.rains.proxy.core.command.execute.KeyCmdExecute;
import com.rains.proxy.core.command.execute.ServerCmdExecute;
import com.rains.proxy.core.command.execute.ThrooughCmdExecute;
import com.rains.proxy.core.command.impl.RedisCommand;
import com.rains.proxy.core.command.impl.RedisRequestPolicy;
import com.rains.proxy.core.enums.RedisCmdEnums;
import com.rains.proxy.core.enums.RedisCmdTypeEnums;
import com.rains.proxy.core.enums.RedisCommandEnums;
import com.rains.proxy.core.reply.IRedisReply;
import com.rains.proxy.core.reply.impl.EmptyRedisReply;
import com.rains.proxy.core.reply.impl.ErrorRedisReply;
import com.rains.proxy.core.utils.ProtoUtils;
import io.netty.channel.ChannelHandlerContext;
import org.springframework.util.Assert;

import java.util.HashMap;
import java.util.Map;

/**
 * @author dourx
 * 2018年 05 月  30日  11:03
 * @version V1.0
 * TODO
 */
public class ThrooughCmdHandler implements IRedisCommandHander<RedisCommand> {
//    private LBRedisServerMasterCluster redisServerMasterCluster;
//    private Map<String, AbstractPoolClient> redisServerBeanMap;
    private ServerCmdExecute serverCmdExecute;

    private KeyCmdExecute keyCmdExecute;

    private ThrooughCmdExecute throoughCmdExecute ;

    private  ChannelHandlerContext ctx;


    public ThrooughCmdHandler() {
    }

    public ThrooughCmdHandler(Map<String, AbstractPoolClient> redisServerBeanMap, LBRedisServerMasterCluster redisServerMasterCluster) {
        serverCmdExecute = new ServerCmdExecute(redisServerBeanMap,redisServerMasterCluster);
        keyCmdExecute =  new KeyCmdExecute(redisServerBeanMap,redisServerMasterCluster);
        throoughCmdExecute = new ThrooughCmdExecute(redisServerBeanMap,redisServerMasterCluster);

    }

    @Override
    public IRedisReply hander(RedisCommand command) {
        String cmd =new String(command.getArgs().get(0));
        RedisCmdEnums redisCmdEnums = RedisCmdEnums.valueOf(cmd.toUpperCase());
        ThrooughCmdExecute cmdExecute= ThrooughCmdExecute.cmdExecute.get(redisCmdEnums.getType());
        if(cmdExecute==null){
            return throoughCmdExecute.execute(command);
        }

        return cmdExecute.execute(command);
    }




    public void setCtx(ChannelHandlerContext ctx){
        Assert.notNull(serverCmdExecute,"serverCmdExecute must not null");
        this.ctx=ctx;
        serverCmdExecute.setCtx(ctx);
        keyCmdExecute.setCtx(ctx);
        throoughCmdExecute.setCtx(ctx);
    }


}
