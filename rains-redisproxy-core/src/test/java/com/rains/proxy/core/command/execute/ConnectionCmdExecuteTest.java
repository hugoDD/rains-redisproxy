package com.rains.proxy.core.command.execute;

import com.rains.proxy.core.command.BaseCmdTest;
import com.rains.proxy.core.command.ICmdExecute;
import com.rains.proxy.core.command.impl.RedisCommand;
import com.rains.proxy.core.enums.Type;
import com.rains.proxy.core.reply.IRedisReply;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * @author dourx
 * 2018年 05 月  30日  14:52
 * @version V1.0
 * TODO
 */
public class ConnectionCmdExecuteTest extends BaseCmdTest {
    ICmdExecute execute;

    @Before
    public void setUp() throws Exception {
        execute = new ConnectionCmdExecute();
    }

    @Test
    public void execute() {
        //auth

        RedisCommand command =createCmd("auth");
        assertNotNull(command);
       IRedisReply reply= execute.execute(command);
        assertNotNull(reply);
        assertEquals(Type.ERROR,reply.getType());

         command =createCmd("ECHO");
        assertNotNull(command);
        reply= execute.execute(command);
        assertNotNull(reply);
        assertEquals(Type.ERROR,reply.getType());



        command =createCmd("SELECT");
        assertNotNull(command);
        reply= execute.execute(command);
        assertNotNull(reply);
        assertEquals(Type.ERROR,reply.getType());


        command =createCmd("PING");
        assertNotNull(command);
        reply= execute.execute(command);
        assertNotNull(reply);
        assertEquals(Type.STATUS,reply.getType());

        command =createCmd("QUIT");
        assertNotNull(command);
        reply= execute.execute(command);
        assertNotNull(reply);
        assertEquals(Type.BULK,reply.getType());

        command =createCmd("SELECT 1");
        assertNotNull(command);
        reply= execute.execute(command);
        assertNotNull(reply);
        assertEquals(Type.STATUS,reply.getType());

        command =createCmd("auth 1");
        assertNotNull(command);
        reply= execute.execute(command);
        assertNotNull(reply);
        assertEquals(Type.STATUS,reply.getType());

        command =createCmd("ECHO 1");
        assertNotNull(command);
        reply= execute.execute(command);
        assertNotNull(reply);
        assertEquals(Type.STATUS,reply.getType());

    }


}