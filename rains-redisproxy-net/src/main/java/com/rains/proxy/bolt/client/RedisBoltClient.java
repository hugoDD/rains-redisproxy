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
import com.alipay.remoting.rpc.RpcTaskScanner;
import com.alipay.remoting.rpc.protocol.UserProcessor;
import org.slf4j.Logger;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Client for Rpc.
 *
 * @author jiangping
 * @version $Id: RpcClient.java, v 0.1 2015-9-23 PM4:03:28 tao Exp $
 */
public class RedisBoltClient extends RpcClient {

    private static final Logger logger = BoltLoggerFactory
            .getLogger("RedisRemoting");

    private final RpcTaskScanner taskScanner = new RpcTaskScanner();
    private final ConcurrentHashMap<String, UserProcessor<?>> userProcessors = new ConcurrentHashMap();
    private final ConnectionEventHandler connectionEventHandler = new RpcConnectionEventHandler(this);
    private final ConnectionEventListener connectionEventListener = new ConnectionEventListener();
    private ConnectionManager connectionManager;
    private Reconnector reconnectManager;
    private RemotingAddressParser addressParser;
    private DefaultConnectionMonitor connectionMonitor;
    private ConnectionMonitorStrategy monitorStrategy;

    public RedisBoltClient() {
        ConnectionSelectStrategy connectionSelectStrategy = (ConnectionSelectStrategy)this.option(BoltGenericOption.CONNECTION_SELECT_STRATEGY);
        if (connectionSelectStrategy == null) {
            connectionSelectStrategy = new RandomSelectStrategy(this);
        }

        DefaultClientConnectionManager defaultConnectionManager = new DefaultClientConnectionManager(
                connectionSelectStrategy, new RedisClientConnectionFactory(userProcessors, this),
                connectionEventHandler, connectionEventListener);
        defaultConnectionManager.setAddressParser(this.addressParser);
        defaultConnectionManager.startup();
        this.connectionManager = defaultConnectionManager;
    }
}
