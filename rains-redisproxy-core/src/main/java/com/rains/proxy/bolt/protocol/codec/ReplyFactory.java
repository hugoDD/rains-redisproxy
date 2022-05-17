package com.rains.proxy.bolt.protocol.codec;

import com.rains.proxy.core.constants.RedisConstants;
import com.rains.proxy.core.reply.IRedisReply;
import com.rains.proxy.core.reply.impl.*;

public class ReplyFactory {
    public static IRedisReply buildReply(char ch){
        IRedisReply redisReply = null;
        if (ch == RedisConstants.ASTERISK_BYTE) {
            redisReply = new MultyBulkRedisReply();
        } else if (ch == RedisConstants.DOLLAR_BYTE) {
            redisReply = new BulkRedisReply();
        } else if (ch == RedisConstants.COLON_BYTE) {
            redisReply = new IntegerRedisReply();
        } else if (ch == RedisConstants.OK_BYTE) {
            redisReply = new StatusRedisReply();
        } else if (ch == RedisConstants.ERROR_BYTE) {
            redisReply = new ErrorRedisReply();
        }
        return redisReply;
    }
}
