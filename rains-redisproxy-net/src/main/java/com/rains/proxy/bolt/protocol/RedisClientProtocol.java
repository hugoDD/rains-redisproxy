package com.rains.proxy.bolt.protocol;

import com.alipay.remoting.*;
import com.alipay.remoting.rpc.RpcCommandFactory;
import com.alipay.remoting.rpc.protocol.RpcCommandHandler;
import com.alipay.remoting.rpc.protocol.RpcHeartbeatTrigger;
import com.rains.proxy.bolt.protocol.codec.RedisBoltReplyDecoder;
import com.rains.proxy.bolt.protocol.codec.RedisBoltRequestEncoder;
import com.rains.proxy.bolt.remoting.RedisBoltCommandFactory;

public class RedisClientProtocol implements Protocol {
    public static final byte PROTOCOL_CODE = 0;

    private CommandEncoder   encoder;
    private CommandDecoder   decoder;
    private HeartbeatTrigger heartbeatTrigger;
    private CommandHandler   commandHandler;
    private CommandFactory   commandFactory;

    public RedisClientProtocol() {
        this.encoder = new RedisBoltRequestEncoder();
        this.decoder = new RedisBoltReplyDecoder();
        this.commandFactory = new RedisBoltCommandFactory();
        this.heartbeatTrigger = new RpcHeartbeatTrigger(this.commandFactory);
        this.commandHandler = new RpcCommandHandler(this.commandFactory);
    }

    @Override
    public CommandEncoder getEncoder() {
        return encoder;
    }

    @Override
    public CommandDecoder getDecoder() {
        return decoder;
    }

    @Override
    public HeartbeatTrigger getHeartbeatTrigger() {
        return heartbeatTrigger;
    }

    @Override
    public CommandHandler getCommandHandler() {
        return commandHandler;
    }

    @Override
    public CommandFactory getCommandFactory() {
        return commandFactory;
    }
}
