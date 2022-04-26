package com.rains.proxy.bolt.protocol.codec;

import com.alipay.remoting.Connection;
import com.alipay.remoting.Protocol;
import com.alipay.remoting.ProtocolCode;
import com.alipay.remoting.ProtocolManager;
import com.alipay.remoting.codec.Codec;
import com.rains.proxy.bolt.protocol.RedisClientProtocol;
import com.rains.proxy.bolt.remoting.RedisBoltCodec;
import com.rains.proxy.core.enums.Type;
import com.rains.proxy.core.protocol.RedisReplyDecoder;
import com.rains.proxy.core.reply.impl.*;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.embedded.EmbeddedChannel;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author dourx
 * @version V1.0
 * @Description: TODO
 * @date 2018年 05 月  24日  2:52
 */
public class RedisBoltReplyDecoderTest extends AbstractRedisReplyTest {





    @Test
    public void testRedisProtocol(){
        Protocol redisClientProtocol = ProtocolManager.getProtocol(ProtocolCode.fromBytes(RedisClientProtocol.PROTOCOL_CODE));
        Assert.assertNotNull(redisClientProtocol);
        assertTrue(redisClientProtocol instanceof RedisClientProtocol);

        assertNotNull(redisClientProtocol.getDecoder());
        assertTrue(redisClientProtocol.getDecoder() instanceof RedisBoltReplyDecoder);

        Codec codec = new RedisBoltCodec();
        assertNotNull(codec);
        assertNotNull(codec.newDecoder());

    }

    @Test
    public void decodeByBluk() {
        //"$6\r\nfoobar\r\n"
        ByteBuf buf = Unpooled.buffer();
        buf.writeBytes("$6\r\nfoobar\r\n".getBytes());

        ByteBuf encodeBuf =  Unpooled.buffer();

        EmbeddedChannel channel = getChannel();

        assertTrue(channel.writeInbound(buf));

        //将该 Channel 标记为已完成状态
        assertTrue(channel.finish());

        //读取产生的消息，并且验证值
        CommonRedisReply bulkReply =  channel.readInbound();
        bulkReply.encode(encodeBuf);
        assertNotNull(bulkReply);
        assertTrue(bulkReply instanceof BulkRedisReply);
        assertEquals(bulkReply.getType(), Type.BULK);
        assertEquals("$6\r\nfoobar\r\n", readToStr(encodeBuf,"$6\r\nfoobar\r\n".length()));

    }

    private EmbeddedChannel getChannel() {
        Codec codec = new RedisBoltCodec();
        EmbeddedChannel channel = new EmbeddedChannel(codec.newDecoder());
        channel.attr(Connection.PROTOCOL).set(ProtocolCode.fromBytes(RedisClientProtocol.PROTOCOL_CODE));
        return channel;
    }

    @Test
    public void decodeByErr() {
        //-ERR unknown command 'foobar'
        String errMsg ="-ERR unknown command 'foobar'\r\n";
        ByteBuf buf = Unpooled.buffer();
        buf.writeBytes(errMsg.getBytes());

        ByteBuf encodeBuf =  Unpooled.buffer();


        EmbeddedChannel channel = getChannel();

        assertTrue(channel.writeInbound(buf));

        //将该 Channel 标记为已完成状态
        assertTrue(channel.finish());

        //读取产生的消息，并且验证值
        CommonRedisReply errReply =  channel.readInbound();
        errReply.encode(encodeBuf);
        assertNotNull(errReply);
        assertTrue(errReply instanceof ErrorRedisReply);
        assertEquals(errReply.getType(), Type.ERROR);
        assertEquals(errMsg, readToStr(encodeBuf,errMsg.length()));

    }

    @Test
    public void decodeByInteger() {
        //测试 ":1000\r\n"
        String msg =":1000\r\n";
        ByteBuf buf = Unpooled.buffer();
        buf.writeBytes(msg.getBytes());

        ByteBuf encodeBuf =  Unpooled.buffer();


        EmbeddedChannel channel = getChannel();
        assertTrue(channel.writeInbound(buf));

        //将该 Channel 标记为已完成状态
        assertTrue(channel.finish());

        //读取产生的消息，并且验证值
        CommonRedisReply errReply =  channel.readInbound();
        errReply.encode(encodeBuf);
        assertNotNull(errReply);
        assertTrue(errReply instanceof IntegerRedisReply);
        assertEquals(errReply.getType(), Type.INTEGER);
        assertEquals(msg, readToStr(encodeBuf,msg.length()));

    }

