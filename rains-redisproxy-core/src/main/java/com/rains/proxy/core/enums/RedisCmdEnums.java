package com.rains.proxy.core.enums;

import com.rains.proxy.core.command.impl.RedisRequestPolicy;

import java.util.Objects;

import static com.rains.proxy.core.command.impl.CommandParse.*;

/**
 * @author dourx
 * 2018年 05 月  31日  10:32
 * @version V1.0
 * TODO
 */
public enum RedisCmdEnums {

    //Key
    DEL("DEL", RedisCmdTypeEnums.Key, new RedisRequestPolicy(COMMON_CMD, DEL_CMD, WRITE_CMD)),
    DUMP("DUMP", RedisCmdTypeEnums.Key, new RedisRequestPolicy(COMMON_CMD, THROUGH_CMD, WRITE_CMD)),        //返回被序列化的值
    EXISTS("EXISTS", RedisCmdTypeEnums.Key, new RedisRequestPolicy(COMMON_CMD, EXISTS_CMD, READ_CMD)),
    EXPIRE("EXPIRE", RedisCmdTypeEnums.Key, new RedisRequestPolicy(COMMON_CMD, THROUGH_CMD, WRITE_CMD)),
    EXPIREAT("EXPIREAT", RedisCmdTypeEnums.Key, new RedisRequestPolicy(COMMON_CMD, THROUGH_CMD, WRITE_CMD)),
    KEYS("KEYS", RedisCmdTypeEnums.Key, new RedisRequestPolicy(NO_CLUSTER_CMD, THROUGH_CMD, READ_CMD)),
    MIGRATE("MIGRATE", RedisCmdTypeEnums.Key, new RedisRequestPolicy(DISABLED_CMD, THROUGH_CMD, WRITE_CMD)),
    MOVE("MOVE", RedisCmdTypeEnums.Key, new RedisRequestPolicy(DISABLED_CMD, THROUGH_CMD, WRITE_CMD)),
    OBJECT("OBJECT", RedisCmdTypeEnums.Key, new RedisRequestPolicy(DISABLED_CMD, THROUGH_CMD, WRITE_CMD)),
    PERSIST("PERSIST", RedisCmdTypeEnums.Key, new RedisRequestPolicy(COMMON_CMD, THROUGH_CMD, WRITE_CMD)),        //移除给定 key 的生存时间
    PEXPIRE("PEXPIRE", RedisCmdTypeEnums.Key, new RedisRequestPolicy(COMMON_CMD, THROUGH_CMD, WRITE_CMD)),        //它以毫秒为单位设置 key 的生存时间
    PEXPIREAT("PEXPIREAT", RedisCmdTypeEnums.Key, new RedisRequestPolicy(COMMON_CMD, THROUGH_CMD, WRITE_CMD)),        //以毫秒为单位设置 key 的过期 unix 时间戳
    PTTL("PTTL", RedisCmdTypeEnums.Key, new RedisRequestPolicy(COMMON_CMD, THROUGH_CMD, WRITE_CMD)),        //以毫秒为单位返回 key 的剩余生存时间
    RANDOMKEY("RANDOMKEY", RedisCmdTypeEnums.Key, new RedisRequestPolicy(DISABLED_CMD, THROUGH_CMD, WRITE_CMD)),
    RENAME("RENAME", RedisCmdTypeEnums.Key, new RedisRequestPolicy(NO_CLUSTER_CMD, THROUGH_CMD, WRITE_CMD)),
    RENAMENX("RENAMENX", RedisCmdTypeEnums.Key, new RedisRequestPolicy(NO_CLUSTER_CMD, THROUGH_CMD, WRITE_CMD)),
    RESTORE("RESTORE", RedisCmdTypeEnums.Key, new RedisRequestPolicy(DISABLED_CMD, THROUGH_CMD, WRITE_CMD)),        //反序列化给定的序列化值，并将它和给定的 key 关联
    SORT("SORT", RedisCmdTypeEnums.Key, new RedisRequestPolicy(COMMON_CMD, THROUGH_CMD, WRITE_CMD)),
    TTL("TTL", RedisCmdTypeEnums.Key, new RedisRequestPolicy(COMMON_CMD, THROUGH_CMD, READ_CMD)),
    TYPE("TYPE", RedisCmdTypeEnums.Key, new RedisRequestPolicy(COMMON_CMD, THROUGH_CMD, READ_CMD)),
    SCAN("SCAN", RedisCmdTypeEnums.Key, new RedisRequestPolicy(COMMON_CMD, THROUGH_CMD, READ_CMD)),


