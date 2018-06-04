package com.rains.proxy.core.protocol;

import com.rains.proxy.core.command.impl.RedisCommand;
import com.rains.proxy.core.constants.RedisConstants;
import com.rains.proxy.core.enums.RequestState;
import com.rains.proxy.core.exception.RedisRequestException;
import com.rains.proxy.core.protocol.request.decoder.IRedisRequestDecoder;
import com.rains.proxy.core.protocol.request.decoder.RedisRequestDecoderFactory;
import com.rains.proxy.core.utils.ProtoUtils;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ReplayingDecoder;
import io.netty.util.CharsetUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * @author dourx
 * @version V1.0
 * @Description: TODO
 * @date 2018年 05 月  29日  14:01
 */
public  class RedisRequestDecoder extends ReplayingDecoder<RequestState> {
    private static final Logger logger = LoggerFactory.getLogger(RedisRequestDecoder.class);

    private RedisCommand requestCommand;

    public RedisRequestDecoder() {
        super(RequestState.READ_SKIP);
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf buffer,
                          List<Object> out) throws Exception {


        switch (state()) {
            case READ_SKIP: {
                try {
                    skipChar(buffer);
                    checkpoint(RequestState.READ_INIT);
                } finally {
                    checkpoint();
                }
            }
            case READ_INIT: {
                requestCommand = new RedisCommand();
                char ch = (char) buffer.readByte();
                if (ch == RedisConstants.ASTERISK_BYTE) {//redis 新协议开头
                    requestCommand.setInline(false);
                    //读取参数数量(新协议)
                    checkpoint(RequestState.READ_ARG_COUNT);
                } else {
                    buffer.resetReaderIndex();
                    requestCommand.setInline(true);
                    //读取参数
                    checkpoint(RequestState.READ_ARG);

                }

            }
            case READ_ARG_COUNT: {
                if (requestCommand != null && !requestCommand.isInline()) {
                    requestCommand.setArgCount(ProtoUtils.readInt(buffer));
                }
                checkpoint(RequestState.READ_ARG);
            }
            case READ_ARG: {
                IRedisRequestDecoder decoder =RedisRequestDecoderFactory.getInstance(requestCommand.isInline());
                RequestState state=   decoder.readArg(buffer,requestCommand);

                checkpoint(state);
            }
            case READ_END: {
                RedisCommand command = this.requestCommand;
                this.requestCommand = null;
                checkpoint(RequestState.READ_INIT);
                out.add(command);
                if(logger.isDebugEnabled()){
                    logger.debug("request解码前协议文本内容:{}",buffer.slice(0,buffer.readerIndex()).toString(CharsetUtil.UTF_8).replaceAll("\r\n","\\\\r\\\\n"));
                }
                return;
            }
            default:
                throw new Error("can't reach here!");
        }
    }

    private int readInt(ByteBuf buffer) throws Exception {
        StringBuilder sb = new StringBuilder();
        char ch = (char) buffer.readByte();
        while (ch != RedisConstants.CR_BYTE) {
            sb.append(ch);
            ch = (char) buffer.readByte();//\r读取
        }
        buffer.readByte();//\n读取
        try {
            int result = 1;
            if (!sb.toString().toLowerCase().equals(RedisConstants.PING)) {
                result = Integer.parseInt(sb.toString());
            }
            return result;
        } catch (Exception e) {//网络闭包引起

            throw new Exception("readInt Unexpected character,result:" + sb.toString() + ",ch:" + String.valueOf(ch));
        }
    }



    private void skipChar(ByteBuf buffer) {
        for (; ; ) {
            char ch = (char) buffer.readByte();
            if (ch != RedisConstants.SPACE_BYTE) {
                buffer.readerIndex(buffer.readerIndex() - 1);
                break;
            }
        }
    }


}
