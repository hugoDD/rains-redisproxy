/**
 * 
 */
package com.rains.proxy.core.cluster;


import com.rains.proxy.core.bean.LBRedisServerMasterCluster;
import com.rains.proxy.core.bean.support.LBRedisServerBean;
import com.rains.proxy.core.cluster.impl.support.RedisQuestBean;

/**
 * 均衡轮询接口
 * @author liubing
 *
 */
public interface LoadBalance {
	
	/**
	 * 刷新
	 */
	public void onRefresh(LBRedisServerMasterCluster ffanRedisServerMasterCluster);
	
	/**
	 * 选取策略
	 * @param redisQuestBean
	 * @return
	 */
	public LBRedisServerBean select(RedisQuestBean redisQuestBean, LBRedisServerBean ffanRedisMasterServer);
	
	/**
	 * 设置
	 * @param ffanRedisServerMasterCluster
	 */
	public void setFfanRedisServerMasterCluster(LBRedisServerMasterCluster ffanRedisServerMasterCluster); 
}
