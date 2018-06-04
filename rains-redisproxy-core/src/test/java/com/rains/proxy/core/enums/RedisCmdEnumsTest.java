package com.rains.proxy.core.enums;

import org.junit.Test;

import static com.rains.proxy.core.command.impl.CommandParse.COMMON_CMD;
import static com.rains.proxy.core.command.impl.CommandParse.DEL_CMD;
import static org.junit.Assert.*;

/**
 * @author dourx
 * 2018年 05 月  31日  14:03
 * @version V1.0
 * TODO
 */
public class RedisCmdEnumsTest {

    @Test
    public void getCommand() {

        RedisCmdEnums cmdEnums=   RedisCmdEnums.valueOf("DEL");
        assertNotNull(cmdEnums);
        assertEquals(cmdEnums.getCommand(),"DEL");
        assertEquals(cmdEnums.getType(),RedisCmdTypeEnums.Key);
        assertNotNull(cmdEnums.getPolicy());
        assertEquals(cmdEnums.getPolicy().getCategory(),COMMON_CMD);
        assertEquals(cmdEnums.getPolicy().getHandleType(),DEL_CMD);

    }
}