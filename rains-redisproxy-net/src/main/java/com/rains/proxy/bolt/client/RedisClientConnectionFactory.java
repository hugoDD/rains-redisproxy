package com.rains.proxy.bolt.client;

import com.alipay.remoting.codec.Codec;
import com.alipay.remoting.config.Configuration;
import com.alipay.remoting.connection.DefaultConnectionFactory;
import com.alipay.remoting.rpc.HeartbeatHandler;
import com.alipay.remoting.rpc.RpcCodec;
import com.alipay.remoting.rpc.RpcHandler;
import com.alipay.remoting.rpc.protocol.UserProcessor;
import com.rains.proxy.bolt.remoting.RedisBoltCodec;
import io.netty.channel.ChannelHandler;

import java.util.concurrent.ConcurrentHashMap;

/**
 * @author ly-dourx
 */
public class RedisClientConnectionFactory extends DefaultConnectionFactory {
    public RedisClientConnectionFactory(ConcurrentHashMap<String, UserProcessor<?>> userProcessors,
                                Configuration configurations) {
        super(new RedisBoltCodec(), new HeartbeatHandler(), new RpcHandler(userProcessors),
                configurations);
    }
}
