package com.rains.proxy.bolt.remoting;

import com.alipay.remoting.*;
import com.alipay.remoting.exception.RemotingException;
import com.alipay.remoting.exception.SerializationException;
import com.alipay.remoting.log.BoltLoggerFactory;
import com.alipay.remoting.rpc.ResponseCommand;
import com.alipay.remoting.rpc.RpcClientRemoting;
import com.alipay.remoting.rpc.RpcResponseResolver;
import com.alipay.remoting.util.RemotingUtil;
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



    /**
     * Synchronous rpc invocation.<br>
     * Notice! DO NOT modify the request object concurrently when this method is called.
     *
     * @param conn
     * @param request
     * @param invokeContext
     * @param timeoutMillis
     * @return
     * @throws RemotingException
     * @throws InterruptedException
     */
    @Override
    public Object invokeSync(final Connection conn, final Object request,
                             final InvokeContext invokeContext, final int timeoutMillis)
            throws RemotingException,
            InterruptedException {
        RemotingCommand requestCommand = toRemotingCommand(request, conn, invokeContext,
                timeoutMillis);
        preProcessInvokeContext(invokeContext, requestCommand, conn);
        ResponseCommand responseCommand = (ResponseCommand) super.invokeSync(conn, requestCommand,
                timeoutMillis);
        responseCommand.setInvokeContext(invokeContext);

        return RedisBoltResponseResolver.resolveResponseObject(responseCommand,
                RemotingUtil.parseRemoteAddress(conn.getChannel()));
    }
}
