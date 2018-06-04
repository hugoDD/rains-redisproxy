/**
 *
 */
package com.rains.proxy.core.bean;


import com.rains.proxy.core.bean.support.LBRedisServerBean;
import com.rains.proxy.core.bean.support.LBRedisServerClusterBean;
import com.rains.proxy.core.client.impl.AbstractPoolClient;
import com.rains.proxy.core.cluster.LoadBalance;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 多主
 * 集群模式配置
 *
 * @author liubing
 */
public class LBRedisServerMasterCluster implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 8481358070088941073L;

    private List<LBRedisServerClusterBean> redisServerClusterBeans = new ArrayList<LBRedisServerClusterBean>();

    private Map<String, LBRedisServerClusterBean> redisServerClusterBeanMap = new HashMap<String, LBRedisServerClusterBean>();// key 相当于 zk 路径,而不是值

    /**
     * 一主多从模式
     */
    private Map<String, List<LBRedisServerBean>> masterClusters = new HashMap<String, List<LBRedisServerBean>>();// key 相当于 zk 路径,而不是值

    /**
     * 主集合
     ***/
    private List<LBRedisServerBean> masters = new ArrayList<LBRedisServerBean>();

    private String redisProxyHost="127.0.0.1";//主机名

    private int redisProxyPort=6379;//端口号

    private LoadBalance loadMasterBalance;//主的一致性算法

    private Map<String, AbstractPoolClient> redisClientBeanMap = new HashMap<String, AbstractPoolClient>();//key 代表实际的值

    public LBRedisServerMasterCluster(
            List<LBRedisServerClusterBean> redisServerClusterBeans) {
        this.redisServerClusterBeans = redisServerClusterBeans;
        init();
    }

    /**
     * 初始化
     */
    private void init() {

        if (redisServerClusterBeans != null && redisServerClusterBeans.size() > 0) {
            for (LBRedisServerClusterBean ffanRedisServerClusterBean : redisServerClusterBeans) {
                masters.add(ffanRedisServerClusterBean.getRedisServerMasterBean());
                redisServerClusterBeanMap.put(ffanRedisServerClusterBean.getRedisServerMasterBean().getKey(), ffanRedisServerClusterBean);
                masterClusters.put(ffanRedisServerClusterBean.getRedisServerMasterBean().getKey(), ffanRedisServerClusterBean.getRedisServerSlaveBeans());
            }
        }

    }

    /**
     * 获取指定主的多从
     *
     * @param key
     * @return
     */
    public List<LBRedisServerBean> getMasterFfanRedisServerBean(String key) {
        if (masterClusters != null && masterClusters.containsKey(key)) {
            return masterClusters.get(key);
        }
        return null;
    }


    /**
     * @return the masters
     */
    public List<LBRedisServerBean> getMasters() {
        return masters;
    }


    /**
     * @param masters the masters to set
     */
    public void setMasters(List<LBRedisServerBean> masters) {
        this.masters = masters;
    }

    /**
     * @return the redisServerClusterBeans
     */
    public List<LBRedisServerClusterBean> getRedisServerClusterBeans() {
        return redisServerClusterBeans;
    }

    /**
     * @param redisServerClusterBeans the redisServerClusterBeans to set
     */
    public void setRedisServerClusterBeans(
            List<LBRedisServerClusterBean> redisServerClusterBeans) {
        this.redisServerClusterBeans = redisServerClusterBeans;
    }

    /**
     * 有主，无从
     *
     * @param key
     */
    public void removeSlavesByMaster(String key) {
        if (redisServerClusterBeanMap.containsKey(key)) {
            redisServerClusterBeanMap.remove(key);
        }
        List<LBRedisServerBean> redisServerSlaveBeans = masterClusters.get(key);
        for (LBRedisServerBean serverBean : redisServerSlaveBeans) {//删除不存在从的连接
            if (redisClientBeanMap.containsKey(serverBean.getKey())) {
                redisClientBeanMap.get(serverBean.getKey()).close();
                redisClientBeanMap.remove(serverBean.getKey());
            }
        }


        if (masterClusters.containsKey(key)) {//删除对应的主从
            masterClusters.remove(key);
        }

        for (LBRedisServerClusterBean redisServerClusterBean : redisServerClusterBeans) {//主存在，从不存在，删除对应的从
            if (redisServerClusterBean.getRedisServerMasterBean().getKey().equals(key)) {
                redisServerClusterBean.getRedisServerSlaveBeans().clear();
                break;
            }
        }
    }

    public LBRedisServerClusterBean getRedisServerClusterBean(String key) {
        if (redisServerClusterBeanMap.containsKey(key)) {
            return redisServerClusterBeanMap.get(key);
        }
        return null;
    }

    /**
     * @return the redisProxyHost
     */
    public String getRedisProxyHost() {
        return redisProxyHost;
    }

    /**
     * @param redisProxyHost the redisProxyHost to set
     */
    public void setRedisProxyHost(String redisProxyHost) {
        this.redisProxyHost = redisProxyHost;
    }

    /**
     * @return the redisProxyPort
     */
    public int getRedisProxyPort() {
        return redisProxyPort;
    }

    /**
     * @param redisProxyPort the redisProxyPort to set
     */
    public void setRedisProxyPort(int redisProxyPort) {
        this.redisProxyPort = redisProxyPort;
    }

    /**
     * @return the loadMasterBalance
     */
    public LoadBalance getLoadMasterBalance() {
        loadMasterBalance.setFfanRedisServerMasterCluster(this);
        return loadMasterBalance;
    }

    /**
     * @param loadMasterBalance the loadMasterBalance to set
     */
    public void setLoadMasterBalance(LoadBalance loadMasterBalance) {
        this.loadMasterBalance = loadMasterBalance;
    }

    /**
     * @return the ffanRedisServerClusterBeanMap
     */
    public Map<String, LBRedisServerClusterBean> getRedisServerClusterBeanMap() {
        return redisServerClusterBeanMap;
    }

    /**
     * @param redisServerClusterBeanMap the ffanRedisServerClusterBeanMap to set
     */
    public void setRedisServerClusterBeanMap(
            Map<String, LBRedisServerClusterBean> redisServerClusterBeanMap) {
        this.redisServerClusterBeanMap = redisServerClusterBeanMap;
    }

    /**
     * @return the ffanRedisClientBeanMap
     */
    public Map<String, AbstractPoolClient> getRedisClientBeanMap() {
        return redisClientBeanMap;
    }

    /**
     * @param redisClientBeanMap the ffanRedisClientBeanMap to set
     */
    public void setRedisClientBeanMap(Map<String, AbstractPoolClient> redisClientBeanMap) {
        this.redisClientBeanMap = redisClientBeanMap;
    }

    /**
     * @return the masterClusters
     */
    public Map<String, List<LBRedisServerBean>> getMasterClusters() {
        return masterClusters;
    }


}
