package com.rains.proxy.core.utils;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author dourx
 * @version V1.0
 * @Description: TODO
 * @date 2018年 05 月  30日  9:48
 */
public class ProtoUtilsTest {

    @Test
    public void convertIntToByteArray() {
        int a = -100;
        byte[] result = ProtoUtils.convertIntToByteArray(a);
        assertEquals(4,result.length);
        assertEquals('-',result[0]);
        assertEquals('1',result[1]);
        assertEquals('0',result[2]);
        assertEquals('0',result[3]);


        a = 100;
        result = ProtoUtils.convertIntToByteArray(a);
        assertEquals(3,result.length);
        assertEquals('1',result[0]);
        assertEquals('0',result[1]);
        assertEquals('0',result[2]);

    }

    @Test
    public void buildErrorReplyBytes() {
     byte[] errorReply=   ProtoUtils.buildErrorReplyBytes("closed by upstream");
     assertNotNull(errorReply);
     assertEquals("ERR closed by upstream",new String(errorReply));
    }

    @Test
    public void readIn(){
        ByteBuf buf = Unpooled.buffer();
        buf.writeBytes("100\r\n".getBytes());
        int v=ProtoUtils.readInt(buf);
        assertEquals(100,v);
        //test skip \r\n
        assertEquals( buf.writerIndex(),buf.readerIndex());


        buf.resetWriterIndex();
        buf.resetReaderIndex();
        buf.writeBytes("-100\r\n".getBytes());
         v=ProtoUtils.readInt(buf);
        assertEquals(-100,v);
        //test skip \r\n
        assertEquals( buf.writerIndex(),buf.readerIndex());

    }
}