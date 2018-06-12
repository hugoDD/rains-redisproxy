/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.rains.proxy.net.server.support;


import com.rains.proxy.core.bean.RedisServerMasterCluster;
import com.rains.proxy.core.bean.support.RedisServerBean;
import com.rains.proxy.core.bean.support.RedisServerClusterBean;
import com.rains.proxy.core.client.impl.AbstractPoolClient;
import com.rains.proxy.core.cluster.LoadBalance;
import com.rains.proxy.core.cluster.impl.support.RedisQuestBean;
import com.rains.proxy.core.command.RedisCommandControl;
import com.rains.proxy.core.command.impl.RedisCommand;
import com.rains.proxy.core.enums.RedisCommandEnums;
import com.rains.proxy.core.enums.Type;
import com.rains.proxy.core.reply.IRedisReply;
import com.rains.proxy.core.reply.impl.ErrorRedisReply;
import com.rains.proxy.core.reply.impl.StatusRedisReply;
import com.rains.proxy.core.utils.ProtoUtils;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import java.io.IOException;
import java.util.List;
import java.util.Map;


/**
 * @author dourx
 * @version V1.0
 * @Description: redis服务端回答
 * @date 2018/5/24  11:43
 */
public class RedisServerHandler extends SimpleChannelInboundHandler<RedisCommand> {

    private Logger logger = LoggerFactory.getLogger(RedisServerHandler.class);


    private Map<String, AbstractPoolClient> redisServerBeanMap;

    private RedisServerMasterCluster redisServerMasterCluster;

    private RedisCommandControl redisCommandControl;


    public RedisServerHandler(Map<String, AbstractPoolClient> redisServerBeanMap, RedisServerMasterCluster redisServerMasterCluster) {
        this.redisServerMasterCluster = redisServerMasterCluster;
        this.redisServerBeanMap = redisServerBeanMap;
        redisCommandControl = new RedisCommandControl(redisServerBeanMap, redisServerMasterCluster);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        if (logger.isDebugEnabled()) {
            logger.debug("channelReadComplete : {}",ctx.name());
        }

        super.channelReadComplete(ctx);
    }

    /**
     * 接受请求
     */
//    @Override
//    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception  {
//        super.channelRead(ctx,msg);
//        Assert.notNull(msg, "handle request command must not null");
//        if (logger.isDebugEnabled()) {
//            logger.debug("handle request command : {}", msg);
//        }
//
//
//        IRedisReply redisReply = redisCommandControl.action((RedisCommand)msg,ctx);
//        if (redisReply.getType() != Type.EMPTY) {
//            ctx.writeAndFlush(redisReply,ctx.voidPromise());
//
//        }
////        if (request.getPolicy().isNotThrough()) {
////            IRedisReply redisReply = redisCommandControl.action(request);
////            ctx.writeAndFlush(redisReply);
////        } else {
////            redisCommandControl.setCtx(ctx);
////            IRedisReply redisReply = redisCommandControl.action(request);
////            if (redisReply.getType() != Type.EMPTY) {
////                ctx.writeAndFlush(redisReply);
////            }
//
////                    if(request!=null&&request.getArgs().size()>1&&!command.equals(RedisConstants.KEYS)){//第一个是命令，第二个是key
////
////                        RedisCommandEnums commandEnums=getRedisCommandEnums(command);
////
////                        if(commandEnums!=null&&commandEnums.isIswrite()){//主
////                            AbstractPoolClient ffanRedisClient=getShardFfanRedisClient(request,command);//默认一致性hash算法
////                            ffanRedisClient.write(request,ctx);
////                        }else if(commandEnums!=null&&!commandEnums.isIswrite()){//从
////                            AbstractPoolClient ffanRedisClient=getShardClusterFfanRedisClient(request,command,commandEnums.isIswrite());//权重算法
////                            ffanRedisClient.write(request,ctx);
////                        }
////                    }else if(request!=null&&request.getArgs().size()>1&&command.equals(RedisConstants.KEYS)){//keys 级别 找主
////                        if(redisServerMasterCluster.getMasters().size()==1){
////                            AbstractPoolClient ffanRedisClient=redisServerBeanMap.get(redisServerMasterCluster.getMasters().get(0).getKey());
////                            ffanRedisClient.write(request,ctx);
////                        }else{
////                            ctx.channel().writeAndFlush(new ErrorRedisReply(ProtoUtils.buildErrorReplyBytes("not support command:"+command)));
////                        }
////
////                    }
//                    /*else if(command.equals(RedisConstants.INFO)){//info 级别
//			    	if(redisServerBeanMap.size()==1){
//			    		for(String key:redisServerBeanMap.keySet()){
//				    		AbstractPoolClient ffanRedisClient=redisServerBeanMap.get(key);
//				    		ffanRedisClient.write(request,ctx);
//				    	}
//			    	}else{
//				    	ctx.channel().writeAndFlush(new ErrorRedisReply(ProtoUtils.buildErrorReplyBytes("not support command:"+command)));
//			    	}
//			    }else{
//                        ctx.channel().writeAndFlush(new ErrorRedisReply(ProtoUtils.buildErrorReplyBytes("unknown command:"+command)));
//                    }*/
//
//      //  }
//
//
//    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RedisCommand msg) throws Exception {
        Assert.notNull(msg, "handle request command must not null");
        if (logger.isDebugEnabled()) {
            logger.debug("handle request command : {}", msg);
        }


        IRedisReply redisReply = redisCommandControl.action(msg,ctx);
        if (redisReply.getType() != Type.EMPTY) {
            ctx.writeAndFlush(redisReply,ctx.voidPromise());

        }
    }


    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        super.channelInactive(ctx);
        // destroy();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        if (cause instanceof IOException) {
            String message = cause.getMessage();
            if (message != null && "远程主机强迫关闭了一个现有的连接。".equals(message)) {
                ctx.channel().writeAndFlush(new StatusRedisReply("OK".getBytes()));
                logger.warn("Client closed!");
            } else {
                logger.error("出错，客户端关闭连接", cause);
            }

        } else {
            logger.error("出错，关闭连接", cause);
            ctx.channel().writeAndFlush(new ErrorRedisReply(ProtoUtils.buildErrorReplyBytes("closed by upstream")));

        }
        if (ctx.channel().isOpen()) {
            logger.warn("ctx channel closed!");
            ctx.channel().close();
        }

    }


}
