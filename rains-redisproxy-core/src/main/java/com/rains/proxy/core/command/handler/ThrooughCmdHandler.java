package com.rains.proxy.core.command.handler;

import com.rains.proxy.bolt.domain.ClusterDomain;
import com.rains.proxy.core.bean.RedisServerMasterCluster;
import com.rains.proxy.core.client.impl.AbstractPoolClient;
import com.rains.proxy.core.command.IRedisCommandHander;
import com.rains.proxy.core.command.execute.KeyCmdExecute;
import com.rains.proxy.core.command.execute.ServerCmdExecute;
import com.rains.proxy.core.command.execute.ThrooughCmdExecute;
import com.rains.proxy.core.command.impl.RedisCommand;
import com.rains.proxy.core.enums.RedisCmdEnums;
import com.rains.proxy.core.reply.IRedisReply;
import io.netty.channel.ChannelHandlerContext;
import org.springframework.util.Assert;

import java.util.Map;

/**
 * @author dourx
 * 2018年 05 月  30日  11:03
 * @version V1.0
 * TODO
 */
public class ThrooughCmdHandler implements IRedisCommandHander<RedisCommand> {
    private ServerCmdExecute serverCmdExecute;

    private KeyCmdExecute keyCmdExecute;

    private ThrooughCmdExecute throoughCmdExecute ;

    private  ChannelHandlerContext ctx;


    public ThrooughCmdHandler() {
    }

    public ThrooughCmdHandler(ClusterDomain redisCluster) {
        serverCmdExecute = new ServerCmdExecute(redisCluster);
        keyCmdExecute =  new KeyCmdExecute(redisCluster);
        throoughCmdExecute = new ThrooughCmdExecute(redisCluster);

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
