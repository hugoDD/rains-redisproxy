package com.rains.proxy.net.service.impl;

import com.rains.proxy.net.model.Server;
import com.rains.proxy.net.service.ServerService;
import com.rains.proxy.net.utils.ByteUtils;
import com.rains.proxy.net.utils.SocketManager;
import com.rains.proxy.net.utils.SocketUtils;
import io.netty.channel.Channel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Base64;

/**
 * create by lorne on 2017/9/21
 */
@Service
public class ServerServiceImpl implements ServerService {


    @Autowired
    private Server server;



    @Override
    public Server getServer() {

        Server server = new Server();
        server.setMaxCount(SocketManager.getInstance().getMaxConnection());
        server.setNowCount(SocketManager.getInstance().getNowConnection());

        server.setIp(server.getIp());
        server.setPort(server.getPort());
        server.setTag(server.getTag());

        return server;
    }


    @Override
    public boolean sendHexCmd(String uniqueKey, String cmd) {
        SocketUtils.send(uniqueKey, ByteUtils.fromHexAscii(cmd));
        return true;
    }


    @Override
    public boolean sendBase64Cmd(String uniqueKey, String cmd) {
        SocketUtils.send(uniqueKey, Base64.getDecoder().decode(cmd));

        return true;
    }

    @Override
    public boolean sendStrCmd(String uniqueKey, String cmd) {
        SocketUtils.send(uniqueKey, cmd.getBytes());
        return true;
    }

    @Override
    public boolean checkChannel(String uniqueKey) {
        Channel channel =  SocketManager.getInstance().getChannelByUniqueKey(uniqueKey);
        return channel!=null&&channel.isActive();
    }
}
