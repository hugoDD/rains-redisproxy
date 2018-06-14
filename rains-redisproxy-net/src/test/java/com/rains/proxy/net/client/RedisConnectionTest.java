package com.rains.proxy.net.client;

import com.rains.proxy.core.command.impl.RedisCommand;
import com.rains.proxy.net.Uitls;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author dourx
 * 2018年 06 月  13日  15:03
 * @version V1.0
 * TODO
 */
public class RedisConnectionTest {
    RedisConnection connection;
    RedisCommand pingcmd;
    RedisCommand setcmd;
    RedisCommand getcmd;
    @Before
    public void setUp() throws Exception {
        connection = new RedisConnection("172.26.223.109",16379,3000);
        pingcmd= Uitls.getRedisCommand("ping");
        setcmd= Uitls.getRedisCommand("set mykey myvalue");
        getcmd= Uitls.getRedisCommand("get mykey");
    }

    @After
    public void tearDown() throws Exception {
        connection.close();
    }

    @Test
    public void write() {
        connection.write(pingcmd,null);
        connection.write(setcmd,null);
        connection.write(getcmd,null);

    }
}