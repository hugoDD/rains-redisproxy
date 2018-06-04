/**
 * 
 */
package com.rains.proxy.net.client;


import com.rains.proxy.core.client.impl.AbstractPoolClient;
import com.rains.proxy.core.command.impl.RedisCommand;
import com.rains.proxy.core.connection.IConnection;
import com.rains.proxy.core.pool.LBRedisProxyPoolEntry;
import com.rains.proxy.core.pool.LBRedisProxyPooledObjectFactory;
import com.rains.proxy.core.pool.commons.LBRedisProxyPoolConfig;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * shard connection
 * @author liubing
 *
 */
public class LBRedisClient extends AbstractPoolClient {

	private Logger logger = LoggerFactory.getLogger(LBRedisClient.class);
	
	private LBRedisProxyPoolConfig lbRedisProxyPoolConfig;

	/**
	 * @param lbRedisProxyPoolConfig
	 */
	public LBRedisClient(LBRedisProxyPoolConfig lbRedisProxyPoolConfig) {
		 super(lbRedisProxyPoolConfig);		 
		 this.lbRedisProxyPoolConfig=lbRedisProxyPoolConfig;
		 super.initPool();//初始化连接池
	}
	
	/**
	 * 关闭
	 */
	@Override
	public synchronized void close() {
		 try {	
	        	super.pool.shutDown();//连接池关闭,所有都关闭
	        } catch (Exception e) {
	        	logger.error("NettyClient close Error,HOST:"+lbRedisProxyPoolConfig.getHost()+",PORT:"+lbRedisProxyPoolConfig.getPort(), e);
	        }
	}

	/**
	 * 创建对象
	 */
	@Override
	protected LBRedisProxyPooledObjectFactory<IConnection> createChannelFactory() {
		return new LBRedisConnectionFactory(super.ffanRedisProxyPoolConfig);
	}
    
	@Override
	public void write(RedisCommand request, ChannelHandlerContext frontCtx) {
		IConnection connection=null;
		LBRedisProxyPoolEntry<IConnection> entry=null;
		try{
			entry  = borrowObject();
        	if(entry==null||entry.getObject()==null){
        		logger.error("NettyClient borrowObject null");
                return ;
        	}
        	connection=entry.getObject();
            connection.write(request,frontCtx);
            
		}catch(Exception e){
			logger.error("NettyClient write request Error :" , e);
		}finally{
			returnObject(entry);
		}
		
	}
   
   
}
