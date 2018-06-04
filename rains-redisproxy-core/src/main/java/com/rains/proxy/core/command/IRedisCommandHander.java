package com.rains.proxy.core.command;

import com.rains.proxy.core.reply.IRedisReply;

/**
 * @author dourx
 * @version V1.0
 * 2018年 05 月  30日  10:43
 * redis请求命令处理接口
 */
public interface IRedisCommandHander<T> {

    IRedisReply hander(T command);
}
