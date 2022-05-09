package com.rains.proxy.bolt.domain;

import com.rains.proxy.core.command.impl.RedisCommand;
import com.rains.proxy.core.utils.RedisCmdUtils;
import org.junit.Test;

import java.util.stream.Stream;

import static org.junit.Assert.*;

public class ClusterDomainTest {

    @Test
    public void select() {
        ClusterDomain clusterDomain = new ClusterDomain();
        RedisCommand writeRequest = RedisCmdUtils.createCmd("set mykey myvalue");
       String host = clusterDomain.select(writeRequest);
       assertEquals(host,"172.26.223.109:16379");
        RedisCommand readRequest = RedisCmdUtils.createCmd("get mykey");
        String slave = clusterDomain.select(readRequest);
        assertNotNull(slave);
        boolean match = Stream.of("172.26.223.109:26379", "172.26.223.109:36379").anyMatch(slave::equals);
        assertTrue(match);
    }
}