package com.rains.proxy.core.reply.impl;

import com.rains.proxy.core.constants.RedisConstants;
import com.rains.proxy.core.enums.Type;
import com.rains.proxy.core.reply.IRedisReply;
import com.rains.proxy.core.utils.ProtoUtils;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author dourx
 * @version V1.0
 * @Description: TODO
 * @date 2018年 05 月  23日  23:28
 */
public class BulkRedisReplyTest extends AbstractRedisReplyTest{


    /**
     * 服务器发送的内容中：
     *
     * 第一字节为 "$" 符号
     * 接下来跟着的是表示实际回复长度的数字值
     * 之后跟着一个 CRLF
     * 再后面跟着的是实际回复数据
     * 最末尾是另一个 CRLF
     * 对于前面的 GET 命令，服务器实际发送的内容为：
     *
     * "$6\r\nfoobar\r\n"
     */
    @Test
    public void encode() {
       // String msgStr = "$6\r\nfoobar\r\n";
        ByteBuf buf = Unpooled.buffer();
        IRedisReply redisReply = new BulkRedisReply();
        ((BulkRedisReply) redisReply).setLength(6);
        ((BulkRedisReply) redisReply).setValue("foobar".getBytes());
        redisReply.encode(buf);



    //  byte[] bytes= ProtoUtils.convertIntToByteArray(3);
        assertEquals(Type.BULK,redisReply.getType());
        assertTrue(buf.isReadable());
        assertEquals('$',(char)buf.readByte());
        assertEquals('6',(char)buf.readByte());
        assertEquals(RedisConstants.CR_BYTE,buf.readByte());
        assertEquals(RedisConstants.LF_BYTE,buf.readByte());
        assertEquals("foobar",readToStr(buf,6));
        assertEquals(RedisConstants.CR_BYTE,buf.readByte());
        assertEquals(RedisConstants.LF_BYTE,buf.readByte());
        assertFalse(buf.isReadable());
    }

    /**
     * 如果被请求的值不存在， 那么批量回复会将特殊值 -1 用作回复的长度值， 就像这样：
     *
     * 客户端：GET non-existing-key
     * 服务器：$-1
     * 这种回复称为空批量回复（NULL Bulk Reply）
     */
    @Test
    public void encodeNull(){
        ByteBuf buf = Unpooled.buffer();
        IRedisReply redisReply = new BulkRedisReply();
        ((BulkRedisReply) redisReply).setLength(-1);
        redisReply.encode(buf);

        assertEquals(Type.BULK,redisReply.getType());
        assertTrue(buf.isReadable());
        assertEquals('$',(char)buf.readByte());
        assertEquals('-',(char)buf.readByte());
        assertEquals('1',(char)buf.readByte());
        assertEquals(RedisConstants.CR_BYTE,buf.readByte());
        assertEquals(RedisConstants.LF_BYTE,buf.readByte());

        assertFalse(buf.isReadable());
    }


}