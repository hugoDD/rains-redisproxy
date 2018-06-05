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
package com.rains.proxy.core.exception;


import com.rains.proxy.core.constants.RedisProxyErrorMsgConstant;


/**
 * @author dourx
 * @version V1.0
 * 创建日期 2018/6/5 
 * 抽象业务异常
 *
 */
public abstract class AbstractRedisProxyException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5237904941832693524L;
	
	protected RedisProxyErrorMsg rpcErrorMsg =RedisProxyErrorMsgConstant.FRAMEWORK_DEFAULT_ERROR;
	
	protected String errorMsg = null;
	
	public AbstractRedisProxyException() {
        super();
    }

    public AbstractRedisProxyException(RedisProxyErrorMsg rpcErrorMsg) {
        super();
        this.rpcErrorMsg = rpcErrorMsg;
    }

    public AbstractRedisProxyException(String message) {
        super(message);
        this.errorMsg = message;
    }

    public AbstractRedisProxyException(String message, RedisProxyErrorMsg rpcErrorMsg) {
        super(message);
        this.rpcErrorMsg = rpcErrorMsg;
        this.errorMsg = message;
    }

    public AbstractRedisProxyException(String message, Throwable cause) {
        super(message, cause);
        this.errorMsg = message;
    }

    public AbstractRedisProxyException(String message, Throwable cause, RedisProxyErrorMsg rpcErrorMsg) {
        super(message, cause);
        this.rpcErrorMsg = rpcErrorMsg;
        this.errorMsg = message;
    }

    public AbstractRedisProxyException(Throwable cause) {
        super(cause);
    }

    public AbstractRedisProxyException(Throwable cause, RedisProxyErrorMsg rpcErrorMsg) {
        super(cause);
        this.rpcErrorMsg = rpcErrorMsg;
    }

    @Override
    public String getMessage() {
        if (rpcErrorMsg == null) {
            return super.getMessage();
        }

        String message;

        if (errorMsg != null && !"".equals(errorMsg)) {
            message = errorMsg;
        } else {
            message = rpcErrorMsg.getMessage();
        }

        // TODO 统一上下文 requestid
        return "error_message: " + message + ", status: " + rpcErrorMsg.getStatus() + ", error_code: " + rpcErrorMsg.getErrorcode()
                + ",r=";
    }

    public int getStatus() {
        return rpcErrorMsg != null ? rpcErrorMsg.getStatus() : 0;
    }

    public int getErrorCode() {
        return rpcErrorMsg != null ? rpcErrorMsg.getErrorcode() : 0;
    }

    public RedisProxyErrorMsg getMotanErrorMsg() {
        return rpcErrorMsg;
    }
}