    //String
    APPEND("APPEND", RedisCmdTypeEnums.String, new RedisRequestPolicy(COMMON_CMD, THROUGH_CMD, WRITE_CMD)),
    BITCOUNT("BITCOUNT", RedisCmdTypeEnums.String, new RedisRequestPolicy(COMMON_CMD, THROUGH_CMD, READ_CMD)),
    BITOP("BITOP", RedisCmdTypeEnums.String, new RedisRequestPolicy(NO_CLUSTER_CMD, THROUGH_CMD, WRITE_CMD)),
    BITFIELD("BITFIELD", RedisCmdTypeEnums.String, new RedisRequestPolicy(COMMON_CMD, THROUGH_CMD, WRITE_CMD)),
    DECR("DECR", RedisCmdTypeEnums.String, new RedisRequestPolicy(COMMON_CMD, THROUGH_CMD, WRITE_CMD)),
    DECRBY("DECRBY", RedisCmdTypeEnums.String, new RedisRequestPolicy(COMMON_CMD, THROUGH_CMD, WRITE_CMD)),
    GET("GET", RedisCmdTypeEnums.String, new RedisRequestPolicy(COMMON_CMD, THROUGH_CMD, READ_CMD)),
    GETBIT("GETBIT", RedisCmdTypeEnums.String, new RedisRequestPolicy(COMMON_CMD, THROUGH_CMD, READ_CMD)),
    GETRANGE("GETRANGE", RedisCmdTypeEnums.String, new RedisRequestPolicy(COMMON_CMD, THROUGH_CMD, READ_CMD)),
    GETSET("GETSET", RedisCmdTypeEnums.String, new RedisRequestPolicy(COMMON_CMD, THROUGH_CMD, WRITE_CMD)),
    INCR("INCR", RedisCmdTypeEnums.String, new RedisRequestPolicy(COMMON_CMD, THROUGH_CMD, WRITE_CMD)),
    INCRBY("INCRBY", RedisCmdTypeEnums.String, new RedisRequestPolicy(COMMON_CMD, THROUGH_CMD, WRITE_CMD)),
    INCRBYFLOAT("INCRBYFLOAT", RedisCmdTypeEnums.String, new RedisRequestPolicy(COMMON_CMD, THROUGH_CMD, WRITE_CMD)),
    MGET("MGET", RedisCmdTypeEnums.String, new RedisRequestPolicy(COMMON_CMD, MGETSET_CMD, READ_CMD)),
    MSET("MSET", RedisCmdTypeEnums.String, new RedisRequestPolicy(COMMON_CMD, MGETSET_CMD, WRITE_CMD)),
    MSETNX("MSETNX", RedisCmdTypeEnums.String, new RedisRequestPolicy(NO_CLUSTER_CMD, THROUGH_CMD, WRITE_CMD)),
    PSETEX("PSETEX", RedisCmdTypeEnums.String, new RedisRequestPolicy(COMMON_CMD, THROUGH_CMD, WRITE_CMD)),            //以毫秒为单位设置 key 的生存时间
    SET("SET", RedisCmdTypeEnums.String, new RedisRequestPolicy(COMMON_CMD, THROUGH_CMD, WRITE_CMD)),
    SETBIT("SETBIT", RedisCmdTypeEnums.String, new RedisRequestPolicy(COMMON_CMD, THROUGH_CMD, WRITE_CMD)),
    SETEX("SETEX", RedisCmdTypeEnums.String, new RedisRequestPolicy(COMMON_CMD, THROUGH_CMD, WRITE_CMD)),
    SETNX("SETNX", RedisCmdTypeEnums.String, new RedisRequestPolicy(COMMON_CMD, THROUGH_CMD, WRITE_CMD)),
    SETRANGE("SETRANGE", RedisCmdTypeEnums.String, new RedisRequestPolicy(COMMON_CMD, THROUGH_CMD, WRITE_CMD)),
    STRLEN("STRLEN", RedisCmdTypeEnums.String, new RedisRequestPolicy(COMMON_CMD, THROUGH_CMD, READ_CMD)),

