package com.rains.proxy.core.protocol.request.decoder;

import com.rains.proxy.core.command.impl.CommandParse;
import com.rains.proxy.core.command.impl.RedisCommand;
import com.rains.proxy.core.command.impl.RedisRequestPolicy;
import com.rains.proxy.core.constants.RedisConstants;
import com.rains.proxy.core.enums.RedisCmdEnums;
import com.rains.proxy.core.enums.RequestState;
import com.rains.proxy.core.utils.ProtoUtils;
import io.netty.buffer.ByteBuf;

import java.util.ArrayList;
import java.util.List;


/**
 * @author dourx
 * @version V1.0
 * 创建日期 2018/5/30
 *
 * 解析新协议
 */

public class RedisNewRequestDecoder implements  IRedisRequestDecoder {
    @Override
    public RequestState readArg(ByteBuf buffer, RedisCommand requestCommand) throws Exception {
        if (requestCommand.getArgs() == null || requestCommand.getArgs().size() == 0) {
            List<byte[]> args = new ArrayList<>(requestCommand.getArgCount());
            while (args.size() < requestCommand.getArgCount()) {
                char ch = (char) buffer.readByte();
                if (ch ==RedisConstants.DOLLAR_BYTE) {
                    int length = ProtoUtils.readInt(buffer);
                    byte[] argByte = new byte[length];
                    buffer.readBytes(argByte);
                    buffer.skipBytes(2);//skip \r\n
                    //LoggerUtils.info("String:"+new String(argByte));
                    args.add(argByte);
                } else {
                    throw new Exception("READ_ARG Unexpected character,ch:" + String.valueOf(ch));
                }
            }
            String  command =new String(args.get(0));
            RedisRequestPolicy policy = RedisCmdEnums.getPolicy(command.toUpperCase());
            requestCommand.setPolicy(policy);
            requestCommand.setArgs(args);
        }

        return RequestState.READ_END;
    }


}
