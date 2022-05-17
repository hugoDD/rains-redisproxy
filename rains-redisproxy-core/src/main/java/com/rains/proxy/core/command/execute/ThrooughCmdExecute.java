package com.rains.proxy.core.command.execute;

import com.alipay.remoting.InvokeCallback;
import com.alipay.remoting.exception.RemotingException;
import com.rains.proxy.bolt.client.RedisBoltClient;
import com.rains.proxy.bolt.client.RedisBoltClientFactory;
import com.rains.proxy.bolt.domain.ClusterDomain;
import com.rains.proxy.core.bean.RedisServerMasterCluster;
import com.rains.proxy.core.bean.support.RedisServerBean;
import com.rains.proxy.core.bean.support.RedisServerClusterBean;
import com.rains.proxy.core.client.impl.AbstractPoolClient;
import com.rains.proxy.core.cluster.LoadBalance;
import com.rains.proxy.core.cluster.impl.support.RedisQuestBean;
import com.rains.proxy.core.command.ICmdExecute;
import com.rains.proxy.core.command.impl.RedisCommand;
import com.rains.proxy.core.command.impl.RedisRequestPolicy;
import com.rains.proxy.core.enums.RedisCmdTypeEnums;
import com.rains.proxy.core.reply.IRedisReply;
import com.rains.proxy.core.reply.impl.EmptyRedisReply;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * @author dourx
 * 2018年 05 月  30日  18:02
 * @version V1.0
 * TODO
 */
public  class ThrooughCmdExecute implements ICmdExecute {
    private Logger logger = LoggerFactory.getLogger(ThrooughCmdExecute.class);
    protected ChannelHandlerContext ctx;
    protected ClusterDomain redisCluster;


    private EmptyRedisReply emptyRedisReply = new EmptyRedisReply();

    public static Map<RedisCmdTypeEnums, ThrooughCmdExecute> cmdExecute = new ConcurrentHashMap<>();

    public ThrooughCmdExecute(ClusterDomain redisCluster) {
       this.redisCluster = redisCluster;
    }


    @Override
    public IRedisReply execute(RedisCommand request) {
        Assert.notNull(ctx, "ctx must not null");

        String cmd = new String(request.getArgs().get(0));

        boolean isExecute = doExecute(cmd.toUpperCase(), request);
        if (isExecute) {
            return emptyRedisReply;
        }

        String redisHost = redisCluster.select(request);
        try {
            RedisBoltClientFactory.getClient().invokeWithCallback(redisHost,request, new InvokeCallback() {
                Executor executor = Executors.newCachedThreadPool();
                        @Override
                        public void onResponse(Object result) {
                            ctx.channel().eventLoop().execute(() -> {
                                // Not interested in the channel promise
                                logger.debug("msg is {}",result);
                                ctx.writeAndFlush(result, ctx.channel().voidPromise());
                            });
                        }

                        @Override
                        public void onException(Throwable e) {
                            logger.debug("msg is null :{}",e);
                        }

                        @Override
                        public Executor getExecutor() {
                            return executor;
                        }
                    },10000);
//                    .invokeSync(redisHost, request, 10000);
//            if(msg==null){
//                logger.debug("msg is null :{}",msg);
//                return emptyRedisReply;
//            }
            // Always write from the event loop, minimize the wakeup events
//            ctx.channel().eventLoop().execute(() -> {
//                // Not interested in the channel promise
//                logger.debug("msg is {}",msg);
//                ctx.writeAndFlush(msg, ctx.channel().voidPromise());
//            });
        } catch (RemotingException e) {
            logger.error("RemotingException write request Error :" , e);
        } catch (InterruptedException e) {
            logger.error("InterruptedException write request Error :" , e);
        }
       /* RedisRequestPolicy policy = request.getPolicy();
        if (policy.isRead()) {//从
            AbstractPoolClient ffanRedisClient = getShardClusterFfanRedisClient(request, cmd, false);//权重算法
            ffanRedisClient.write(request, ctx);
        } else {//主
            AbstractPoolClient ffanRedisClient = getShardFfanRedisClient(request, cmd);//默认一致性hash算法
            ffanRedisClient.write(request, ctx);
        }*/

      return emptyRedisReply;
    }

    protected boolean doExecute(String cmd, RedisCommand redisCommand) {

        return false;
    }






    public void setCtx(ChannelHandlerContext ctx){
        this.ctx = ctx;
    }



}
