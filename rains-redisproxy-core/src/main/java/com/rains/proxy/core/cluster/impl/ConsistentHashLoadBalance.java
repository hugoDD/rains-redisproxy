/**
 * 
 */
package com.rains.proxy.core.cluster.impl;


import com.rains.proxy.core.algorithm.Hashing;
import com.rains.proxy.core.algorithm.impl.ConsistentHash;
import com.rains.proxy.core.algorithm.impl.MurmurHash;
import com.rains.proxy.core.bean.support.RedisServerBean;
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
	protected RedisServerBean doSelect(RedisQuestBean redisQuestBean,
                                       List<RedisServerBean> ffanRedisMasterServers) {
		Hashing hashFunction = new MurmurHash(); // hash函数实例
		ConsistentHash<RedisServerBean> consistentHash=new ConsistentHash<RedisServerBean>(hashFunction, ffanRedisMasterServers.size(), ffanRedisMasterServers);
		RedisServerBean ffanRedisServerBean=consistentHash.getBytes(redisQuestBean.getKey());
		return ffanRedisServerBean;
	}
	
}
