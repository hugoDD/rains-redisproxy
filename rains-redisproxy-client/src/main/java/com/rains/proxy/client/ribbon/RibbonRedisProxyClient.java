package com.rains.proxy.client.ribbon;

import com.netflix.client.DefaultLoadBalancerRetryHandler;
import com.netflix.client.RetryHandler;
import com.netflix.loadbalancer.BaseLoadBalancer;
import com.netflix.loadbalancer.ILoadBalancer;
import com.netflix.loadbalancer.LoadBalancerStats;
import com.netflix.loadbalancer.Server;
import com.netflix.loadbalancer.reactive.LoadBalancerCommand;
import com.netflix.loadbalancer.reactive.ServerOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;
import redis.clients.util.Pool;
import rx.Observable;

import java.util.List;
import java.util.Map;

public class RibbonRedisProxyClient  {
    public static Logger logger = LoggerFactory.getLogger(RibbonRedisProxyClient.class);
    private ILoadBalancer loadBalancer;

    protected RetryHandler retryHandler = new DefaultLoadBalancerRetryHandler(0, 1, true);

    /**    
     *   
     * @author dourx 
     * @date    
     * @param   
     * @return   
     */ 
    public RibbonRedisProxyClient(ILoadBalancer loadBalancer) {
        this.loadBalancer = loadBalancer;
    }

    public Jedis callWithRibbon(final Map<String,Pool<Jedis>> poolMap) {


        return LoadBalancerCommand.<Jedis>builder()
                .withLoadBalancer(loadBalancer)
                .withRetryHandler(retryHandler)
                .build()
                .submit(new ServerOperation<Jedis>() {

                    @Override
                    public Observable<Jedis> call(Server server) {
                        try {
                            if(logger.isDebugEnabled()){
                                logger.debug("call :{}",server.getId());
                            }
                           Jedis jedis = poolMap.get(server.getId()).getResource();
                            return Observable.just(jedis);
                        } catch (Exception e) {
                            return Observable.error(e);
                        }
                    }
                }).toBlocking().first();
    }

    public List<Server> getAllServers() {
        return ((BaseLoadBalancer) loadBalancer).getAllServers();
    }

    public LoadBalancerStats getLoadBalancerStats() {
        return ((BaseLoadBalancer) loadBalancer).getLoadBalancerStats();
    }
}
