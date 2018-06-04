package com.rains.proxy.core.exception;

/**
 * @author dourx
 * @version V1.0
 * @Description: TODO
 * @date 2018年 05 月  28日  16:45
 */
public class RedisRequestException extends Throwable {
    public RedisRequestException(Throwable cause) {
        super(cause);
    }

    public RedisRequestException(String message) {
        super(message);
    }

    public RedisRequestException(String message, Throwable cause) {
        super(message, cause);
    }

    protected RedisRequestException(String message, Throwable cause,
                             boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
