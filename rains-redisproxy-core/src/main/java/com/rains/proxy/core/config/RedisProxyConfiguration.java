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
package com.rains.proxy.core.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

/**
 * @author dourx
 * @version V1.0
 * @Descripio: TOO
  @date a年 a 月  a日  a
 */
@ConfigurationProperties(prefix = "redisproxy", ignoreUnknownFields = true)
public class RedisProxyConfiguration {


    private RedisProxyPoolConfig redisPool;



    private List<RedisProxyGroupNode> groupNode;


    public RedisProxyPoolConfig getRedisPool() {
        return redisPool;
    }

    public void setRedisPool(RedisProxyPoolConfig redisPool) {
        this.redisPool = redisPool;
    }

    public List<RedisProxyGroupNode> getGroupNode() {
        return groupNode;
    }

    public void setGroupNode(List<RedisProxyGroupNode> groupNode) {
        this.groupNode = groupNode;
    }


}
