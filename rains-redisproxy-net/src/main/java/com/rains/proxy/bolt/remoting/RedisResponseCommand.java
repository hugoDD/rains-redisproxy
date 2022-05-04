package com.rains.proxy.bolt.remoting;

import com.alipay.remoting.rpc.ResponseCommand;
import com.alipay.remoting.rpc.RpcCommand;
import com.alipay.remoting.rpc.protocol.RpcCommandCode;

public class RedisResponseCommand extends ResponseCommand {
    private Object            responseObject;
    private String            errorMsg;
    public RedisResponseCommand() {
        super(RpcCommandCode.RPC_RESPONSE);
    }

    public RedisResponseCommand(Object response) {
        super(RpcCommandCode.RPC_RESPONSE);
        this.responseObject = response;
    }

    public RedisResponseCommand(int id, Object response) {
        super(RpcCommandCode.RPC_RESPONSE, id);
        this.responseObject = response;
    }

    public Object getResponseObject() {
        return responseObject;
    }

    public void setResponseObject(Object responseObject) {
        this.responseObject = responseObject;
    }

    /**
     * Getter method for property <tt>errorMsg</tt>.
     *
     * @return property value of errorMsg
     */
    public String getErrorMsg() {
        return errorMsg;
    }

    /**
     * Setter method for property <tt>errorMsg</tt>.
     *
     * @param errorMsg value to be assigned to property errorMsg
     */
    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

}
