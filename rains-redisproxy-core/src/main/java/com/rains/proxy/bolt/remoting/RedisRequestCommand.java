package com.rains.proxy.bolt.remoting;

import com.alipay.remoting.rpc.RequestCommand;
import com.alipay.remoting.rpc.protocol.RpcCommandCode;
import com.alipay.remoting.util.IDGenerator;
import com.rains.proxy.core.command.IRedisCommand;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class RedisRequestCommand extends RequestCommand {
    private IRedisCommand requestObject;
    private static Queue<Integer> requestIdsQueue = new ConcurrentLinkedQueue<>();

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
        requestIdsQueue.add(this.getId());
    }

    public IRedisCommand getRequestObject() {
        return requestObject;
    }

    public void setRequestObject(IRedisCommand requestObject) {
        this.requestObject = requestObject;
    }

    public static Integer pollRequestId(){
       return requestIdsQueue.poll();
    }
}
