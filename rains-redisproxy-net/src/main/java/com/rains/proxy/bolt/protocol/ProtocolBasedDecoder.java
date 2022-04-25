package com.rains.proxy.bolt.protocol;

import com.alipay.remoting.Connection;
import com.alipay.remoting.Protocol;
import com.alipay.remoting.ProtocolCode;
import com.alipay.remoting.ProtocolManager;
import com.alipay.remoting.codec.AbstractBatchDecoder;
import com.alipay.remoting.exception.CodecException;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.Attribute;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class ProtocolBasedDecoder extends AbstractBatchDecoder {
    private static final Logger logger = LoggerFactory.getLogger(ProtocolBasedDecoder.class);
    protected ProtocolCode defaultProtocolCode;

    public ProtocolBasedDecoder(ProtocolCode defaultProtocolCode) {
        this.defaultProtocolCode = defaultProtocolCode;
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        if(in.readableBytes()==0){
            if(logger.isDebugEnabled()){
                logger.debug("内容读取完成:{}",in.readableBytes());
            }
            return;
        }

        Attribute<ProtocolCode> att = ctx.channel().attr(Connection.PROTOCOL);
        ProtocolCode protocolCode;
        if (att != null && att.get() != null) {
            protocolCode =  att.get();
        } else {
            protocolCode = this.defaultProtocolCode;
        }
        Protocol protocol = ProtocolManager.getProtocol(protocolCode);
        ;

        if (protocol == null) {
            throw new CodecException("Unknown protocol code: [" + protocolCode + "] while decode in ProtocolDecoder.");
        } else {
            protocol.getDecoder().decode(ctx, in, out);
        }
    }
}
