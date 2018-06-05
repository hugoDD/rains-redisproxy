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

import java.io.Serializable;

/**
 * @author dourx
 * @version V1.0
 * 创建日期 2018/6/5
 */
public class RedisProxyErrorMsg implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4928071017793101657L;
	
	private int status;
    private int errorcode;
    private String message;
	public RedisProxyErrorMsg(int status, int errorcode, String message) {
		super();
		this.status = status;
		this.errorcode = errorcode;
		this.message = message;
	}
	/**
	 * @return the status
	 */
	public int getStatus() {
		return status;
	}
	/**
	 * @param status the status to set
	 */
	public void setStatus(int status) {
		this.status = status;
	}
	/**
	 * @return the errorcode
	 */
	public int getErrorcode() {
		return errorcode;
	}
	/**
	 * @param errorcode the errorcode to set
	 */
	public void setErrorcode(int errorcode) {
		this.errorcode = errorcode;
	}
	/**
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}
	/**
	 * @param message the message to set
	 */
	public void setMessage(String message) {
		this.message = message;
	}
    
    

}
