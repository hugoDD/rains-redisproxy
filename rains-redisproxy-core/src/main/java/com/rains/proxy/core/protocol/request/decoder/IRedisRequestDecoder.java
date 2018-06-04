package com.rains.proxy.core.protocol.request.decoder;

import com.rains.proxy.core.command.impl.RedisCommand;
import com.rains.proxy.core.enums.RequestState;
import com.rains.proxy.core.exception.RedisRequestException;
import io.netty.buffer.ByteBuf;

/**
 * @author dourx
 * @version V1.0
 * @Description: TODO
 * @date 2018年 05 月  29日  14:50
 */
public interface IRedisRequestDecoder {

    RequestState readArg(ByteBuf buf, RedisCommand requestCommand)throws Exception;


}
