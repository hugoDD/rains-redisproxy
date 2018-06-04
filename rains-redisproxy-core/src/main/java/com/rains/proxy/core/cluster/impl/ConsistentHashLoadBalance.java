/**
 * 
 */
package com.rains.proxy.core.cluster.impl;


import com.rains.proxy.core.algorithm.Hashing;
import com.rains.proxy.core.algorithm.impl.ConsistentHash;
import com.rains.proxy.core.algorithm.impl.MurmurHash;
import com.rains.proxy.core.bean.support.LBRedisServerBean;
import com.rains.proxy.core.cluster.impl.support.RedisQuestBean;

import java.util.List;

/**
 * 一致性hash
 * @author liubing
 *
 */
public class ConsistentHashLoadBalance extends AbstractLoadBalance {

	private final static int SLOT=1024;
	
	
	public ConsistentHashLoadBalance() {
		
	}
	
	/* (non-Javadoc)
	 * @see com.wanda.ffan.redis.proxy.core.cluster.impl.AbstractLoadBalance#doSelect(com.wanda.ffan.redis.proxy.core.cluster.impl.support.RedisQuestBean, java.util.List)
	 */
	@Override
	protected LBRedisServerBean doSelect(RedisQuestBean redisQuestBean,
										 List<LBRedisServerBean> ffanRedisMasterServers) {
		Hashing hashFunction = new MurmurHash(); // hash函数实例
		ConsistentHash<LBRedisServerBean> consistentHash=new ConsistentHash<LBRedisServerBean>(hashFunction, ffanRedisMasterServers.size(), ffanRedisMasterServers);
		LBRedisServerBean ffanRedisServerBean=consistentHash.getBytes(redisQuestBean.getKey());
		return ffanRedisServerBean;
	}
	
}