    @Test
    public void decodeByStatus() {
        //测试 "+OK"
        String msg ="+OK\r\n";
        ByteBuf buf = Unpooled.buffer();
        buf.writeBytes(msg.getBytes());

        ByteBuf encodeBuf =  Unpooled.buffer();


        EmbeddedChannel channel = getChannel();
        assertTrue(channel.writeInbound(buf));

        //将该 Channel 标记为已完成状态
        assertTrue(channel.finish());

        //读取产生的消息，并且验证值
        CommonRedisReply errReply =  channel.readInbound();
        errReply.encode(encodeBuf);
        assertNotNull(errReply);
        assertTrue(errReply instanceof StatusRedisReply);
        assertEquals(errReply.getType(), Type.STATUS);
        assertEquals(msg, readToStr(encodeBuf,msg.length()));

    }
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
    public void decodeByMulty(){
        String msg ="*4\r\n$3\r\nfoo\r\n$3\r\nbar\r\n$5\r\nHello\r\n$5\r\nWorld\r\n";
        decodeByMultyCommon(msg);

        msg="*3\r\n$3\r\nfoo\r\n$-1\r\n$3\r\nbar\r\n";
        decodeByMultyCommon(msg);

        msg="*5\r\n:1\r\n:2\r\n:3\r\n:4\r\n$6\r\nfoobar\r\n";
        decodeByMultyCommon(msg);


        msg = "*0\r\n";
        decodeByMultyCommon(msg);

        msg ="*-1\r\n";
        decodeByMultyCommon(msg);

         msg ="*7\r\n$1\r\n0\r\n$2\r\nt2\r\n$5\r\nmkey1\r\n$5\r\nhkey1\r\n$5\r\nmykey\r\n$5\r\nmkey2\r\n$2\r\nt1\r\n";
        decodeByMultyCommon(msg);


    }

    @Test
    public void decodeByScan(){
        String msg ="*2\r\n$1\r\n0\r\n*7\r\n$2\r\nt3\r\n$2\r\nt2\r\n$5\r\nmkey1\r\n$5\r\nhkey1\r\n$5\r\nmykey\r\n$5\r\nmkey2\r\n$2\r\nt2\r\n";
        ByteBuf buf = Unpooled.buffer();
        buf.writeBytes(msg.getBytes());

        ByteBuf encodeBuf =  Unpooled.buffer();


        EmbeddedChannel channel = getChannel();

        assertTrue(channel.writeInbound(buf));

        //将该 Channel 标记为已完成状态
        assertTrue(channel.finish());

        //读取产生的消息，并且验证值
        CommonRedisReply errReply =  channel.readInbound();
        errReply.encode(encodeBuf);
        assertNotNull(errReply);
        assertTrue(errReply instanceof MultyBulkRedisReply);
        assertEquals(errReply.getType(), Type.MULTYBULK);
        assertEquals(msg.length(),encodeBuf.writerIndex());
    }


    public void decodeByMultyCommon(String msg) {
        //测试 "+OK"

        ByteBuf buf = Unpooled.buffer();
        buf.writeBytes(msg.getBytes());

        ByteBuf encodeBuf =  Unpooled.buffer();


       EmbeddedChannel channel = getChannel();

        assertTrue(channel.writeInbound(buf));

        //将该 Channel 标记为已完成状态
        assertTrue(channel.finish());

        //读取产生的消息，并且验证值
        CommonRedisReply errReply =  channel.readInbound();
        errReply.encode(encodeBuf);
        assertNotNull(errReply);
        assertTrue(errReply instanceof MultyBulkRedisReply);
        assertEquals(errReply.getType(), Type.MULTYBULK);
        assertEquals(msg, readToStr(encodeBuf,msg.length()));

    }
}