    //Hash
    HDEL("HDEL", RedisCmdTypeEnums.Hash, new RedisRequestPolicy(COMMON_CMD, THROUGH_CMD, WRITE_CMD)),
    HEXISTS("HEXISTS", RedisCmdTypeEnums.Hash, new RedisRequestPolicy(COMMON_CMD, THROUGH_CMD, READ_CMD)),
    HGET("HGET", RedisCmdTypeEnums.Hash, new RedisRequestPolicy(COMMON_CMD, THROUGH_CMD, READ_CMD)),
    HGETALL("HGETALL", RedisCmdTypeEnums.Hash, new RedisRequestPolicy(COMMON_CMD, THROUGH_CMD, READ_CMD)),
    HINCRBY("HINCRBY", RedisCmdTypeEnums.Hash, new RedisRequestPolicy(COMMON_CMD, THROUGH_CMD, WRITE_CMD)),
    HINCRBYFLOAT("HINCRBYFLOAT", RedisCmdTypeEnums.Hash, new RedisRequestPolicy(COMMON_CMD, THROUGH_CMD, WRITE_CMD)),
    HKEYS("HKEYS", RedisCmdTypeEnums.Hash, new RedisRequestPolicy(COMMON_CMD, THROUGH_CMD, READ_CMD)),
    HLEN("HLEN", RedisCmdTypeEnums.Hash, new RedisRequestPolicy(COMMON_CMD, THROUGH_CMD, READ_CMD)),
    HMGET("HMGET", RedisCmdTypeEnums.Hash, new RedisRequestPolicy(COMMON_CMD, THROUGH_CMD, READ_CMD)),
    HMSET("HMSET", RedisCmdTypeEnums.Hash, new RedisRequestPolicy(COMMON_CMD, THROUGH_CMD, WRITE_CMD)),
    HSET("HSET", RedisCmdTypeEnums.Hash, new RedisRequestPolicy(COMMON_CMD, THROUGH_CMD, WRITE_CMD)),
    HSETNX("HSETNX", RedisCmdTypeEnums.Hash, new RedisRequestPolicy(COMMON_CMD, THROUGH_CMD, WRITE_CMD)),
    HVALS("HVALS", RedisCmdTypeEnums.Hash, new RedisRequestPolicy(COMMON_CMD, THROUGH_CMD, READ_CMD)),
    HSCAN("HSCAN", RedisCmdTypeEnums.Hash, new RedisRequestPolicy(COMMON_CMD, THROUGH_CMD, READ_CMD)),
    HSTRLEN("HSTRLEN", RedisCmdTypeEnums.Hash, new RedisRequestPolicy(COMMON_CMD, THROUGH_CMD, READ_CMD)),

    // List
    BLPOP("BLPOP", RedisCmdTypeEnums.List, new RedisRequestPolicy(NO_CLUSTER_CMD, BLOCK_CMD, READ_CMD)),
    BRPOP("BRPOP", RedisCmdTypeEnums.List, new RedisRequestPolicy(NO_CLUSTER_CMD, BLOCK_CMD, READ_CMD)),
    BRPOPLPUSH("BRPOPLPUSH", RedisCmdTypeEnums.List, new RedisRequestPolicy(NO_CLUSTER_CMD, BLOCK_CMD, WRITE_CMD)),

