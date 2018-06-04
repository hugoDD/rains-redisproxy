package com.rains.proxy.core.command.impl;

import com.rains.proxy.core.constants.RedisConstants;
import com.rains.proxy.core.log.impl.LogService;
import com.rains.proxy.core.log.impl.LoggerUtils;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.EmptyByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.util.CharsetUtil;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * @author dourx
 * @version V1.0
 * @Description: TODO
 * @date 2018年 05 月  22日  14:01
 */
public class RedisCommandTest {


    @Test
    public void encode() {
        //set mykey myvalue
        ByteBuf buf = Unpooled.buffer();;
        RedisCommand redisCommand = new RedisCommand();
        redisCommand.setArgCount(3);
        List<byte[]> args = new ArrayList<>();
        args.add("set".getBytes());
        args.add("mykey".getBytes());
        args.add("myvalue".getBytes());
        redisCommand.setArgs(args);

        redisCommand.encode(buf);

        assertEquals('*',(char)buf.readByte());
        assertEquals('3',(char)buf.readByte());
        assertEquals(RedisConstants.CR_BYTE,(char)buf.readByte());
        assertEquals(RedisConstants.LF_BYTE,(char)buf.readByte());

        assertEquals('$',(char)buf.readByte());
        assertEquals('3',(char)buf.readByte());
        assertEquals(RedisConstants.CR_BYTE,(char)buf.readByte());
        assertEquals(RedisConstants.LF_BYTE,(char)buf.readByte());

        assertEquals("set",readStr(buf,"set".length()));
        assertEquals(RedisConstants.CR_BYTE,(char)buf.readByte());
        assertEquals(RedisConstants.LF_BYTE,(char)buf.readByte());

        assertEquals('$',(char)buf.readByte());
        assertEquals('5',(char)buf.readByte());
        assertEquals(RedisConstants.CR_BYTE,(char)buf.readByte());
        assertEquals(RedisConstants.LF_BYTE,(char)buf.readByte());

        assertEquals("mykey",readStr(buf,"mykey".length()));
        assertEquals(RedisConstants.CR_BYTE,(char)buf.readByte());
        assertEquals(RedisConstants.LF_BYTE,(char)buf.readByte());


        assertEquals('$',(char)buf.readByte());
        assertEquals('7',(char)buf.readByte());
        assertEquals(RedisConstants.CR_BYTE,(char)buf.readByte());
        assertEquals(RedisConstants.LF_BYTE,(char)buf.readByte());


        assertEquals("myvalue",readStr(buf,"myvalue".length()));
        assertEquals(RedisConstants.CR_BYTE,(char)buf.readByte());
        assertEquals(RedisConstants.LF_BYTE,(char)buf.readByte());


        buf.resetReaderIndex();
        while (buf.isReadable()) {
            System.out.print((char)buf.readByte());
        }
       //LoggerUtils.info( buf.toString());
    }

    private String readStr(ByteBuf buf,int len){
        StringBuilder sb = new StringBuilder();
        for(int i =0 ;i<len ;i++){
            sb.append((char)buf.readByte());
        }
        return  sb.toString();
    }


}