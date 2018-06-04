package com.rains.proxy.core.protocol.request.decoder;

import com.rains.proxy.core.command.impl.CommandParse;
import com.rains.proxy.core.command.impl.RedisCommand;
import com.rains.proxy.core.command.impl.RedisRequestPolicy;
import com.rains.proxy.core.constants.RedisConstants;
import com.rains.proxy.core.enums.RedisCmdEnums;
import com.rains.proxy.core.enums.RequestState;
import com.rains.proxy.core.log.impl.LoggerUtils;
import com.rains.proxy.core.utils.ProtoUtils;
import io.netty.buffer.ByteBuf;
import org.springframework.util.Assert;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * @author dourx
 * @version V1.0
 * @Description: TODO
 * @date 2018年 05 月  29日  15:27
 */
public class RedisInLineRequestDecoder implements IRedisRequestDecoder {
    @Override
    public RequestState readArg(ByteBuf buf, RedisCommand requestCommand) throws Exception {
        int start = 0;
        int len = 0;
        List<byte[]> args = new ArrayList<>();
        byte[] src;
        while (buf.isReadable()) {
//             && buf.readByte() != '\n'
            char c = (char) buf.readByte();



            if(c==RedisConstants.SPACE_BYTE ){//set mykey 7\r\nmyvalue\r\n
                //  buf.resetReaderIndex();
                Assert.notNull(buf,"redis command is null");
                src = new byte[len];

                ByteBuf slice =buf.slice(start,len);
                slice.readBytes(src);

                len=0;
                if(start==0){
                    String  command =new String(src);
                    RedisRequestPolicy policy = RedisCmdEnums.getPolicy(command.toUpperCase());
                    requestCommand.setPolicy(policy);

                }
                start = buf.readerIndex();
                if (LoggerUtils.isDebugEnabled()) {
                    LoggerUtils.debug("Inline command: {},index=:{}", new String(src), start);
                }
                args.add(src);

            }else if(c == RedisConstants.CR_BYTE && buf.readByte()== RedisConstants.LF_BYTE){//QUIT\r\n auth\r\n ping\r\n
                Assert.notNull(buf,"redis command is null");


                if(start==0){
                    src = new byte[len];
                    ByteBuf slice =buf.slice(start,len);
                    slice.readBytes(src);
                    String  command =new String(src);
                    RedisRequestPolicy policy = RedisCmdEnums.getPolicy(command.toUpperCase());
                    requestCommand.setPolicy(policy);
                    args.add(src);
                    if (LoggerUtils.isDebugEnabled()) {
                        LoggerUtils.debug("Inline command: {},index=:{}", new String(src), buf.readerIndex());
                    }
                    break;
                }else {
                    buf.readerIndex(start);
                    len= ProtoUtils.readInt(buf);
                    src = new byte[len];
                    start = buf.readerIndex();
                    ByteBuf slice =buf.slice(start,len);
                    slice.readBytes(src);
                    args.add(src);
                    buf.skipBytes(len+2);//skip data len and \r\n
                   // buf.release();
                    if (LoggerUtils.isDebugEnabled()) {
                        LoggerUtils.debug("Inline command: {},index=:{}", new String(src), buf.readerIndex());
                    }
                  break;



                }
            } else {
                len++;
                if (LoggerUtils.isDebugEnabled()) {
                    LoggerUtils.debug("index=:{},len:{}",  buf.readerIndex(),len);
                }

            }
        }





        requestCommand.setArgs(args);
        requestCommand.setArgCount(args.size());

        if (LoggerUtils.isDebugEnabled()) {
            LoggerUtils.debug("complete Inline command: {}", requestCommand.toString());
        }

        return  RequestState.READ_END;
    }


}
