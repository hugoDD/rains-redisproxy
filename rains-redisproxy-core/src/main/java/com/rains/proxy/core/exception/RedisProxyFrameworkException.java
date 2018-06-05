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
 * 包装客户端异常
 */
public class RedisProxyFrameworkException extends AbstractRedisProxyException {
	
    private static final long serialVersionUID = -1638857395789735293L;

    public RedisProxyFrameworkException() {
        super(RedisProxyErrorMsgConstant.FRAMEWORK_DEFAULT_ERROR);
    }

    public RedisProxyFrameworkException(RedisProxyErrorMsg ffanRpcErrorMsg) {
        super(ffanRpcErrorMsg);
    }

    public RedisProxyFrameworkException(String message) {
        super(message, RedisProxyErrorMsgConstant.FRAMEWORK_DEFAULT_ERROR);
    }

    public RedisProxyFrameworkException(String message, RedisProxyErrorMsg ffanRpcErrorMsg) {
        super(message, ffanRpcErrorMsg);
    }

    public RedisProxyFrameworkException(String message, Throwable cause) {
        super(message, cause, RedisProxyErrorMsgConstant.FRAMEWORK_DEFAULT_ERROR);
    }

    public RedisProxyFrameworkException(String message, Throwable cause, RedisProxyErrorMsg ffanRpcErrorMsg) {
        super(message, cause, ffanRpcErrorMsg);
    }

    public RedisProxyFrameworkException(Throwable cause) {
        super(cause, RedisProxyErrorMsgConstant.FRAMEWORK_DEFAULT_ERROR);
    }

    public RedisProxyFrameworkException(Throwable cause, RedisProxyErrorMsg ffanRpcErrorMsg) {
        super(cause, ffanRpcErrorMsg);
    }

}
