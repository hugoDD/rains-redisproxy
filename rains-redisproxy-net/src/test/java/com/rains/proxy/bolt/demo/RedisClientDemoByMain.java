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
package com.rains.proxy.bolt.demo;

import com.alipay.remoting.ConnectionEventType;
import com.alipay.remoting.InvokeCallback;
import com.alipay.remoting.InvokeContext;
import com.alipay.remoting.config.Configs;
import com.alipay.remoting.exception.RemotingException;
import com.alipay.remoting.rpc.RpcClient;
import com.rains.proxy.bolt.client.RedisBoltClient;
import com.rains.proxy.bolt.processor.CONNECTEventProcessor;
import com.rains.proxy.bolt.processor.DISCONNECTEventProcessor;
import com.rains.proxy.bolt.processor.SimpleClientUserProcessor;
import com.rains.proxy.core.command.impl.RedisCommand;
import com.rains.proxy.core.reply.IRedisReply;
import com.rains.proxy.core.utils.RedisCmdUtils;
import io.netty.buffer.ByteBuf;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.stream.Stream;

/**
 *
 * @author tsui
 * @version $Id: RpcClientDemoByMain.java, v 0.1 2018-04-10 10:39 tsui Exp $
 */
public class RedisClientDemoByMain {
    static Logger             logger                    = LoggerFactory
                                                            .getLogger(RedisClientDemoByMain.class);

    static RpcClient          client;

    static String             addr                      = "172.26.223.109:6379?_PROTOCOL=0";
   // static String             addr                      = "localhost:6379?_PROTOCOL=0";

    SimpleClientUserProcessor clientUserProcessor       = new SimpleClientUserProcessor();
    CONNECTEventProcessor clientConnectProcessor    = new CONNECTEventProcessor();
    DISCONNECTEventProcessor clientDisConnectProcessor = new DISCONNECTEventProcessor();

    public RedisClientDemoByMain() {
        System.setProperty(Configs.TCP_IDLE_SWITCH, "false");
        // 1. create a rpc client
        client = new RedisBoltClient();
        // 2. add processor for connect and close event if you need
        client.addConnectionEventProcessor(ConnectionEventType.CONNECT, clientConnectProcessor);
        client.addConnectionEventProcessor(ConnectionEventType.CLOSE, clientDisConnectProcessor);
        // 3. do init
        client.init();
    }

    public static void main(String[] args) {

        RedisClientDemoByMain demo =new RedisClientDemoByMain();


        RedisCommand setCmd = RedisCmdUtils.createCmd("set mykey myvalue");
        RedisCommand getCmd = RedisCmdUtils.createCmd("get mykey");
        try {
            RedisCommand ping = RedisCmdUtils.createCmd("ping");
            RedisCommand auth = RedisCmdUtils.createCmd("auth Youcloud@2022");
//            RedisCommand auth = RedisCmdUtils.createCmd("auth 123456");

            IRedisReply authRes = (IRedisReply) client.invokeSync(addr, auth, 30000000);
            System.out.println("invoke auth sync result = [" + authRes + "]");

            IRedisReply pingRes = (IRedisReply) client.invokeSync(addr, ping, 3000);
            System.out.println("invoke ping sync result = [" + pingRes + "]");




            IRedisReply setCmdRes = (IRedisReply) client.invokeSync(addr, setCmd, 3000);
            System.out.println("invoke sync result = [" + setCmdRes + "]");

            IRedisReply getCmdRes = (IRedisReply) client.invokeSync(addr, getCmd, 3000);
            System.out.println("invoke sync result = [" + getCmdRes + "]");

           // for (int i = 0; i < 10; i++) {
                demo.testCallback();
//            }
        System.out.println();
        } catch (RemotingException e) {
            String errMsg = "RemotingException caught in oneway!";
            logger.error(errMsg, e);
            Assert.fail(errMsg);
        } catch (InterruptedException e) {
            logger.error("interrupted!");
        }
        client.shutdown();
    }

    public  void testCallback() throws RemotingException, InterruptedException {
        RedisCommand getCmd = RedisCmdUtils.createCmd("get mykey");
        final CountDownLatch latch = new CountDownLatch(1);

        client.invokeWithCallback(addr, getCmd, new InvokeCallback() {
            Executor executor = Executors.newCachedThreadPool();
            @Override
            public void onResponse(Object result) {
                IRedisReply getCmdRes = (IRedisReply) result;
                System.out.println("invoke invokeWithCallback result = [" + getCmdRes + "]");
                latch.countDown();
            }

            @Override
            public void onException(Throwable e) {
                logger.error("Process exception in callback.", e);
                latch.countDown();
            }

            @Override
            public Executor getExecutor() {
                return executor;
            }
        }, 1000000);

        latch.await();
    }

    protected String readToStr(ByteBuf buf, int len){
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < len; i++) {
            sb.append((char)buf.readByte());
        }
        return sb.toString();
    }
}