    LINDEX("LINDEX", RedisCmdTypeEnums.List, new RedisRequestPolicy(COMMON_CMD, THROUGH_CMD, READ_CMD)),
    LINSERT("LINSERT", RedisCmdTypeEnums.List, new RedisRequestPolicy(COMMON_CMD, THROUGH_CMD, WRITE_CMD)),
    LLEN("LLEN", RedisCmdTypeEnums.List, new RedisRequestPolicy(COMMON_CMD, THROUGH_CMD, READ_CMD)),
    LPOP("LPOP", RedisCmdTypeEnums.List, new RedisRequestPolicy(COMMON_CMD, THROUGH_CMD, READ_CMD)),
    LPUSH("LPUSH", RedisCmdTypeEnums.List, new RedisRequestPolicy(COMMON_CMD, THROUGH_CMD, WRITE_CMD)),
    LPUSHX("LPUSHX", RedisCmdTypeEnums.List, new RedisRequestPolicy(COMMON_CMD, THROUGH_CMD, WRITE_CMD)),
    LRANGE("LRANGE", RedisCmdTypeEnums.List, new RedisRequestPolicy(COMMON_CMD, THROUGH_CMD, READ_CMD)),
    LREM("LREM", RedisCmdTypeEnums.List, new RedisRequestPolicy(COMMON_CMD, THROUGH_CMD, WRITE_CMD)),
    LSET("LSET", RedisCmdTypeEnums.List, new RedisRequestPolicy(COMMON_CMD, THROUGH_CMD, WRITE_CMD)),
    LTRIM("LTRIM", RedisCmdTypeEnums.List, new RedisRequestPolicy(COMMON_CMD, THROUGH_CMD, WRITE_CMD)),
    RPOP("RPOP", RedisCmdTypeEnums.List, new RedisRequestPolicy(COMMON_CMD, THROUGH_CMD, READ_CMD)),

    RPOPLPUSH("RPOPLPUSH", RedisCmdTypeEnums.List, new RedisRequestPolicy(NO_CLUSTER_CMD, THROUGH_CMD, WRITE_CMD)),

    RPUSH("RPUSH", RedisCmdTypeEnums.List, new RedisRequestPolicy(COMMON_CMD, THROUGH_CMD, WRITE_CMD)),
    RPUSHX("RPUSHX", RedisCmdTypeEnums.List, new RedisRequestPolicy(COMMON_CMD, THROUGH_CMD, WRITE_CMD)),

    // Set
    SADD("SADD", RedisCmdTypeEnums.Set, new RedisRequestPolicy(COMMON_CMD, THROUGH_CMD, WRITE_CMD)),
    SCARD("SCARD", RedisCmdTypeEnums.Set, new RedisRequestPolicy(COMMON_CMD, THROUGH_CMD, READ_CMD)),
    SISMEMBER("SISMEMBER", RedisCmdTypeEnums.Set, new RedisRequestPolicy(COMMON_CMD, THROUGH_CMD, READ_CMD)),
    SMEMBERS("SMEMBERS", RedisCmdTypeEnums.Set, new RedisRequestPolicy(COMMON_CMD, THROUGH_CMD, READ_CMD)),
    SMOVE("SMOVE", RedisCmdTypeEnums.Set, new RedisRequestPolicy(COMMON_CMD, THROUGH_CMD, WRITE_CMD)),
    SPOP("SPOP", RedisCmdTypeEnums.Set, new RedisRequestPolicy(COMMON_CMD, THROUGH_CMD, WRITE_CMD)),
    SRANDMEMBER("SRANDMEMBER", RedisCmdTypeEnums.Set, new RedisRequestPolicy(COMMON_CMD, THROUGH_CMD, READ_CMD)),
    SREM("SREM", RedisCmdTypeEnums.Set, new RedisRequestPolicy(COMMON_CMD, THROUGH_CMD, WRITE_CMD)),
    SSCAN("SSCAN", RedisCmdTypeEnums.Set, new RedisRequestPolicy(COMMON_CMD, THROUGH_CMD, READ_CMD)),

    SDIFF("SDIFF", RedisCmdTypeEnums.Set, new RedisRequestPolicy(NO_CLUSTER_CMD, THROUGH_CMD, READ_CMD)),
    SDIFFSTORE("SDIFFSTORE", RedisCmdTypeEnums.Set, new RedisRequestPolicy(NO_CLUSTER_CMD, THROUGH_CMD, WRITE_CMD)),
    SINTER("SINTER", RedisCmdTypeEnums.Set, new RedisRequestPolicy(NO_CLUSTER_CMD, THROUGH_CMD, READ_CMD)),
    SINTERSTORE("SINTERSTORE", RedisCmdTypeEnums.Set, new RedisRequestPolicy(NO_CLUSTER_CMD, THROUGH_CMD, WRITE_CMD)),
    SUNION("SUNION", RedisCmdTypeEnums.Set, new RedisRequestPolicy(NO_CLUSTER_CMD, THROUGH_CMD, READ_CMD)),
    SUNIONSTORE("SUNIONSTORE", RedisCmdTypeEnums.Set, new RedisRequestPolicy(NO_CLUSTER_CMD, THROUGH_CMD, WRITE_CMD)),


