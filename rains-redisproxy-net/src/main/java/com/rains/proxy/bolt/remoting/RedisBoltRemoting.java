package com.rains.proxy.bolt.remoting;

import com.alipay.remoting.*;
import com.alipay.remoting.exception.SerializationException;
import com.alipay.remoting.log.BoltLoggerFactory;
import com.alipay.remoting.rpc.RpcClientRemoting;
import org.slf4j.Logger;

public class RedisBoltRemoting<T> extends RpcClientRemoting {
    private static final Logger logger = BoltLoggerFactory.getLogger("RedisBoltRemoting");
    public RedisBoltRemoting(CommandFactory commandFactory, RemotingAddressParser addressParser, ConnectionManager connectionManager) {
        super(commandFactory, addressParser, connectionManager);
    }

    @Override
    protected RemotingCommand toRemotingCommand(Object request, Connection conn, InvokeContext invokeContext, int timeoutMillis) throws SerializationException {
       return this.getCommandFactory().createRequestCommand(request);


    }
}
