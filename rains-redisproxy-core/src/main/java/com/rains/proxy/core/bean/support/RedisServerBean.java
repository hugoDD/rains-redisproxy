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
package com.rains.proxy.core.bean.support;


import com.rains.proxy.core.algorithm.impl.support.RedisWeight;
import com.rains.proxy.core.config.RedisProxyPoolConfig;
import com.rains.proxy.core.constants.RedisConstants;

/**
 * @author dourx
 * @version V1.0
 * 创建日期 2018/6/5
 */
public class RedisServerBean implements RedisWeight {

    private String host;//主机名

    private int port;//端口号

    private RedisProxyPoolConfig redisPoolConfig;

    private int weight = 1;//默认权重比例为1


    /**
     *
     */
    public RedisServerBean() {
        super();
    }

    /**
     * @return the host
     */
    public String getHost() {
        return host;
    }

    /**
     * @param host the host to set
     */
    public void setHost(String host) {
        this.host = host;
    }

    /**
     * @return the port
     */
    public int getPort() {
        return port;
    }

    /**
     * @param port the port to set
     */
    public void setPort(int port) {
        this.port = port;
    }

    /**
     * 关键key
     *
     * @return
     */
    public String getKey() {
        StringBuffer sbBuffer = new StringBuffer();
        sbBuffer.append(RedisConstants.REDIS_PROXY).append(host).append(RedisConstants.SEPERATOR_ACCESS_LOG).append(port);
        return sbBuffer.toString();
    }

    public String getServerKey() {
        StringBuffer sbBuffer = new StringBuffer();
        sbBuffer.append(host).append(RedisConstants.PROTOCOL_SEPARATOR).append(port);
        return sbBuffer.toString();
    }

    @Override
    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    /**
     * @return the redisPoolConfig
     */
    public RedisProxyPoolConfig getRedisPoolConfig() {
        return redisPoolConfig;
    }

    /**
     * @param redisPoolConfig the redisPoolConfig to set
     */
    public void setRedisPoolConfig(RedisProxyPoolConfig redisPoolConfig) {
        this.redisPoolConfig = redisPoolConfig;
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
