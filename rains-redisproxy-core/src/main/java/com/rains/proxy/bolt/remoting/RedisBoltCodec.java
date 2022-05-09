package com.rains.proxy.bolt.remoting;

import com.alipay.remoting.ProtocolCode;
import com.alipay.remoting.codec.Codec;
import com.alipay.remoting.codec.ProtocolCodeBasedEncoder;
import com.rains.proxy.bolt.protocol.ProtocolBasedDecoder;
import com.rains.proxy.bolt.protocol.RedisClientProtocol;
import io.netty.channel.ChannelHandler;

public class RedisBoltCodec implements Codec {
    @Override
    public ChannelHandler newEncoder() {
        return new ProtocolCodeBasedEncoder(ProtocolCode.fromBytes(RedisClientProtocol.PROTOCOL_CODE));
    }

    @Override
    public ChannelHandler newDecoder() {
        return new ProtocolBasedDecoder(ProtocolCode.fromBytes(RedisClientProtocol.PROTOCOL_CODE));
    }
}
