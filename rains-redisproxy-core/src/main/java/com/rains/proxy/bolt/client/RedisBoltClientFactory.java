package com.rains.proxy.bolt.client;

import com.alipay.remoting.ConnectionEventType;
import com.alipay.remoting.config.Configs;
import com.rains.proxy.bolt.processor.CONNECTEventProcessor;
import com.rains.proxy.bolt.processor.DISCONNECTEventProcessor;

/**
 * @author ly-dourx
 */
public class RedisBoltClientFactory {
    private static RedisBoltClientFactory factory = new RedisBoltClientFactory();

    private RedisBoltClient client;

    private RedisBoltClientFactory() {
    }

    public static RedisBoltClient getClient() {

        return factory.newClient();
    }

    public synchronized RedisBoltClient newClient() {
        if (client != null) {
            return client;
        }
        System.setProperty(Configs.TCP_IDLE_SWITCH, "false");
        // 1. create a rpc client

        client = new RedisBoltClient();
        // 2. add processor for connect and close event if you need
        client.addConnectionEventProcessor(ConnectionEventType.CONNECT, new CONNECTEventProcessor());
        client.addConnectionEventProcessor(ConnectionEventType.CLOSE, new DISCONNECTEventProcessor());
        // 3. do init
        client.startup();
        return client;
    }
}
