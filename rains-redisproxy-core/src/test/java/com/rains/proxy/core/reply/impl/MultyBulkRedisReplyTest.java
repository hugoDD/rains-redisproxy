package com.rains.proxy.core.reply.impl;

import com.rains.proxy.core.constants.RedisConstants;
import com.rains.proxy.core.enums.Type;
import com.rains.proxy.core.reply.IRedisReply;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author dourx
 * @version V1.0
 * @Description: 多条批量回复单元测试
 * @date 2018年 05 月  24日  1:21
 */
public class MultyBulkRedisReplyTest extends AbstractRedisReplyTest{


    /**
     * 像 LRANGE 这样的命令需要返回多个值， 这一目标可以通过多条批量回复来完成。
     *
     * 多条批量回复是由多个回复组成的数组， 数组中的每个元素都可以是任意类型的回复， 包括多条批量回复本身。
     *
     * 多条批量回复的第一个字节为 "*" ， 后跟一个字符串表示的整数值， 这个值记录了多条批量回复所包含的回复数量， 再后面是一个 CRLF 。
     *
     * 客户端： LRANGE mylist 0 3
     * 服务器： *4
     * 服务器： $3
     * 服务器： foo
     * 服务器： $3
     * 服务器： bar
     * 服务器： $5
     * 服务器： Hello
     * 服务器： $5
     * 服务器： World
     */
    @Test
    public void encodeByBulk() {
        ByteBuf buf = Unpooled.buffer();
        MultyBulkRedisReply redisReply = new MultyBulkRedisReply();
        redisReply.setCount(4);
        BulkRedisReply bulkRedisReply1 = new BulkRedisReply("foo".getBytes());
        bulkRedisReply1.setLength(3);
        redisReply.addReply(bulkRedisReply1);

        BulkRedisReply bulkRedisReply2 = new BulkRedisReply("bar".getBytes());
        bulkRedisReply1.setLength(3);
        redisReply.addReply(bulkRedisReply2);

        BulkRedisReply bulkRedisReply3 = new BulkRedisReply("Hello".getBytes());
        bulkRedisReply1.setLength(5);
        redisReply.addReply(bulkRedisReply3);

        BulkRedisReply bulkRedisReply4 = new BulkRedisReply("World".getBytes());
        bulkRedisReply1.setLength(5);
        redisReply.addReply(bulkRedisReply4);

        redisReply.encode(buf);


        assertEquals(redisReply.getType(), Type.MULTYBULK);

        assertTrue(buf.isReadable());

        assertEquals("*4",readToStr(buf,2));
        assertEquals(RedisConstants.CR_BYTE,(char)buf.readByte());
        assertEquals(RedisConstants.LF_BYTE,(char)buf.readByte());

        assertEquals("$3",readToStr(buf,2));
        assertEquals(RedisConstants.CR_BYTE,(char)buf.readByte());
        assertEquals(RedisConstants.LF_BYTE,(char)buf.readByte());
        assertEquals("foo",readToStr(buf,3));
        assertEquals(RedisConstants.CR_BYTE,(char)buf.readByte());
        assertEquals(RedisConstants.LF_BYTE,(char)buf.readByte());

        assertEquals("$3",readToStr(buf,2));
        assertEquals(RedisConstants.CR_BYTE,(char)buf.readByte());
        assertEquals(RedisConstants.LF_BYTE,(char)buf.readByte());
        assertEquals("bar",readToStr(buf,3));
        assertEquals(RedisConstants.CR_BYTE,(char)buf.readByte());
        assertEquals(RedisConstants.LF_BYTE,(char)buf.readByte());

        assertEquals("$5",readToStr(buf,2));
        assertEquals(RedisConstants.CR_BYTE,(char)buf.readByte());
        assertEquals(RedisConstants.LF_BYTE,(char)buf.readByte());
        assertEquals("Hello",readToStr(buf,5));
        assertEquals(RedisConstants.CR_BYTE,(char)buf.readByte());
        assertEquals(RedisConstants.LF_BYTE,(char)buf.readByte());

        assertEquals("$5",readToStr(buf,2));
        assertEquals(RedisConstants.CR_BYTE,(char)buf.readByte());
        assertEquals(RedisConstants.LF_BYTE,(char)buf.readByte());
        assertEquals("World",readToStr(buf,5));
        assertEquals(RedisConstants.CR_BYTE,(char)buf.readByte());
        assertEquals(RedisConstants.LF_BYTE,(char)buf.readByte());

        assertFalse(buf.isReadable());



    }


