/**
 * 
 */
package com.rains.proxy.core.cluster.impl;

import com.rains.proxy.core.algorithm.impl.RoundRobinHash;
import com.rains.proxy.core.bean.support.LBRedisServerBean;
import com.rains.proxy.core.cluster.impl.support.RedisQuestBean;

import java.util.List;

/**
 * RoundRobin 权重算法
 * @author liubing
 *
 */
public class RoundRobinLoadBalance extends AbstractLoadBalance {

	public RoundRobinLoadBalance() {

	}

	/* (non-Javadoc)
	 * @see com.wanda.ffan.redis.proxy.core.cluster.impl.AbstractLoadBalance#doSelect(com.wanda.ffan.redis.proxy.core.cluster.impl.support.RedisQuestBean, java.util.List)
	 */
	@Override
	protected LBRedisServerBean doSelect(RedisQuestBean redisQuestBean,
			List<LBRedisServerBean> ffanRedisMasterServers) {
		RoundRobinHash<LBRedisServerBean> roundRobinHash=new RoundRobinHash<LBRedisServerBean>(ffanRedisMasterServers);
		LBRedisServerBean result=roundRobinHash.weightRandom();
		return result;
	}

}
