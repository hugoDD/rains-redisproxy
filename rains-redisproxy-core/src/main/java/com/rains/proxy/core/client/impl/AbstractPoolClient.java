/**
 * 
 */
package com.rains.proxy.core.client.impl;



import com.rains.proxy.core.client.Client;
import com.rains.proxy.core.command.impl.RedisCommand;
import com.rains.proxy.core.connection.IConnection;
import com.rains.proxy.core.exception.RedisException;
import com.rains.proxy.core.log.impl.LoggerUtils;
import com.rains.proxy.core.pool.LBRedisProxyPoolEntry;
import com.rains.proxy.core.pool.LBRedisProxyPooledObjectFactory;
import com.rains.proxy.core.pool.commons.LBRedisProxyPoolConfig;
import com.rains.proxy.core.pool.impl.LBRedisProxyBasicPool;
import com.rains.proxy.core.pool.util.LBRedisProxyChannelPoolUtils;
import io.netty.channel.ChannelHandlerContext;

/**
 * @author liubing
 *
 */
public abstract class AbstractPoolClient implements Client {
	
	protected LBRedisProxyBasicPool<IConnection> pool;
    protected LBRedisProxyPoolConfig ffanRedisProxyPoolConfig;
    protected LBRedisProxyPooledObjectFactory<IConnection> factory;

	/**
	 * 
	 */
	public AbstractPoolClient(LBRedisProxyPoolConfig ffanRedisProxyPoolConfig) {
		super();
		this.ffanRedisProxyPoolConfig=ffanRedisProxyPoolConfig;
	}
    
	protected void initPool() {
		try{
            factory = createChannelFactory();
            pool = LBRedisProxyChannelPoolUtils.createPool(ffanRedisProxyPoolConfig, factory);
		}catch(Exception e){
			LoggerUtils.error("initPool fail,reason:"+e.getCause()+",message:"+e.getMessage(), e);
		}
	}
	
	/**
	 * 创建一个工厂类
	 * @return
	 */
    protected abstract LBRedisProxyPooledObjectFactory<IConnection> createChannelFactory();
    
    public abstract void write(RedisCommand request, ChannelHandlerContext frontCtx);
    
    protected LBRedisProxyPoolEntry<IConnection> borrowObject() throws Exception {
    	LBRedisProxyPoolEntry<IConnection> nettyChannelEntry=pool.borrowEntry();
        if (nettyChannelEntry != null&&nettyChannelEntry.getObject()!=null) {
            return nettyChannelEntry;
        }
        
        String errorMsg = this.getClass().getSimpleName() + " borrowObject Error";
        LoggerUtils.error(errorMsg);
        throw new RedisException(errorMsg);
    }


    protected void returnObject(LBRedisProxyPoolEntry<IConnection> entry) {
        if (entry == null) {
            return;
        }
        try {
        	pool.returnEntry(entry);
        } catch (Exception ie) {
        	LoggerUtils.error(this.getClass().getSimpleName() + " return client Error" , ie);
        }
    }

}