    // SortedSet
    ZADD("ZADD", RedisCmdTypeEnums.SortedSet, new RedisRequestPolicy(COMMON_CMD, THROUGH_CMD, WRITE_CMD)),
    ZCARD("ZCARD", RedisCmdTypeEnums.SortedSet, new RedisRequestPolicy(COMMON_CMD, THROUGH_CMD, READ_CMD)),
    ZCOUNT("ZCOUNT", RedisCmdTypeEnums.SortedSet, new RedisRequestPolicy(COMMON_CMD, THROUGH_CMD, READ_CMD)),
    ZINCRBY("ZINCRBY", RedisCmdTypeEnums.SortedSet, new RedisRequestPolicy(COMMON_CMD, THROUGH_CMD, WRITE_CMD)),
    ZRANGE("ZRANGE", RedisCmdTypeEnums.SortedSet, new RedisRequestPolicy(COMMON_CMD, THROUGH_CMD, READ_CMD)),
    ZRANGEBYSCORE("ZRANGEBYSCORE", RedisCmdTypeEnums.SortedSet, new RedisRequestPolicy(COMMON_CMD, THROUGH_CMD, READ_CMD)),
    ZRANK("ZRANK", RedisCmdTypeEnums.SortedSet, new RedisRequestPolicy(COMMON_CMD, THROUGH_CMD, READ_CMD)),
    ZREM("ZREM", RedisCmdTypeEnums.SortedSet, new RedisRequestPolicy(COMMON_CMD, THROUGH_CMD, WRITE_CMD)),
    ZREMRANGEBYRANK("ZREMRANGEBYRANK", RedisCmdTypeEnums.SortedSet, new RedisRequestPolicy(COMMON_CMD, THROUGH_CMD, WRITE_CMD)),
    ZREMRANGEBYSCORE("ZREMRANGEBYSCORE", RedisCmdTypeEnums.SortedSet, new RedisRequestPolicy(COMMON_CMD, THROUGH_CMD, WRITE_CMD)),
    ZREVRANGE("ZREVRANGE", RedisCmdTypeEnums.SortedSet, new RedisRequestPolicy(COMMON_CMD, THROUGH_CMD, READ_CMD)),
    ZREVRANGEBYSCORE("ZREVRANGEBYSCORE", RedisCmdTypeEnums.SortedSet, new RedisRequestPolicy(COMMON_CMD, THROUGH_CMD, READ_CMD)),
    ZREVRANK("ZREVRANK", RedisCmdTypeEnums.SortedSet, new RedisRequestPolicy(COMMON_CMD, THROUGH_CMD, READ_CMD)),
    ZSCORE("ZSCORE", RedisCmdTypeEnums.SortedSet, new RedisRequestPolicy(COMMON_CMD, THROUGH_CMD, READ_CMD)),
    ZUNIONSTORE("ZUNIONSTORE", RedisCmdTypeEnums.SortedSet, new RedisRequestPolicy(NO_CLUSTER_CMD, THROUGH_CMD, WRITE_CMD)),
    ZINTERSTORE("ZINTERSTORE", RedisCmdTypeEnums.SortedSet, new RedisRequestPolicy(NO_CLUSTER_CMD, THROUGH_CMD, WRITE_CMD)),
    ZSCAN("ZSCAN", RedisCmdTypeEnums.SortedSet, new RedisRequestPolicy(COMMON_CMD, THROUGH_CMD, READ_CMD)),
    ZRANGEBYLEX("ZRANGEBYLEX", RedisCmdTypeEnums.SortedSet, new RedisRequestPolicy(COMMON_CMD, THROUGH_CMD, READ_CMD)),
    ZLEXCOUNT("ZLEXCOUNT", RedisCmdTypeEnums.SortedSet, new RedisRequestPolicy(COMMON_CMD, THROUGH_CMD, READ_CMD)),
    ZREMRANGEBYLEX("ZREMRANGEBYLEX", RedisCmdTypeEnums.SortedSet, new RedisRequestPolicy(COMMON_CMD, THROUGH_CMD, WRITE_CMD)),

