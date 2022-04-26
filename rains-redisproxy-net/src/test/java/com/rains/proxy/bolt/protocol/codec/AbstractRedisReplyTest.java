package com.rains.proxy.bolt.protocol.codec;

import com.alipay.remoting.ProtocolManager;
import com.rains.proxy.bolt.protocol.RedisClientProtocol;
import io.netty.buffer.ByteBuf;

/**
 * @author dourx
 * @version V1.0
 * @Description: TODO
 * @date 2018年 05 月  24日  0:05
 */
public class AbstractRedisReplyTest {
    static  {
        ProtocolManager.registerProtocol(new RedisClientProtocol(), RedisClientProtocol.PROTOCOL_CODE);
    }
    protected String readToStr(ByteBuf buf, int len){
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < len; i++) {
            sb.append((char)buf.readByte());
        }
        return sb.toString();
    }

}
