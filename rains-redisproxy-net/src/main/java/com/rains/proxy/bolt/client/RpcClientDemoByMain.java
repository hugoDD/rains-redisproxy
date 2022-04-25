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

import com.alipay.remoting.ConnectionEventType;
import com.alipay.remoting.exception.RemotingException;
import com.alipay.remoting.rpc.RpcClient;
import com.rains.proxy.bolt.processor.CONNECTEventProcessor;
import com.rains.proxy.bolt.processor.DISCONNECTEventProcessor;
import com.rains.proxy.bolt.processor.SimpleClientUserProcessor;
import com.rains.proxy.core.command.impl.RedisCommand;
import org.junit.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author tsui
 * @version $Id: RpcClientDemoByMain.java, v 0.1 2018-04-10 10:39 tsui Exp $
 */
public class RpcClientDemoByMain {
    static Logger             logger                    = LoggerFactory
                                                            .getLogger(RpcClientDemoByMain.class);

    static RpcClient          client;

    static String             addr                      = "127.0.0.1:8999";

    SimpleClientUserProcessor clientUserProcessor       = new SimpleClientUserProcessor();
    CONNECTEventProcessor clientConnectProcessor    = new CONNECTEventProcessor();
    DISCONNECTEventProcessor clientDisConnectProcessor = new DISCONNECTEventProcessor();

    public RpcClientDemoByMain() {
        // 1. create a rpc client
        client = new RedisBoltClient();
        // 2. add processor for connect and close event if you need
        client.addConnectionEventProcessor(ConnectionEventType.CONNECT, clientConnectProcessor);
        client.addConnectionEventProcessor(ConnectionEventType.CLOSE, clientDisConnectProcessor);
        // 3. do init
        client.init();
    }

    public static void main(String[] args) {
        new RpcClientDemoByMain();
        RedisCommand redisCommand = new RedisCommand();
        redisCommand.setArgCount(3);
        List<byte[]> cmdArgs = new ArrayList<>();
        cmdArgs.add("set".getBytes());
        cmdArgs.add("mykey".getBytes());
        cmdArgs.add("myvalue".getBytes());
        redisCommand.setArgs(cmdArgs);
        try {
            String res = (String) client.invokeSync(addr, redisCommand, 3000);
            System.out.println("invoke sync result = [" + res + "]");
        } catch (RemotingException e) {
            String errMsg = "RemotingException caught in oneway!";
            logger.error(errMsg, e);
            Assert.fail(errMsg);
        } catch (InterruptedException e) {
            logger.error("interrupted!");
        }
        client.shutdown();
    }
}
