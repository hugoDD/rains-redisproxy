package com.rains.proxy.bolt.remoting;

import com.alipay.remoting.rpc.ResponseCommand;
import com.alipay.remoting.rpc.RpcCommand;
import com.alipay.remoting.rpc.protocol.RpcCommandCode;

public class RedisResponseCommand extends ResponseCommand {
    private Object            responseObject;
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
}