    // HyperLogLog
    PFADD("PFADD", RedisCmdTypeEnums.HyperLogLog, new RedisRequestPolicy(DISABLED_CMD, THROUGH_CMD, WRITE_CMD)),
    PFCOUNT("PFCOUNT", RedisCmdTypeEnums.HyperLogLog, new RedisRequestPolicy(DISABLED_CMD, THROUGH_CMD, WRITE_CMD)),
    PFMERGE("PFMERGE", RedisCmdTypeEnums.HyperLogLog, new RedisRequestPolicy(DISABLED_CMD, THROUGH_CMD, WRITE_CMD)),

    // Geo
    GEOADD("GEOADD", RedisCmdTypeEnums.Geo, new RedisRequestPolicy(COMMON_CMD, THROUGH_CMD, WRITE_CMD)),
    GEOPOS("GEOPOS", RedisCmdTypeEnums.Geo, new RedisRequestPolicy(COMMON_CMD, THROUGH_CMD, READ_CMD)),
    GEODIST("GEODIST", RedisCmdTypeEnums.Geo, new RedisRequestPolicy(COMMON_CMD, THROUGH_CMD, READ_CMD)),
    GEORADIUS("GEORADIUS", RedisCmdTypeEnums.Geo, new RedisRequestPolicy(COMMON_CMD, THROUGH_CMD, READ_CMD)),
    GEORADIUSBYMEMBER("GEORADIUSBYMEMBER", RedisCmdTypeEnums.Geo, new RedisRequestPolicy(COMMON_CMD, THROUGH_CMD, READ_CMD)),
    GEOHASH("GEOHASH", RedisCmdTypeEnums.Geo, new RedisRequestPolicy(COMMON_CMD, THROUGH_CMD, READ_CMD)),

    // Pub/Sub
    PUBSUB("PUBSUB", RedisCmdTypeEnums.PubSub, new RedisRequestPolicy(DISABLED_CMD, THROUGH_CMD, READ_CMD)),        // 不支持
    PUBLISH("PUBLISH", RedisCmdTypeEnums.PubSub, new RedisRequestPolicy(COMMON_CMD, THROUGH_CMD, WRITE_CMD)),
    PSUBSCRIBE("PSUBSCRIBE", RedisCmdTypeEnums.PubSub, new RedisRequestPolicy(COMMON_CMD, PUBSUB_CMD, READ_CMD)),
    PUNSUBSCRIBE("PUNSUBSCRIBE", RedisCmdTypeEnums.PubSub, new RedisRequestPolicy(COMMON_CMD, PUBSUB_CMD, READ_CMD)),
    SUBSCRIBE("SUBSCRIBE", RedisCmdTypeEnums.PubSub, new RedisRequestPolicy(COMMON_CMD, PUBSUB_CMD, READ_CMD)),
    UNSUBSCRIBE("UNSUBSCRIBE", RedisCmdTypeEnums.PubSub, new RedisRequestPolicy(COMMON_CMD, PUBSUB_CMD, READ_CMD)),

    // Transaction
    MULTI("MULTI", RedisCmdTypeEnums.Transaction, new RedisRequestPolicy(DISABLED_CMD, THROUGH_CMD, WRITE_CMD)),    //开启事务
    EXEC("EXEC", RedisCmdTypeEnums.Transaction, new RedisRequestPolicy(DISABLED_CMD, THROUGH_CMD, WRITE_CMD)), //提交事务
    DISCARD("DISCARD", RedisCmdTypeEnums.Transaction, new RedisRequestPolicy(DISABLED_CMD, THROUGH_CMD, WRITE_CMD)),    //取消事务
    WATCH("WATCH", RedisCmdTypeEnums.Transaction, new RedisRequestPolicy(DISABLED_CMD, THROUGH_CMD, WRITE_CMD)),    //监视
    UNWATCH("UNWATCH", RedisCmdTypeEnums.Transaction, new RedisRequestPolicy(DISABLED_CMD, THROUGH_CMD, WRITE_CMD)), //取消监视

