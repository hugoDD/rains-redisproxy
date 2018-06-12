/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.rains.proxy.core.bean;


import com.rains.proxy.core.bean.support.RedisServerBean;
import com.rains.proxy.core.bean.support.RedisServerClusterBean;
import com.rains.proxy.core.client.impl.AbstractPoolClient;
import com.rains.proxy.core.cluster.LoadBalance;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



/**
 * @author dourx
 * @version V1.0
 * 创建日期 2018/6/5
 * 集群模式配置
 */
public class RedisServerMasterCluster implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 8481358070088941073L;

    private List<RedisServerClusterBean> redisServerClusterBeans = new ArrayList<RedisServerClusterBean>();

    private Map<String, RedisServerClusterBean> redisServerClusterBeanMap = new HashMap<String, RedisServerClusterBean>();// key 相当于 zk 路径,而不是值

    /**
     * 一主多从模式
     */
    private Map<String, List<RedisServerBean>> masterClusters = new HashMap<String, List<RedisServerBean>>();// key 相当于 zk 路径,而不是值

    /**
     * 主集合
     ***/
    private List<RedisServerBean> masters = new ArrayList<RedisServerBean>();

    private String redisProxyHost;//主机名

    private int redisProxyPort=6379;//端口号

    private LoadBalance loadMasterBalance;//主的一致性算法

    private Map<String, AbstractPoolClient> redisClientBeanMap = new HashMap<String, AbstractPoolClient>();//key 代表实际的值

    public RedisServerMasterCluster(
            List<RedisServerClusterBean> redisServerClusterBeans) {
        this.redisServerClusterBeans = redisServerClusterBeans;
        init();
    }

    /**
     * 初始化
     */
    private void init() {

        if (redisServerClusterBeans != null && redisServerClusterBeans.size() > 0) {
            for (RedisServerClusterBean ffanRedisServerClusterBean : redisServerClusterBeans) {
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
    public List<RedisServerBean> getMasterFfanRedisServerBean(String key) {
        if (masterClusters != null && masterClusters.containsKey(key)) {
            return masterClusters.get(key);
        }
        return null;
    }


    /**
     * @return the masters
     */
    public List<RedisServerBean> getMasters() {
        return masters;
    }


    /**
     * @param masters the masters to set
     */
    public void setMasters(List<RedisServerBean> masters) {
        this.masters = masters;
    }

    /**
     * @return the redisServerClusterBeans
     */
    public List<RedisServerClusterBean> getRedisServerClusterBeans() {
        return redisServerClusterBeans;
    }

    /**
     * @param redisServerClusterBeans the redisServerClusterBeans to set
     */
    public void setRedisServerClusterBeans(
            List<RedisServerClusterBean> redisServerClusterBeans) {
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
        List<RedisServerBean> redisServerSlaveBeans = masterClusters.get(key);
        for (RedisServerBean serverBean : redisServerSlaveBeans) {//删除不存在从的连接
            if (redisClientBeanMap.containsKey(serverBean.getKey())) {
                redisClientBeanMap.get(serverBean.getKey()).close();
                redisClientBeanMap.remove(serverBean.getKey());
            }
        }


        if (masterClusters.containsKey(key)) {//删除对应的主从
            masterClusters.remove(key);
        }

        for (RedisServerClusterBean redisServerClusterBean : redisServerClusterBeans) {//主存在，从不存在，删除对应的从
            if (redisServerClusterBean.getRedisServerMasterBean().getKey().equals(key)) {
                redisServerClusterBean.getRedisServerSlaveBeans().clear();
                break;
            }
        }
    }

    public RedisServerClusterBean getRedisServerClusterBean(String key) {
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
    public Map<String, RedisServerClusterBean> getRedisServerClusterBeanMap() {
        return redisServerClusterBeanMap;
    }

    /**
     * @param redisServerClusterBeanMap the ffanRedisServerClusterBeanMap to set
     */
    public void setRedisServerClusterBeanMap(
            Map<String, RedisServerClusterBean> redisServerClusterBeanMap) {
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
    public Map<String, List<RedisServerBean>> getMasterClusters() {
        return masterClusters;
    }


}
