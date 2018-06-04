package com.rains.proxy.core.command.execute;

import com.rains.proxy.core.command.ICmdExecute;
import com.rains.proxy.core.command.impl.RedisCommand;
import com.rains.proxy.core.reply.IRedisReply;
import com.rains.proxy.core.reply.impl.BulkRedisReply;
import com.rains.proxy.core.reply.impl.ErrorRedisReply;
import com.rains.proxy.core.reply.impl.StatusRedisReply;
import com.rains.proxy.core.utils.ProtoUtils;
import com.rains.proxy.core.utils.StringUtils;
import io.netty.util.CharsetUtil;

/**
 * @author dourx
 * 2018年 05 月  30日  11:32
 * @version V1.0
 * TODO
 */
public class ConnectionCmdExecute implements ICmdExecute {

    private static final String ERR_MSG = "ERR wrong number of arguments for '%s' command";

    @Override
    public IRedisReply execute(RedisCommand request) {
        String cmd =new String(request.getArgs().get(0));

        switch (cmd.toUpperCase()) {
            case "AUTH":
                if (request.getArgs().size() < 2) {
                    String errMsg = String.format(ERR_MSG, cmd);
                    return new ErrorRedisReply(errMsg.getBytes());
                }
                return auth(request);
            case "ECHO":
                if (request.getArgs().size() < 2) {
                    String errMsg = String.format(ERR_MSG, cmd);
                    return new ErrorRedisReply(errMsg.getBytes());
                }
                return new StatusRedisReply(request.getArgs().get(1));
            case "PING":
                return new StatusRedisReply(ProtoUtils.PONG);
            case "QUIT":
                return new BulkRedisReply();
            case "SELECT":
                if (request.getArgs().size() < 2) {
                    String errMsg = String.format(ERR_MSG, cmd);
                    return new ErrorRedisReply(errMsg.getBytes());
                }
                return new StatusRedisReply(ProtoUtils.OK);
            default:
                return new StatusRedisReply(ProtoUtils.PONG);
        }

    }


    // Auth
    private IRedisReply auth(RedisCommand request) {


        String password = new String(request.getArgs().get(1), CharsetUtil.UTF_8);

        //  todo 以后增加认证
        if (StringUtils.isNotBlank(password)) {
            return new StatusRedisReply(ProtoUtils.OK);
        }
        return new StatusRedisReply(ProtoUtils.OK);
    }



}
