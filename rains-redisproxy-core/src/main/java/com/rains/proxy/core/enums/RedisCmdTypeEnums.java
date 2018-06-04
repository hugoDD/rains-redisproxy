package com.rains.proxy.core.enums;

/**
 * @author dourx
 * 2018年 05 月  31日  13:42
 * @version V1.0
 * TODO
 */
public enum RedisCmdTypeEnums {

    Key,
    String,
    Hash,
    List,
    Set,
    SortedSet,
    HyperLogLog,
    Geo,
    PubSub,
    Transaction,
    Script,
    Connection,
    Server;
}
