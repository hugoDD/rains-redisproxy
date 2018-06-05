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
import com.rains.proxy.core.utils.ProtoUtils;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
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
public class RedisServerHandler extends ChannelInboundHandlerAdapter {

    private Logger logger = LoggerFactory.getLogger(RedisServerHandler.class);


    private Map<String, AbstractPoolClient> ffanRedisServerBeanMap;

    private RedisServerMasterCluster ffanRedisServerMasterCluster;

    private RedisCommandControl redisCommandControl;


    public RedisServerHandler(Map<String, AbstractPoolClient> ffanRedisServerBeanMap, RedisServerMasterCluster ffanRedisServerMasterCluster) {
        this.ffanRedisServerMasterCluster = ffanRedisServerMasterCluster;
        this.ffanRedisServerBeanMap = ffanRedisServerBeanMap;
        redisCommandControl = new RedisCommandControl(ffanRedisServerBeanMap, ffanRedisServerMasterCluster);
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
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception  {
        super.channelRead(ctx,msg);
        Assert.notNull(msg, "handle request command must not null");
        if (logger.isDebugEnabled()) {
            logger.debug("handle request command : {}", msg);
        }


        IRedisReply redisReply = redisCommandControl.action((RedisCommand)msg,ctx);
        if (redisReply.getType() != Type.EMPTY) {
            ctx.writeAndFlush(redisReply);

        }
//        if (request.getPolicy().isNotThrough()) {
//            IRedisReply redisReply = redisCommandControl.action(request);
//            ctx.writeAndFlush(redisReply);
//        } else {
//            redisCommandControl.setCtx(ctx);
//            IRedisReply redisReply = redisCommandControl.action(request);
//            if (redisReply.getType() != Type.EMPTY) {
//                ctx.writeAndFlush(redisReply);
//            }

//                    if(request!=null&&request.getArgs().size()>1&&!command.equals(RedisConstants.KEYS)){//第一个是命令，第二个是key
//
//                        RedisCommandEnums commandEnums=getRedisCommandEnums(command);
//
//                        if(commandEnums!=null&&commandEnums.isIswrite()){//主
//                            AbstractPoolClient ffanRedisClient=getShardFfanRedisClient(request,command);//默认一致性hash算法
//                            ffanRedisClient.write(request,ctx);
//                        }else if(commandEnums!=null&&!commandEnums.isIswrite()){//从
//                            AbstractPoolClient ffanRedisClient=getShardClusterFfanRedisClient(request,command,commandEnums.isIswrite());//权重算法
//                            ffanRedisClient.write(request,ctx);
//                        }
//                    }else if(request!=null&&request.getArgs().size()>1&&command.equals(RedisConstants.KEYS)){//keys 级别 找主
//                        if(ffanRedisServerMasterCluster.getMasters().size()==1){
//                            AbstractPoolClient ffanRedisClient=ffanRedisServerBeanMap.get(ffanRedisServerMasterCluster.getMasters().get(0).getKey());
//                            ffanRedisClient.write(request,ctx);
//                        }else{
//                            ctx.channel().writeAndFlush(new ErrorRedisReply(ProtoUtils.buildErrorReplyBytes("not support command:"+command)));
//                        }
//
//                    }
                    /*else if(command.equals(RedisConstants.INFO)){//info 级别
			    	if(ffanRedisServerBeanMap.size()==1){
			    		for(String key:ffanRedisServerBeanMap.keySet()){
				    		AbstractPoolClient ffanRedisClient=ffanRedisServerBeanMap.get(key);
				    		ffanRedisClient.write(request,ctx);
				    	}
			    	}else{
				    	ctx.channel().writeAndFlush(new ErrorRedisReply(ProtoUtils.buildErrorReplyBytes("not support command:"+command)));
			    	}
			    }else{
                        ctx.channel().writeAndFlush(new ErrorRedisReply(ProtoUtils.buildErrorReplyBytes("unknown command:"+command)));
                    }*/

      //  }


    }

    /**
     * 根据参数获取枚举类
     * @param command
     * @return
     */
    private RedisCommandEnums getRedisCommandEnums(String command) {
        try {
            return RedisCommandEnums.valueOf(command.toUpperCase());
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 读写分离， 从采用权重算法
     * @param request
     * @param command
     * @return
     */
    private AbstractPoolClient getShardClusterFfanRedisClient(RedisCommand request, String command, boolean flag) {
        RedisQuestBean redisQuestBean = new RedisQuestBean(new String(request.getArgs().get(0)), request.getArgs().get(1), true);
        LoadBalance loadMasterBalance = ffanRedisServerMasterCluster.getLoadMasterBalance();
        RedisServerBean ffanRedisServerBean = loadMasterBalance.select(redisQuestBean, null);
        List<RedisServerBean> ffanRedisServerBeans = ffanRedisServerMasterCluster.getMasterFfanRedisServerBean(ffanRedisServerBean.getKey());
        if (ffanRedisServerBeans != null && ffanRedisServerBeans.size() > 0) {
            RedisServerClusterBean redisServerClusterBean = ffanRedisServerMasterCluster.getRedisServerClusterBean(ffanRedisServerBean.getKey());
            if (redisServerClusterBean != null) {
                LoadBalance loadClusterBalance = redisServerClusterBean.getLoadClusterBalance();
                loadClusterBalance.setFfanRedisServerMasterCluster(ffanRedisServerMasterCluster);
                redisQuestBean.setWrite(flag);
                RedisServerBean ffanClusterRedisServerBean = loadClusterBalance.select(redisQuestBean, ffanRedisServerBean);
                if (ffanClusterRedisServerBean != null) {
                    String key = ffanClusterRedisServerBean.getKey();
                    return ffanRedisServerBeanMap.get(key);
                }
            }
        }
        String key = ffanRedisServerBean.getKey();
        return ffanRedisServerBeanMap.get(key);

    }

    /**
     * 一致性hash算法 主
     * @param request
     * @return
     */
    private AbstractPoolClient getShardFfanRedisClient(RedisCommand request, String command) {
        RedisQuestBean redisQuestBean = new RedisQuestBean(new String(request.getArgs().get(0)), request.getArgs().get(1), true);
        String key = null;
        if (ffanRedisServerMasterCluster.getMasters().size() == 1) {
            key = ffanRedisServerMasterCluster.getMasters().get(0).getKey();
        } else {
            LoadBalance loadMasterBalance = ffanRedisServerMasterCluster.getLoadMasterBalance();
            RedisServerBean ffanRedisServerBean = loadMasterBalance.select(redisQuestBean, null);
            key = ffanRedisServerBean.getKey();
        }

        return ffanRedisServerBeanMap.get(key);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        super.channelInactive(ctx);
        // destroy();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        if (cause instanceof IOException) {
            String message = cause.getMessage();
            if (message != null && "远程主机强迫关闭了一个现有的连接。".equals(message)) {
                logger.warn("Client closed!");
            } else {
                logger.error("出错，客户端关闭连接", cause);
            }

        } else {
            logger.error("出错，关闭连接", cause);
            ctx.channel().writeAndFlush(new ErrorRedisReply(ProtoUtils.buildErrorReplyBytes("closed by upstream")));

        }
        if (ctx.channel().isOpen()) {
            ctx.channel().close();
        }

    }


}
