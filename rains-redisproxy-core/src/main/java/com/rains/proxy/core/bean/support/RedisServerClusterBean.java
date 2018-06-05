/**
 *
 */
package com.rains.proxy.core.bean.support;


import com.rains.proxy.core.cluster.LoadBalance;

import java.util.List;

/**
 * 一主多从
 *
 * @author liubing
 */
public class RedisServerClusterBean {

    private RedisServerBean redisServerMasterBean;//主

    private List<RedisServerBean> redisServerSlaveBeans;//从

    private LoadBalance loadClusterBalance;//从权重

    /**
     *
     */
    public RedisServerClusterBean() {
        super();
    }

    /**
     * @return the loadClusterBalance
     */
    public LoadBalance getLoadClusterBalance() {
        return loadClusterBalance;
    }

    /**
     * @param loadClusterBalance the loadClusterBalance to set
     */
    public void setLoadClusterBalance(LoadBalance loadClusterBalance) {
        this.loadClusterBalance = loadClusterBalance;
    }

    /**
     * @return the redisRedisServerMasterBean
     */
    public RedisServerBean getRedisServerMasterBean() {
        return redisServerMasterBean;
    }


    public void setRedisServerMasterBean(
            RedisServerBean redisServerMasterBean) {
        this.redisServerMasterBean = redisServerMasterBean;
    }

    /**
     * @return the redisServerSlaveBeans
     */
    public List<RedisServerBean> getRedisServerSlaveBeans() {
        return redisServerSlaveBeans;
    }

    /**
     * @param redisServerSlaveBeans the redisServerSlaveBeans to set
     */
    public void setRedisServerSlaveBeans(
            List<RedisServerBean> redisServerSlaveBeans) {
        this.redisServerSlaveBeans = redisServerSlaveBeans;
    }


}
