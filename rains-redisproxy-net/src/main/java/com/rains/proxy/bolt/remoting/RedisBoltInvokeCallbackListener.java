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
package com.rains.proxy.bolt.remoting;

import com.alipay.remoting.*;
import com.alipay.remoting.exception.ConnectionClosedException;
import com.alipay.remoting.log.BoltLoggerFactory;
import com.alipay.remoting.rpc.ResponseCommand;
import com.alipay.remoting.rpc.exception.InvokeException;
import com.alipay.remoting.rpc.exception.InvokeServerBusyException;
import com.alipay.remoting.rpc.exception.InvokeServerException;
import com.alipay.remoting.rpc.exception.InvokeTimeoutException;
import org.slf4j.Logger;

import java.util.concurrent.RejectedExecutionException;

/**
 * @author hugoDD
 */
public class RedisBoltInvokeCallbackListener implements InvokeCallbackListener {

    private static final Logger logger = BoltLoggerFactory.getLogger("RedisBoltRemoting");

    private String              address;

    public RedisBoltInvokeCallbackListener() {

    }

    public RedisBoltInvokeCallbackListener(String address) {
        this.address = address;
    }

    /**
     * @see InvokeCallbackListener#onResponse(InvokeFuture)
     */
    @Override
    public void onResponse(InvokeFuture future) {
        InvokeCallback callback = future.getInvokeCallback();
        if (callback != null) {
            CallbackTask task = new CallbackTask(this.getRemoteAddress(), future);
            if (callback.getExecutor() != null) {
                // There is no need to switch classloader, because executor is provided by user.
                try {
                    callback.getExecutor().execute(task);
                } catch (RejectedExecutionException e) {
                    if (callback instanceof RejectionProcessableInvokeCallback) {
                        switch (((RejectionProcessableInvokeCallback) callback)
                            .rejectedExecutionPolicy()) {
                            case CALLER_RUNS:
                                task.run();
                                break;
                            case CALLER_HANDLE_EXCEPTION:
                                callback.onException(e);
                                break;
                            case DISCARD:
                            default:
                                logger.warn("Callback thread pool busy. discard the callback");
                                break;
                        }
                    } else {
                        logger.warn("Callback thread pool busy.");
                    }
                }
            } else {
                task.run();
            }
        }
    }

    class CallbackTask implements Runnable {

        InvokeFuture future;
        String       remoteAddress;

        /**
         *
         */
        public CallbackTask(String remoteAddress, InvokeFuture future) {
            this.remoteAddress = remoteAddress;
            this.future = future;
        }

        /**
         * @see Runnable#run()
         */
        @Override
        public void run() {
            InvokeCallback callback = future.getInvokeCallback();
            // a lot of try-catches to protect thread pool
            ResponseCommand response = null;

            try {
                response = (ResponseCommand) future.waitResponse(0);
            } catch (InterruptedException e) {
                String msg = "Exception caught when getting response from InvokeFuture. The address is "
                             + this.remoteAddress;
                logger.error(msg, e);
            }
            if (response == null || response.getResponseStatus() != ResponseStatus.SUCCESS) {
                try {
                    Exception e;
                    if (response == null) {
                        e = new InvokeException("Exception caught in invocation. The address is "
                                                + this.remoteAddress + " responseStatus:"
                                                + ResponseStatus.UNKNOWN, future.getCause());
                    } else {
                        response.setInvokeContext(future.getInvokeContext());
                        switch (response.getResponseStatus()) {
                            case TIMEOUT:
                                e = new InvokeTimeoutException(
                                    "Invoke timeout when invoke with callback.The address is "
                                            + this.remoteAddress);
                                break;
                            case CONNECTION_CLOSED:
                                e = new ConnectionClosedException(
                                    "Connection closed when invoke with callback.The address is "
                                            + this.remoteAddress);
                                break;
                            case SERVER_THREADPOOL_BUSY:
                                e = new InvokeServerBusyException(
                                    "Server thread pool busy when invoke with callback.The address is "
                                            + this.remoteAddress);
                                break;
                            case SERVER_EXCEPTION:
                                String msg = "Server exception when invoke with callback.Please check the server log! The address is "
                                             + this.remoteAddress;
                                RedisResponseCommand resp = (RedisResponseCommand) response;
                                Object ex = resp.getResponseObject();
                                if (ex instanceof Throwable) {
                                    e = new InvokeServerException(msg, (Throwable) ex);
                                } else {
                                    e = new InvokeServerException(msg);
                                }
                                break;
                            default:
                                e = new InvokeException(
                                    "Exception caught in invocation. The address is "
                                            + this.remoteAddress + " responseStatus:"
                                            + response.getResponseStatus(), future.getCause());

                        }
                    }
                    callback.onException(e);
                } catch (Throwable e) {
                    logger
                        .error(
                            "Exception occurred in user defined InvokeCallback#onException() logic, The address is {}",
                            this.remoteAddress, e);
                }
            } else {
                ClassLoader oldClassLoader = null;
                try {
                    if (future.getAppClassLoader() != null) {
                        oldClassLoader = Thread.currentThread().getContextClassLoader();
                        Thread.currentThread().setContextClassLoader(future.getAppClassLoader());
                    }
                    response.setInvokeContext(future.getInvokeContext());
                    RedisResponseCommand rpcResponse = (RedisResponseCommand) response;
                    try {
                        callback.onResponse(rpcResponse.getResponseObject());
                    } catch (Throwable e) {
                        logger
                            .error(
                                "Exception occurred in user defined InvokeCallback#onResponse() logic.",
                                e);
                    }
                }  catch (Throwable e) {
                    logger.error(
                        "Exception caught in RpcInvokeCallbackListener. The address is {}",
                        this.remoteAddress, e);
                } finally {
                    if (oldClassLoader != null) {
                        Thread.currentThread().setContextClassLoader(oldClassLoader);
                    }
                }
            } // enf of else
        } // end of run
    }

    /**
     * @see InvokeCallbackListener#getRemoteAddress()
     */
    @Override
    public String getRemoteAddress() {
        return this.address;
    }
}