    /**
     * 多条批量回复中的元素可以将自身的长度设置为 -1 ， 从而表示该元素不存在， 并且也不是一个空白字符串（empty string）。
     *
     * 当 SORT 命令使用 GET pattern 选项对一个不存在的键进行操作时， 就会发生多条批量回复中带有空白元素的情况。
     *
     * 以下例子展示了一个包含空元素的多重批量回复：
     *
     * 服务器： *3
     * 服务器： $3
     * 服务器： foo
     * 服务器： $-1
     * 服务器： $3
     * 服务器： bar
     * 其中， 回复中的第二个元素为空。
     */
    @Test
    public void encodeByBulkButOneEmpty(){
        ByteBuf buf = Unpooled.buffer();
        MultyBulkRedisReply redisReply = new MultyBulkRedisReply();
        redisReply.setCount(3);
        BulkRedisReply bulkRedisReply1 = new BulkRedisReply("foo".getBytes());
        bulkRedisReply1.setLength(3);
        redisReply.addReply(bulkRedisReply1);

        BulkRedisReply bulkRedisReply2 = new BulkRedisReply();
        bulkRedisReply1.setLength(-1);
        redisReply.addReply(bulkRedisReply2);

        BulkRedisReply bulkRedisReply3 = new BulkRedisReply("bar".getBytes());
        bulkRedisReply1.setLength(3);
        redisReply.addReply(bulkRedisReply3);

        redisReply.encode(buf);


        assertEquals(Type.MULTYBULK,redisReply.getType());

        assertTrue(buf.isReadable());
        assertEquals("*3",readToStr(buf,2));
        assertEquals(RedisConstants.CR_BYTE,(char)buf.readByte());
        assertEquals(RedisConstants.LF_BYTE,(char)buf.readByte());

        assertEquals("$3",readToStr(buf,2));
        assertEquals(RedisConstants.CR_BYTE,(char)buf.readByte());
        assertEquals(RedisConstants.LF_BYTE,(char)buf.readByte());
        assertEquals("foo",readToStr(buf,3));
        assertEquals(RedisConstants.CR_BYTE,(char)buf.readByte());
        assertEquals(RedisConstants.LF_BYTE,(char)buf.readByte());

        assertEquals("$-1",readToStr(buf,3));
        assertEquals(RedisConstants.CR_BYTE,(char)buf.readByte());
        assertEquals(RedisConstants.LF_BYTE,(char)buf.readByte());

        assertEquals("$3",readToStr(buf,2));
        assertEquals(RedisConstants.CR_BYTE,(char)buf.readByte());
        assertEquals(RedisConstants.LF_BYTE,(char)buf.readByte());
        assertEquals("bar",readToStr(buf,3));
        assertEquals(RedisConstants.CR_BYTE,(char)buf.readByte());
        assertEquals(RedisConstants.LF_BYTE,(char)buf.readByte());

        assertFalse(buf.isReadable());

    }


