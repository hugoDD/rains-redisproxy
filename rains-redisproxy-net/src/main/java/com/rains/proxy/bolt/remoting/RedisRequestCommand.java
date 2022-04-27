package com.rains.proxy.bolt.remoting;

import com.alipay.remoting.InvokeContext;
import com.alipay.remoting.RemotingCommand;
import com.alipay.remoting.exception.DeserializationException;
import com.alipay.remoting.exception.SerializationException;
import com.alipay.remoting.rpc.RequestCommand;
import com.alipay.remoting.rpc.protocol.RpcCommandCode;
import com.alipay.remoting.serialization.SerializerManager;
import com.alipay.remoting.util.IDGenerator;
import com.rains.proxy.core.command.IRedisCommand;

public class RedisRequestCommand extends RequestCommand {
    private IRedisCommand requestObject;

    /**
     * create request command without id
     */
    public RedisRequestCommand() {
        super(RpcCommandCode.RPC_REQUEST);
    }

    /**
     * create request command with id and request object
     * @param request request object
     */
    public RedisRequestCommand(IRedisCommand request) {
        super(RpcCommandCode.RPC_REQUEST);
        this.requestObject = request;
        this.setId(IDGenerator.nextId());
    }

    public IRedisCommand getRequestObject() {
        return requestObject;
    }

    public void setRequestObject(IRedisCommand requestObject) {
        this.requestObject = requestObject;
    }
}
