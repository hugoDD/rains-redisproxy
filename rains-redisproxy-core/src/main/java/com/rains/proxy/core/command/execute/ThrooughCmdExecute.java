package com.rains.proxy.core.command.execute;

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
import org.springframework.util.Assert;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author dourx
 * 2018年 05 月  30日  18:02
 * @version V1.0
 * TODO
 */
public  class ThrooughCmdExecute implements ICmdExecute {

    protected RedisServerMasterCluster redisServerMasterCluster;
    protected Map<String, AbstractPoolClient> redisServerBeanMap;
    protected ChannelHandlerContext ctx;

    private EmptyRedisReply emptyRedisReply = new EmptyRedisReply();

    public static Map<RedisCmdTypeEnums, ThrooughCmdExecute> cmdExecute = new ConcurrentHashMap<>();

    public ThrooughCmdExecute(Map<String, AbstractPoolClient> redisServerBeanMap, RedisServerMasterCluster redisServerMasterCluster) {
        this.redisServerMasterCluster = redisServerMasterCluster;
        this.redisServerBeanMap = redisServerBeanMap;
    }


    @Override
    public IRedisReply execute(RedisCommand request) {
        Assert.notNull(ctx, "ctx must not null");

        String cmd = new String(request.getArgs().get(0));

        boolean isExecute = doExecute(cmd.toUpperCase(), request);
        if (isExecute) {
            return emptyRedisReply;
        }

        RedisRequestPolicy policy = request.getPolicy();
        if (policy.isRead()) {//从
            AbstractPoolClient ffanRedisClient = getShardClusterFfanRedisClient(request, cmd, false);//权重算法
            ffanRedisClient.write(request, ctx);
        } else {//主
            AbstractPoolClient ffanRedisClient = getShardFfanRedisClient(request, cmd);//默认一致性hash算法
            ffanRedisClient.write(request, ctx);
        }

        return emptyRedisReply;
    }

    protected boolean doExecute(String cmd, RedisCommand redisCommand) {
        return false;
    }




    /**
     * 一致性hash算法 主
     * @param request
     * @return
     */
    private AbstractPoolClient getShardFfanRedisClient(RedisCommand request,String command){
        RedisQuestBean redisQuestBean=new RedisQuestBean(new String(request.getArgs().get(0)), request.getArgs().get(1), true);
        String  key=null;
        if(redisServerMasterCluster.getMasters().size()==1){
            key=redisServerMasterCluster.getMasters().get(0).getKey();
        }else{
            LoadBalance loadMasterBalance=redisServerMasterCluster.getLoadMasterBalance();
            RedisServerBean ffanRedisServerBean=loadMasterBalance.select(redisQuestBean, null);
            key=ffanRedisServerBean.getKey();
        }

        return redisServerBeanMap.get(key);
    }

    /**
     * 读写分离， 从采用权重算法
     * @param request
     * @param command
     * @return
     */
    private AbstractPoolClient getShardClusterFfanRedisClient(RedisCommand request,String command,boolean flag){
        RedisQuestBean redisQuestBean=new RedisQuestBean(new String(request.getArgs().get(0)), request.getArgs().get(1),true );
        LoadBalance loadMasterBalance=redisServerMasterCluster.getLoadMasterBalance();
        RedisServerBean ffanRedisServerBean=loadMasterBalance.select(redisQuestBean, null);
        List<RedisServerBean> ffanRedisServerBeans=redisServerMasterCluster.getMasterFfanRedisServerBean(ffanRedisServerBean.getKey());
        if(ffanRedisServerBeans!=null&&ffanRedisServerBeans.size()>0){
            RedisServerClusterBean redisServerClusterBean= redisServerMasterCluster.getRedisServerClusterBean(ffanRedisServerBean.getKey());
            if(redisServerClusterBean!=null){
                LoadBalance loadClusterBalance=redisServerClusterBean.getLoadClusterBalance();
                loadClusterBalance.setFfanRedisServerMasterCluster(redisServerMasterCluster);
                redisQuestBean.setWrite(flag);
                RedisServerBean ffanClusterRedisServerBean=loadClusterBalance.select(redisQuestBean, ffanRedisServerBean);
                if(ffanClusterRedisServerBean!=null){
                    String key=ffanClusterRedisServerBean.getKey();
                    return redisServerBeanMap.get(key);
                }
            }
        }
        String key=ffanRedisServerBean.getKey();
        return redisServerBeanMap.get(key);

    }

    public void setCtx(ChannelHandlerContext ctx){
        this.ctx = ctx;
    }



}
