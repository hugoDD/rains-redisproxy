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
public class LBRedisServerClusterBean {

    private LBRedisServerBean redisServerMasterBean;//主

    private List<LBRedisServerBean> redisServerSlaveBeans;//从

    private LoadBalance loadClusterBalance;//从权重

    /**
     *
     */
    public LBRedisServerClusterBean() {
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
    public LBRedisServerBean getRedisServerMasterBean() {
        return redisServerMasterBean;
    }


    public void setRedisServerMasterBean(
            LBRedisServerBean redisServerMasterBean) {
        this.redisServerMasterBean = redisServerMasterBean;
    }

    /**
     * @return the redisServerSlaveBeans
     */
    public List<LBRedisServerBean> getRedisServerSlaveBeans() {
        return redisServerSlaveBeans;
    }

    /**
     * @param redisServerSlaveBeans the redisServerSlaveBeans to set
     */
    public void setRedisServerSlaveBeans(
            List<LBRedisServerBean> redisServerSlaveBeans) {
        this.redisServerSlaveBeans = redisServerSlaveBeans;
    }


}
