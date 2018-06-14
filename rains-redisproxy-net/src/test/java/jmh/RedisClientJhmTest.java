package jmh;

import com.rains.proxy.core.config.RedisProxyPoolConfig;
import com.rains.proxy.core.pool.IPoolEntry;
import com.rains.proxy.core.pool.impl.PoolBasicIdleEntriesQueue;
import com.rains.proxy.core.pool.impl.PoolEntry;
import com.rains.proxy.net.client.RedisProxyClient;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.concurrent.TimeUnit;


/**
 * @author dourx
 * 2018年 06 月  05日  13:47
 * @version V1.0
 * TODO
 */
@Warmup
@Measurement
@OutputTimeUnit(TimeUnit.MICROSECONDS)
@State(Scope.Benchmark)
public class RedisClientJhmTest {
    public RedisProxyClient redisProxyClient;


    @Setup
    public void prepare() {
        RedisProxyPoolConfig poolConfig = new RedisProxyPoolConfig();

       // redisProxyClient = new RedisProxyClient();
    }




//    @Benchmark
//    @BenchmarkMode(Mode.Throughput)
//    public void jhmQueue(){
//        MockConnection connection = new MockConnection();
//        PoolEntry<MockConnection> poolEntry = new PoolEntry<>(connection);
//        queue.offer(poolEntry);
//        IPoolEntry pollIPoolEntry = queue.poll();
//    }
//
//    public static void main(String[] args) throws RunnerException {
//        Options opt = new OptionsBuilder()
//                .include(RedisProxyPoolQueueJhmTest.class.getSimpleName())
//                .forks(1)
//                .build();
//
//        new Runner(opt).run();
//    }
}
