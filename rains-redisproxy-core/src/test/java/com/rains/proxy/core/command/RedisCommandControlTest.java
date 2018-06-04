package com.rains.proxy.core.command;

import com.rains.proxy.core.command.impl.RedisCommand;
import com.rains.proxy.core.enums.Type;
import com.rains.proxy.core.reply.IRedisReply;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author dourx
 * 2018年 05 月  30日  15:21
 * @version V1.0
 * TODO
 */
public class RedisCommandControlTest extends BaseCmdTest{

    @Before
    public void setUp() throws Exception {
    }

    @Test
    public void actionForConn() {
        RedisCommandControl redisCommandControl = new RedisCommandControl();

        RedisCommand command =createCmd("auth");
        assertNotNull(command);
        IRedisReply reply= redisCommandControl.action(command);
        assertNotNull(reply);
        assertEquals(Type.ERROR,reply.getType());

        command =createCmd("ECHO");
        assertNotNull(command);
        reply= redisCommandControl.action(command);
        assertNotNull(reply);
        assertEquals(Type.ERROR,reply.getType());



        command =createCmd("SELECT");
        assertNotNull(command);
        reply=redisCommandControl.action(command);
        assertNotNull(reply);
        assertEquals(Type.ERROR,reply.getType());


        command =createCmd("PING");
        assertNotNull(command);
        reply= redisCommandControl.action(command);
        assertNotNull(reply);
        assertEquals(Type.STATUS,reply.getType());

        command =createCmd("QUIT");
        assertNotNull(command);
        reply= redisCommandControl.action(command);
        assertNotNull(reply);
        assertEquals(Type.STATUS,reply.getType());

        command =createCmd("SELECT 1");
        assertNotNull(command);
        reply= redisCommandControl.action(command);
        assertNotNull(reply);
        assertEquals(Type.STATUS,reply.getType());

        command =createCmd("auth 1");
        assertNotNull(command);
        reply=redisCommandControl.action(command);
        assertNotNull(reply);
        assertEquals(Type.STATUS,reply.getType());

        command =createCmd("ECHO 1");
        assertNotNull(command);
        reply= redisCommandControl.action(command);
        assertNotNull(reply);
        assertEquals(Type.STATUS,reply.getType());


    }
}