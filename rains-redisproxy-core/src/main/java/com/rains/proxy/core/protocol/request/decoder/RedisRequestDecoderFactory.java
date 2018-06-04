package com.rains.proxy.core.protocol.request.decoder;

/**
 * @author dourx
 * @version V1.0
 * @Description: TODO
 * @date 2018年 05 月  29日  15:03
 */
public class RedisRequestDecoderFactory {

    private static  IRedisRequestDecoder newRequestDecoder = new RedisNewRequestDecoder();
    private static  IRedisRequestDecoder inlineRequestDecoder = new RedisInLineRequestDecoder();


    public static IRedisRequestDecoder getInstance(boolean inline){
        return inline?inlineRequestDecoder : newRequestDecoder;
    }
}
