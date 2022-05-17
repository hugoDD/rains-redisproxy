package com.rains.proxy.core.reply.impl;

import com.rains.proxy.core.enums.Type;
import io.netty.buffer.ByteBuf;
import io.netty.handler.codec.redis.RedisCodecException;

/**
 * @author dourx
 * 2018年 05 月  30日  18:17
 * @version V1.0
 * TODO
 */
public class EmptyRedisReply extends  AbstractRedisReply {

    public EmptyRedisReply() {
        super(Type.EMPTY);
    }
    @Override
    public void doEncode(ByteBuf out) {
        throw new RedisCodecException("empty redis reply doencode is error");
    }

    @Override
    public boolean handler(ByteBuf in) {
        return true;
    }
}