    /**
     * 多条批量回复， 回复中包含四个整数值， 以及一个二进制安全字符串：
     *
     * *5\r\n
     * :1\r\n
     * :2\r\n
     * :3\r\n
     * :4\r\n
     * $6\r\n
     * foobar\r\n
     */
    @Test
    public void encodeIntegerAndBluk(){
        ByteBuf buf = Unpooled.buffer();
        MultyBulkRedisReply redisReply = new MultyBulkRedisReply();
        redisReply.setCount(5);

        IntegerRedisReply redisReply1 = new IntegerRedisReply("1".getBytes());
        redisReply.addReply(redisReply1);

        IntegerRedisReply redisReply2 = new IntegerRedisReply("2".getBytes());
        redisReply.addReply(redisReply2);

        IntegerRedisReply redisReply3 = new IntegerRedisReply("3".getBytes());
        redisReply.addReply(redisReply3);

        IntegerRedisReply redisReply4 = new IntegerRedisReply("-1".getBytes());
        redisReply.addReply(redisReply4);

        BulkRedisReply bulkRedisReply = new BulkRedisReply("foobar".getBytes());
        bulkRedisReply.setLength(6);
        redisReply.addReply(bulkRedisReply);

        redisReply.encode(buf);

        assertEquals(Type.MULTYBULK,redisReply.getType());

        assertTrue(buf.isReadable());

        assertEquals("*5",readToStr(buf,2));
        assertEquals(RedisConstants.CR_BYTE,(char)buf.readByte());
        assertEquals(RedisConstants.LF_BYTE,(char)buf.readByte());

        assertEquals(":1",readToStr(buf,2));
        assertEquals(RedisConstants.CR_BYTE,(char)buf.readByte());
        assertEquals(RedisConstants.LF_BYTE,(char)buf.readByte());

        assertEquals(":2",readToStr(buf,2));
        assertEquals(RedisConstants.CR_BYTE,(char)buf.readByte());
        assertEquals(RedisConstants.LF_BYTE,(char)buf.readByte());

        assertEquals(":3",readToStr(buf,2));
        assertEquals(RedisConstants.CR_BYTE,(char)buf.readByte());
        assertEquals(RedisConstants.LF_BYTE,(char)buf.readByte());

        assertEquals(":-1",readToStr(buf,3));
        assertEquals(RedisConstants.CR_BYTE,(char)buf.readByte());
        assertEquals(RedisConstants.LF_BYTE,(char)buf.readByte());

        assertEquals("$6",readToStr(buf,2));
        assertEquals(RedisConstants.CR_BYTE,(char)buf.readByte());
        assertEquals(RedisConstants.LF_BYTE,(char)buf.readByte());

        assertEquals("foobar",readToStr(buf,6));
        assertEquals(RedisConstants.CR_BYTE,(char)buf.readByte());
        assertEquals(RedisConstants.LF_BYTE,(char)buf.readByte());

        assertFalse(buf.isReadable());


    }

    /**
     * 多条批量回复也可以是空白的（empty）， 就像这样：
     *
     * 客户端： LRANGE nokey 0 1
     * 服务器： *0\r\n
     */
    @Test
    public void encodeByEmpty(){
        ByteBuf buf = Unpooled.buffer();
        MultyBulkRedisReply redisReply = new MultyBulkRedisReply();

        redisReply.encode(buf);

        assertEquals(Type.MULTYBULK,redisReply.getType());

        assertTrue(buf.isReadable());

        assertEquals("*0",readToStr(buf,2));
        assertEquals(RedisConstants.CR_BYTE,(char)buf.readByte());
        assertEquals(RedisConstants.LF_BYTE,(char)buf.readByte());

    }

    /**
     * 无内容的多条批量回复（null multi bulk reply）也是存在的， 比如当 BLPOP 命令的阻塞时间超过最大时限时， 它就返回一个无内容的多条批量回复， 这个回复的计数值为 -1 ：
     *
     * 客户端： BLPOP key 1
     * 服务器： *-1\r\n
     */
    @Test
    public void encodeByNull(){
        ByteBuf buf = Unpooled.buffer();
        MultyBulkRedisReply redisReply = new MultyBulkRedisReply();
        redisReply.setCount(-1);
        redisReply.encode(buf);

        assertEquals(Type.MULTYBULK,redisReply.getType());

        assertTrue(buf.isReadable());

        assertEquals("*-1",readToStr(buf,3));
        assertEquals(RedisConstants.CR_BYTE,(char)buf.readByte());
        assertEquals(RedisConstants.LF_BYTE,(char)buf.readByte());
    }


}