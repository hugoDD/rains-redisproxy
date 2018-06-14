package jmh;

import com.rains.proxy.core.command.impl.RedisCommand;
import com.rains.proxy.core.enums.RedisCmdEnums;
import com.rains.proxy.net.client.RedisConnection;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.ArrayList;
import java.util.List;
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
@State(Scope.Benchmark)
public class RedisConnectionJhmTest {


    RedisConnection connection;
    RedisCommand pingCmd;
    RedisCommand setCmd;
    RedisCommand getCmd;
    @Setup
    public void prepare() {


        connection = new RedisConnection("172.26.223.109",16379,3000);
       // redisProxyClient = new RedisProxyClient();
         pingCmd=  getRedisCommand("ping");
         setCmd=  getRedisCommand("set mykey myvalue");
         getCmd=  getRedisCommand("get mykey");

    }

    @TearDown
    public void tearDown() throws Exception {
        connection.close();
    }


    @Benchmark
    @BenchmarkMode({Mode.Throughput,Mode.AverageTime})
    public void oneConn(){

       connection.write(pingCmd,null);
        connection.close();
     //  connection.getBackChannel().re
    }

//    @Benchmark
//    @BenchmarkMode({Mode.Throughput,Mode.AverageTime})
//    public void oneSet(){
//        connection.write(setCmd,null);
//    }

//    @Benchmark
//    @BenchmarkMode({Mode.Throughput,Mode.AverageTime})
//    public void oneGet(){
//
//        connection.write(getCmd,null);
//    }
//
    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder()
                .include(RedisConnectionJhmTest.class.getSimpleName())
                .forks(1)
                .build();

        new Runner(opt).run();
    }



    private RedisCommand getRedisCommand(String command) {
        RedisCommand redisCommand = new RedisCommand();
        redisCommand.setArgCount(3);
        List<byte[]> args = new ArrayList<>();
        String[] subCmd = command.split(" ");
        redisCommand.setArgCount(subCmd.length);
        for(String sub : subCmd){
            args.add(sub.getBytes());

        }
        redisCommand.setArgs(args);
        String firstCmd = new String(args.get(0));

        redisCommand.setPolicy(RedisCmdEnums.getPolicy(firstCmd));
        return redisCommand;
    }
}
