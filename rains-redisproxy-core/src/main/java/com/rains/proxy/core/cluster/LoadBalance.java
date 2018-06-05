/**
 * 
 */
package com.rains.proxy.core.cluster;


import com.rains.proxy.core.bean.RedisServerMasterCluster;
import com.rains.proxy.core.bean.support.RedisServerBean;
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
	public void onRefresh(RedisServerMasterCluster ffanRedisServerMasterCluster);
	
	/**
	 * 选取策略
	 * @param redisQuestBean
	 * @return
	 */
	public RedisServerBean select(RedisQuestBean redisQuestBean, RedisServerBean ffanRedisMasterServer);
	
	/**
	 * 设置
	 * @param ffanRedisServerMasterCluster
	 */
	public void setFfanRedisServerMasterCluster(RedisServerMasterCluster ffanRedisServerMasterCluster);
}
