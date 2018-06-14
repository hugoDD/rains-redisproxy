package jmh;


import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import redis.clients.jedis.JedisPoolConfig;

import java.util.concurrent.TimeUnit;


/**
 * @author dourx
 * 2018年 06 月  05日  13:47
 * @version V1.0
 * TODO
 */
@Warmup
@Measurement
@OutputTimeUnit(TimeUnit.SECONDS)
@BenchmarkMode({Mode.Throughput})
//@Threads(100)
@State(Scope.Benchmark)
public class ProxyClientJhmTest {

    private StringRedisTemplate setTemplate;

    private StringRedisTemplate getTemplate;
    private StringRedisTemplate hsetTemplate;
    private StringRedisTemplate hgetTemplate;
    private StringRedisTemplate lpushTemplate;
    private StringRedisTemplate lpopTemplate;

    String key = "spring.boot.redis.test";
    String hkey = "spring.boot.redis.hash.test";
    String lkey = "spring.boot.redis.list.test";


    @Setup
    public void prepare() {
//        spring.redis.host=172.26.223.26
//        spring.redis.port=6379
//        spring.redis.password=
//         spring.redis.pool.maxActive=100
//        spring.redis.pool.maxWait=-1
//        spring.redis.pool.maxIdle=50
//        spring.redis.pool.minIdle=20
//        spring.redis.timeout=3000
        JedisPoolConfig  poolConfig = new JedisPoolConfig();
        poolConfig.setMaxIdle(200);
        poolConfig.setMaxTotal(200);
        poolConfig.setMinIdle(200);
        poolConfig.setMaxWaitMillis(-1);

        RedisConnectionFactory connectionFactory = new JedisConnectionFactory(poolConfig);
        ((JedisConnectionFactory) connectionFactory).setHostName("172.26.223.26");
        ((JedisConnectionFactory) connectionFactory).setTimeout(3000);
        ((JedisConnectionFactory) connectionFactory).setPort(6379);

        ((JedisConnectionFactory) connectionFactory).afterPropertiesSet();
        setTemplate = new StringRedisTemplate(connectionFactory);
        getTemplate = new StringRedisTemplate(connectionFactory);
        hgetTemplate = new StringRedisTemplate(connectionFactory);
        hsetTemplate = new StringRedisTemplate(connectionFactory);
        lpopTemplate = new StringRedisTemplate(connectionFactory);
        lpushTemplate = new StringRedisTemplate(connectionFactory);


//        JedisPoolConfig  poolConfig1 = new JedisPoolConfig();
//        poolConfig1.setMaxIdle(150);
//        poolConfig1.setMaxTotal(300);
//        poolConfig1.setMinIdle(120);
//        poolConfig1.setMaxWaitMillis(-1);
//
//        RedisConnectionFactory connectionFactory1 = new JedisConnectionFactory(poolConfig1);
//        ((JedisConnectionFactory) connectionFactory1).setHostName("172.26.223.109");
//        ((JedisConnectionFactory) connectionFactory1).setTimeout(30000);
//        ((JedisConnectionFactory) connectionFactory1).setPort(6379);
//        ((JedisConnectionFactory) connectionFactory1).afterPropertiesSet();
//        template1 = new StringRedisTemplate(connectionFactory1);
    }





    @Benchmark
    public void proxySetCmd(){
        ValueOperations<String, String> ops = this.setTemplate.opsForValue();
        ops.set(key, "foo");
        // logger.debug("Found key {}, value={}", key , ops.get(key));
    }

//    @Benchmark
//    @BenchmarkMode({Mode.Throughput})
//    public void redisSetCmd(){
//        ValueOperations<String, String> ops = this.template1.opsForValue();
//        ops.set(key, "foo");
//       // logger.debug("Found key {}, value={}", key , ops.get(key));
//    }

    @Benchmark
    public void proxyGetCmd(){
        ValueOperations<String, String> ops = this.getTemplate.opsForValue();
        ops.get(key);
        // logger.debug("Found key {}, value={}", key , ops.get(key));
    }

//    @Benchmark
//    @BenchmarkMode({Mode.Throughput})
//    public void redisGetCmd() {
//        ValueOperations<String, String> ops = this.template1.opsForValue();
//        ops.get(key);
//    }


    //hash
    @Benchmark
    public void proxyHsetCmd(){
        HashOperations<String, String,String> ops = this.hsetTemplate.opsForHash();
        ops.put(hkey,"hashfiledkey","hashvalue");
        // logger.debug("Found key {}, value={}", key , ops.get(key));
    }
//    @Benchmark
//    @BenchmarkMode({Mode.Throughput})
//    public void redisHsetCmd(){
//        HashOperations<String, String,String> ops = this.template1.opsForHash();
//        ops.put(hkey,"hashfiledkey","hashvalue");
//        // logger.debug("Found key {}, value={}", key , ops.get(key));
//    }

    @Benchmark
    public void proxyHgetCmd(){
        HashOperations<String, String,String> ops = this.hgetTemplate.opsForHash();
        ops.get(hkey,"hashfiledkey");
        // logger.debug("Found key {}, value={}", key , ops.get(key));
    }

//    @Benchmark
//    @BenchmarkMode({Mode.Throughput})
//    public void redisHgetCmd(){
//        HashOperations<String, String,String> ops = this.template1.opsForHash();
//        ops.get(hkey,"hashfiledkey");
//        // logger.debug("Found key {}, value={}", key , ops.get(key));
//    }

    //list
    @Benchmark
    public void proxyListPushCmd(){
        ListOperations<String, String> ops = this.lpushTemplate.opsForList();
        ops.leftPush(lkey,"listValue");
        // logger.debug("Found key {}, value={}", key , ops.get(key));
    }
//    @Benchmark
//    @BenchmarkMode({Mode.Throughput})
//    public void redisListPushCmd(){
//        ListOperations<String, String> ops = this.template1.opsForList();
//        ops.leftPush(lkey,"listValue");
//        // logger.debug("Found key {}, value={}", key , ops.get(key));
//    }

    @Benchmark
    public void proxyListPopCmd(){
        ListOperations<String, String> ops = this.lpopTemplate.opsForList();
        ops.leftPop(lkey);
        // logger.debug("Found key {}, value={}", key , ops.get(key));
    }
//    @Benchmark
//    @BenchmarkMode({Mode.Throughput})
//    public void redisListPopCmd(){
//        ListOperations<String, String> ops = this.template1.opsForList();
//        ops.leftPop(lkey);
//        // logger.debug("Found key {}, value={}", key , ops.get(key));
//    }


    //
    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder()
                .include(ProxyClientJhmTest.class.getSimpleName())
                .forks(1)
                .build();

        new Runner(opt).run();
    }
}
