/*
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
package com.rains.proxy.bolt.client;


import com.alipay.remoting.*;
import com.alipay.remoting.config.BoltGenericOption;
import com.alipay.remoting.log.BoltLoggerFactory;
import com.alipay.remoting.rpc.RpcClient;
import com.alipay.remoting.rpc.RpcConnectionEventHandler;
import com.alipay.remoting.rpc.protocol.UserProcessor;
import com.rains.proxy.bolt.protocol.RedisClientProtocol;
import com.rains.proxy.bolt.remoting.RedisBoltCommandFactory;
import com.rains.proxy.bolt.remoting.RedisBoltRemoting;
import org.slf4j.Logger;

import java.lang.reflect.Field;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Client for Rpc.
 *
 * @author jiangping
 * @version $Id: RpcClient.java, v 0.1 2015-9-23 PM4:03:28 tao Exp $
 */
public class RedisBoltClient extends RpcClient {

    private static final Logger logger = BoltLoggerFactory
            .getLogger("RedisBoltClient");


    static {
        ProtocolManager.registerProtocol(new RedisClientProtocol(), RedisClientProtocol.PROTOCOL_CODE);
    }

    public RedisBoltClient() {
        super();

        ConcurrentHashMap<String, UserProcessor<?>> userProcessors = getUserProcessors();
        RpcConnectionEventHandler connectionEventHandler = getConnectionEventHandler();
        ConnectionEventListener connectionEventListener = getConnectionEventListener();
        ConnectionSelectStrategy connectionSelectStrategy = this.option(BoltGenericOption.CONNECTION_SELECT_STRATEGY);

        if (connectionSelectStrategy == null) {
            connectionSelectStrategy = new RandomSelectStrategy(this);
        }

        DefaultClientConnectionManager defaultConnectionManager = new DefaultClientConnectionManager(
                connectionSelectStrategy, new RedisClientConnectionFactory(userProcessors, this),
                connectionEventHandler, connectionEventListener);
        defaultConnectionManager.setAddressParser(this.getAddressParser());
        defaultConnectionManager.startup();



        this.setConnectionManager(defaultConnectionManager);
    }

    @Override
    public void startup() throws LifeCycleException {
        super.startup();

        this.rpcRemoting = new RedisBoltRemoting(new RedisBoltCommandFactory(), this.getAddressParser(),
                this.getConnectionManager());
    }

    private ConcurrentHashMap<String, UserProcessor<?>> getUserProcessors(){
        try {
            Field userProcessors = getClass().getSuperclass().getDeclaredField("userProcessors");
            userProcessors.setAccessible(true);
           return (ConcurrentHashMap<String, UserProcessor<?>>) userProcessors.get(this);
        } catch (NoSuchFieldException | IllegalAccessException  e) {
            logger.error(e.getMessage(),e);
        }
        return null;
    }

    private RpcConnectionEventHandler getConnectionEventHandler(){
        try {
            Field connectionEventHandle = getClass().getSuperclass().getDeclaredField("connectionEventHandler");
            connectionEventHandle.setAccessible(true);
           return (RpcConnectionEventHandler) connectionEventHandle.get(this);
        } catch (NoSuchFieldException | IllegalAccessException  e) {
            logger.error(e.getMessage(),e);
        }
        return null;
    }

    private ConnectionEventListener getConnectionEventListener(){
        try {
            Field connectionEventListener = getClass().getSuperclass().getDeclaredField("connectionEventListener");
            connectionEventListener.setAccessible(true);
           return (ConnectionEventListener) connectionEventListener.get(this);
        } catch (NoSuchFieldException | IllegalAccessException  e) {
            logger.error(e.getMessage(),e);
        }
        return null;
    }
}
