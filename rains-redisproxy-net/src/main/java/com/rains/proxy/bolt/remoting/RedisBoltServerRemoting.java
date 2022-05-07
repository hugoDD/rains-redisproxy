package com.rains.proxy.bolt.remoting;

import com.alipay.remoting.*;
import com.alipay.remoting.exception.SerializationException;
import com.alipay.remoting.rpc.RpcServerRemoting;

/**
 * @author ly-dourx
 */
public class RedisBoltServerRemoting extends RpcServerRemoting {

    /** address parser to get custom args */
    protected RemotingAddressParser addressParser;

    /** connection manager */
    protected ConnectionManager     connectionManager;


    public RedisBoltServerRemoting(CommandFactory commandFactory) {
        super(commandFactory);
    }

    public RedisBoltServerRemoting(CommandFactory commandFactory, RemotingAddressParser addressParser, DefaultConnectionManager connectionManager) {
        super(commandFactory, addressParser, connectionManager);
    }

    @Override
    protected RemotingCommand toRemotingCommand(Object request, Connection conn, InvokeContext invokeContext, int timeoutMillis) throws SerializationException {
        return this.getCommandFactory().createRequestCommand(request);

    }

    public RemotingAddressParser getAddressParser() {
        return addressParser;
    }

    public ConnectionManager getConnectionManager() {
        return connectionManager;
    }
}
