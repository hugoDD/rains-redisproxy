/**
 * 
 */
package com.rains.proxy.core.cluster.impl;



import com.rains.proxy.core.bean.RedisServerMasterCluster;
import com.rains.proxy.core.bean.support.RedisServerBean;
import com.rains.proxy.core.cluster.LoadBalance;
import com.rains.proxy.core.cluster.impl.support.RedisQuestBean;

import java.util.List;

/**
 * @author liubing
 *
 */
public abstract class AbstractLoadBalance implements LoadBalance {
	

	
	private RedisServerMasterCluster ffanRedisServerMasterCluster;
	
	
	/**
	 *
	 */
	public AbstractLoadBalance() {
		super();
	}

	/* (non-Javadoc)
	 * @see com.wanda.ffan.redis.proxy.core.cluster.LoadBalance#onRefresh(com.wanda.ffan.redis.proxy.core.config.FfanRedisServerMasterCluster)
	 */
	@Override
	public void onRefresh(
			RedisServerMasterCluster ffanRedisServerMasterCluster) {
		this.ffanRedisServerMasterCluster=ffanRedisServerMasterCluster;
	}

	/* (non-Javadoc)
	 * @see com.wanda.ffan.redis.proxy.core.cluster.LoadBalance#select(com.wanda.ffan.redis.proxy.core.cluster.impl.support.RedisQuestBean)
	 */
	@Override
	public RedisServerBean select(RedisQuestBean redisQuestBean, RedisServerBean ffanRedisMasterServer) {
		if(redisQuestBean.isWrite()&&ffanRedisMasterServer==null){//写
			List<RedisServerBean> ffanRedisServerBeans=ffanRedisServerMasterCluster.getMasters();
			if(ffanRedisServerBeans.size()>1){//默认第一个
				RedisServerBean ffanRedisServerBean=doSelect(redisQuestBean,ffanRedisServerBeans);
				return ffanRedisServerBean;
			}else if(ffanRedisServerBeans.size()==1){
				return ffanRedisServerBeans.get(0);
			}
		}else if(!redisQuestBean.isWrite()&&ffanRedisMasterServer!=null){//选取从
			List<RedisServerBean> ffanRedisClusterServerBeans=ffanRedisServerMasterCluster.getMasterFfanRedisServerBean(ffanRedisMasterServer.getKey());
			if(ffanRedisClusterServerBeans.size()>1){//默认第一个
				RedisServerBean ffanRedisServerBean=doSelect(redisQuestBean,ffanRedisClusterServerBeans);
				return ffanRedisServerBean;
			}else if(ffanRedisClusterServerBeans.size()==1){
				return ffanRedisClusterServerBeans.get(0);
			}
		}
		return null;
	}

	/**
	 * @return the ffanRedisServerMasterCluster
	 */
	public RedisServerMasterCluster getFfanRedisServerMasterCluster() {
		return ffanRedisServerMasterCluster;
	}

	/**
	 * @param ffanRedisServerMasterCluster the ffanRedisServerMasterCluster to set
	 */
	public void setFfanRedisServerMasterCluster(
			RedisServerMasterCluster ffanRedisServerMasterCluster) {
		this.ffanRedisServerMasterCluster = ffanRedisServerMasterCluster;
	}
	
	/**
	 * 选取策略
	 * @param redisQuestBean
	 * @return
	 */
	protected abstract RedisServerBean doSelect(RedisQuestBean redisQuestBean, List<RedisServerBean> ffanRedisMasterServers);
	
}