    // Script
    EVAL("EVAL", RedisCmdTypeEnums.Script, new RedisRequestPolicy(DISABLED_CMD, THROUGH_CMD, WRITE_CMD)),
    EVALSHA("EVALSHA", RedisCmdTypeEnums.Script, new RedisRequestPolicy(DISABLED_CMD, THROUGH_CMD, WRITE_CMD)),
    SCRIPT("SCRIPT", RedisCmdTypeEnums.Script, new RedisRequestPolicy(DISABLED_CMD, THROUGH_CMD, WRITE_CMD)),

    // Connection
    AUTH("AUTH", RedisCmdTypeEnums.Connection, new RedisRequestPolicy(COMMON_CMD, NO_THROUGH_CMD, READ_CMD)),
    ECHO("ECHO", RedisCmdTypeEnums.Connection, new RedisRequestPolicy(COMMON_CMD, NO_THROUGH_CMD, READ_CMD)),
    PING("PING", RedisCmdTypeEnums.Connection, new RedisRequestPolicy(COMMON_CMD, NO_THROUGH_CMD, READ_CMD)),
    QUIT("QUIT", RedisCmdTypeEnums.Connection, new RedisRequestPolicy(COMMON_CMD, NO_THROUGH_CMD, READ_CMD)),
    SELECT("SELECT", RedisCmdTypeEnums.Connection, new RedisRequestPolicy(COMMON_CMD, NO_THROUGH_CMD, READ_CMD)),

    // Server
    BGREWRITEAOF("BGREWRITEAOF", RedisCmdTypeEnums.Server, new RedisRequestPolicy(DISABLED_CMD, THROUGH_CMD, WRITE_CMD)),
    BGSAVE("BGSAVE", RedisCmdTypeEnums.Server, new RedisRequestPolicy(DISABLED_CMD, THROUGH_CMD, WRITE_CMD)),
    GETNAME("GETNAME", RedisCmdTypeEnums.Server, new RedisRequestPolicy(DISABLED_CMD, THROUGH_CMD, WRITE_CMD)),

    CLIENT("CLIENT", RedisCmdTypeEnums.Server, new RedisRequestPolicy(NO_CLUSTER_CMD, THROUGH_CMD, WRITE_CMD)),

    SETNAME("SETNAME", RedisCmdTypeEnums.Server, new RedisRequestPolicy(DISABLED_CMD, THROUGH_CMD, WRITE_CMD)),
    CONFIG("CONFIG", RedisCmdTypeEnums.Server, new RedisRequestPolicy(DISABLED_CMD, THROUGH_CMD, WRITE_CMD)),
    RESETSTAT("RESETSTAT", RedisCmdTypeEnums.Server, new RedisRequestPolicy(DISABLED_CMD, THROUGH_CMD, WRITE_CMD)),
    REWRITE("REWRITE", RedisCmdTypeEnums.Server, new RedisRequestPolicy(DISABLED_CMD, THROUGH_CMD, WRITE_CMD)),
    DBSIZE("DBSIZE", RedisCmdTypeEnums.Server, new RedisRequestPolicy(DISABLED_CMD, THROUGH_CMD, WRITE_CMD)),
    DEBUG("DEBUG", RedisCmdTypeEnums.Server, new RedisRequestPolicy(DISABLED_CMD, THROUGH_CMD, WRITE_CMD)),
    SEGFAULT("SEGFAULT", RedisCmdTypeEnums.Server, new RedisRequestPolicy(DISABLED_CMD, THROUGH_CMD, WRITE_CMD)),
    FLUSHALL("FLUSHALL", RedisCmdTypeEnums.Server, new RedisRequestPolicy(DISABLED_CMD, THROUGH_CMD, WRITE_CMD)),
    FLUSHDB("FLUSHDB", RedisCmdTypeEnums.Server, new RedisRequestPolicy(DISABLED_CMD, THROUGH_CMD, WRITE_CMD)),

