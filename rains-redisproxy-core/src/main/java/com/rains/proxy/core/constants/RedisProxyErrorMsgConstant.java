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
package com.rains.proxy.core.constants;


import com.rains.proxy.core.exception.RedisProxyErrorMsg;


/**
 * @author dourx
 * @version V1.0
 * 创建日期 2018/6/5
 * 业务错误变量
 */
public class RedisProxyErrorMsgConstant {
	
	 // service error status 503
    public static final int SERVICE_DEFAULT_ERROR_CODE = 10001;
    public static final int SERVICE_REJECT_ERROR_CODE = 10002;
    public static final int SERVICE_TIMEOUT_ERROR_CODE = 10003;
    public static final int SERVICE_TASK_CANCEL_ERROR_CODE = 10004;
    // service error status 404
    public static final int SERVICE_UNFOUND_ERROR_CODE = 10101;
    // service error status 403
    public static final int SERVICE_REQUEST_LENGTH_OUT_OF_LIMIT_ERROR_CODE = 10201;
    // framework error
    public static final int FRAMEWORK_DEFAULT_ERROR_CODE = 20001;
    public static final int FRAMEWORK_ENCODE_ERROR_CODE = 20002;
    public static final int FRAMEWORK_DECODE_ERROR_CODE = 20003;
    public static final int FRAMEWORK_INIT_ERROR_CODE = 20004;
    public static final int FRAMEWORK_EXPORT_ERROR_CODE = 20005;
    public static final int FRAMEWORK_SERVER_ERROR_CODE = 20006;
    public static final int FRAMEWORK_REFER_ERROR_CODE = 20007;
    public static final int FRAMEWORK_REGISTER_ERROR_CODE = 20008;
    // biz exception
    public static final int BIZ_DEFAULT_ERROR_CODE = 30001;
    /**
     * service error start
     **/

    public static final RedisProxyErrorMsg SERVICE_DEFAULT_ERROR = new RedisProxyErrorMsg(503, SERVICE_DEFAULT_ERROR_CODE, "service error");
    public static final RedisProxyErrorMsg SERVICE_REJECT = new RedisProxyErrorMsg(503, SERVICE_REJECT_ERROR_CODE, "service reject");
    public static final RedisProxyErrorMsg SERVICE_UNFOUND = new RedisProxyErrorMsg(404, SERVICE_UNFOUND_ERROR_CODE, "service unfound");
    public static final RedisProxyErrorMsg SERVICE_TIMEOUT = new RedisProxyErrorMsg(503, SERVICE_TIMEOUT_ERROR_CODE, "service request timeout");
    public static final RedisProxyErrorMsg SERVICE_TASK_CANCEL = new RedisProxyErrorMsg(503, SERVICE_TASK_CANCEL_ERROR_CODE, "service task cancel");
    public static final RedisProxyErrorMsg SERVICE_REQUEST_LENGTH_OUT_OF_LIMIT = new RedisProxyErrorMsg(403,
            SERVICE_REQUEST_LENGTH_OUT_OF_LIMIT_ERROR_CODE, "servier requset data length over of limit");

    /**
     * framework error start
     **/
    public static final RedisProxyErrorMsg FRAMEWORK_DEFAULT_ERROR = new RedisProxyErrorMsg(503, FRAMEWORK_DEFAULT_ERROR_CODE,
            "framework default error");

    /** service error end **/
    public static final RedisProxyErrorMsg FRAMEWORK_ENCODE_ERROR =
            new RedisProxyErrorMsg(503, FRAMEWORK_ENCODE_ERROR_CODE, "framework encode error");
    public static final RedisProxyErrorMsg FRAMEWORK_DECODE_ERROR =
            new RedisProxyErrorMsg(503, FRAMEWORK_DECODE_ERROR_CODE, "framework decode error");
    public static final RedisProxyErrorMsg FRAMEWORK_INIT_ERROR = new RedisProxyErrorMsg(500, FRAMEWORK_INIT_ERROR_CODE, "framework init error");
    public static final RedisProxyErrorMsg FRAMEWORK_EXPORT_ERROR =
            new RedisProxyErrorMsg(503, FRAMEWORK_EXPORT_ERROR_CODE, "framework export error");
    public static final RedisProxyErrorMsg FRAMEWORK_REFER_ERROR = new RedisProxyErrorMsg(503, FRAMEWORK_REFER_ERROR_CODE, "framework refer error");
    /**
     * biz error start
     **/
    public static final RedisProxyErrorMsg BIZ_DEFAULT_EXCEPTION = new RedisProxyErrorMsg(503, BIZ_DEFAULT_ERROR_CODE, "provider error");
    /** framework error end **/

    private RedisProxyErrorMsgConstant() {
    }
    /** biz error end **/
}
