package com.rains.proxy.bolt.remoting;

import com.alipay.remoting.CommandFactory;
import com.alipay.remoting.ConnectionManager;
import com.alipay.remoting.DefaultConnectionManager;
import com.alipay.remoting.RemotingAddressParser;
import com.alipay.remoting.rpc.RpcServerRemoting;

public class RedisBoltServerRemoting extends RpcServerRemoting {
    public RedisBoltServerRemoting(CommandFactory commandFactory) {
        super(commandFactory);
    }

    public RedisBoltServerRemoting(CommandFactory commandFactory, RemotingAddressParser addressParser, DefaultConnectionManager connectionManager) {
        super(commandFactory, addressParser, connectionManager);
    }
    /** address parser to get custom args */
    public RemotingAddressParser getAddressParser(){
        return this.addressParser;
    }

    /** connection manager */
    public ConnectionManager getConnectionManager(){
        return this.connectionManager;
    }



}