    INFO("INFO", RedisCmdTypeEnums.Server, new RedisRequestPolicy(NO_CLUSTER_CMD, THROUGH_CMD, READ_CMD)),

    LASTSAVE("LASTSAVE", RedisCmdTypeEnums.Server, new RedisRequestPolicy(DISABLED_CMD, THROUGH_CMD, WRITE_CMD)),
    MONITOR("MONITOR", RedisCmdTypeEnums.Server, new RedisRequestPolicy(DISABLED_CMD, THROUGH_CMD, WRITE_CMD)),
    PSYNC("PSYNC", RedisCmdTypeEnums.Server, new RedisRequestPolicy(DISABLED_CMD, THROUGH_CMD, WRITE_CMD)),
    SAVE("SAVE", RedisCmdTypeEnums.Server, new RedisRequestPolicy(DISABLED_CMD, THROUGH_CMD, WRITE_CMD)),
    SHUTDOWN("SHUTDOWN", RedisCmdTypeEnums.Server, new RedisRequestPolicy(DISABLED_CMD, THROUGH_CMD, WRITE_CMD)),
    SLAVEOF("SLAVEOF", RedisCmdTypeEnums.Server, new RedisRequestPolicy(DISABLED_CMD, THROUGH_CMD, WRITE_CMD)),
    SLOWLOG("SLOWLOG", RedisCmdTypeEnums.Server, new RedisRequestPolicy(DISABLED_CMD, THROUGH_CMD, WRITE_CMD)),
    SYNC("SYNC", RedisCmdTypeEnums.Server, new RedisRequestPolicy(DISABLED_CMD, THROUGH_CMD, WRITE_CMD)),
    TIME("TIME", RedisCmdTypeEnums.Server, new RedisRequestPolicy(COMMON_CMD, THROUGH_CMD, READ_CMD)),

    COMMAND("COMMAND", RedisCmdTypeEnums.Server, new RedisRequestPolicy(DISABLED_CMD, THROUGH_CMD, WRITE_CMD)),        // command count/getkeys/info/
    WAIT("WAIT", RedisCmdTypeEnums.Server, new RedisRequestPolicy(DISABLED_CMD, THROUGH_CMD, WRITE_CMD)),
    ROLE("ROLE", RedisCmdTypeEnums.Server, new RedisRequestPolicy(DISABLED_CMD, THROUGH_CMD, WRITE_CMD)),
    READONLY("READONLY", RedisCmdTypeEnums.Server, new RedisRequestPolicy(DISABLED_CMD, THROUGH_CMD, WRITE_CMD)),        // 执行该命令后，可以在slave上执行只读命令
    READWRITE("READWRITE", RedisCmdTypeEnums.Server, new RedisRequestPolicy(DISABLED_CMD, THROUGH_CMD, WRITE_CMD)),        // 执行该命令后，取消在slave上执行命令
    TOUCH("TOUCH", RedisCmdTypeEnums.Server, new RedisRequestPolicy(DISABLED_CMD, THROUGH_CMD, WRITE_CMD)),        //
    UNLINK("UNLINK", RedisCmdTypeEnums.Server, new RedisRequestPolicy(DISABLED_CMD, THROUGH_CMD, WRITE_CMD));        //


    private String command;

    private RedisCmdTypeEnums type;

    private RedisRequestPolicy policy;


    private RedisCmdEnums(String command, RedisCmdTypeEnums type, RedisRequestPolicy policy) {
        this.command = command;
        this.type = type;
        this.policy = policy;
    }

    public String getCommand() {
        return command;
    }

    public RedisCmdTypeEnums getType() {
        return type;
    }

    public RedisRequestPolicy getPolicy() {
        return policy;
    }
    public static RedisRequestPolicy getPolicy(String cmd) {
        if(Objects.isNull(cmd)){
            return new RedisRequestPolicy(DISABLED_CMD, NO_THROUGH_CMD, READ_CMD);
        }
        RedisCmdEnums redisCmdEnums= RedisCmdEnums.valueOf(cmd.toUpperCase());
        if(redisCmdEnums==null){
            return new RedisRequestPolicy(DISABLED_CMD, NO_THROUGH_CMD, READ_CMD);
        }
        return redisCmdEnums.policy;
    }
}
