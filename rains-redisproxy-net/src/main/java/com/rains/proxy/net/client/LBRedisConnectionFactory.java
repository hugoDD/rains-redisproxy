/**
 * 
 */
package com.rains.proxy.net.client;


import com.rains.proxy.core.connection.IConnection;
import com.rains.proxy.core.pool.LBRedisProxyPooledObjectFactory;
import com.rains.proxy.core.pool.commons.LBRedisProxyPoolConfig;
import com.rains.proxy.core.pool.exception.LBRedisProxyPoolException;

/**
 * @author liubing
 *
 */
public class LBRedisConnectionFactory implements LBRedisProxyPooledObjectFactory<IConnection> {
	
	private LBRedisProxyPoolConfig ffanRedisProxyPoolConfig;
	
	/**
	 * 
	 * @param ffanRedisProxyPoolConfig
	 */
	public LBRedisConnectionFactory(LBRedisProxyPoolConfig ffanRedisProxyPoolConfig) {
		super();
		this.ffanRedisProxyPoolConfig = ffanRedisProxyPoolConfig;
	}

	@Override
	public IConnection createInstance() throws LBRedisProxyPoolException {
		LBRedisConnection ffanRedisConnection=new LBRedisConnection(ffanRedisProxyPoolConfig);
		ffanRedisConnection.open();
		return ffanRedisConnection;
	}



	@Override
	public Boolean validateEntry(IConnection connection)
			throws LBRedisProxyPoolException {
		return connection.isAvailable();
	}



	@Override
	public void destroyEntry(IConnection connection) throws LBRedisProxyPoolException {
		connection.close();
	}
	
	
	
}
