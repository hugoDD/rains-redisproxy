/**
 * 
 */
package com.rains.proxy.core.connection;

import com.rains.proxy.core.command.impl.RedisCommand;
import com.rains.proxy.core.pool.commons.Pool;
import io.netty.channel.ChannelHandlerContext;

/**
 * @author liubing
 *
 */
public interface IConnection extends Pool {
	
	public void write(RedisCommand request, ChannelHandlerContext frontCtx);
	
	 /**
     * open the channel
     * 
     * @return
     */
    boolean open();

    /**
     * close the channel.
     */
    void close();

    /**
     * close the channel gracefully.
     */
    void close(int timeout);

    /**
     * is closed.
     * 
     * @return closed
     */
    boolean isClosed();

    /**
     * the node available status
     * 
     * @return
     */
    boolean isAvailable();
}
