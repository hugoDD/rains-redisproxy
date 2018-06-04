package com.rains.proxy.core.reply.impl;

import io.netty.buffer.ByteBuf;

import static org.junit.Assert.*;

/**
 * @author dourx
 * @version V1.0
 * @Description: TODO
 * @date 2018年 05 月  24日  0:05
 */
public class AbstractRedisReplyTest {
    protected String readToStr(ByteBuf buf, int len){
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < len; i++) {
            sb.append((char)buf.readByte());
        }
        return sb.toString();
    }

}