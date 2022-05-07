package com.rains.proxy.core.cluster.impl;

import com.google.common.collect.Lists;
import com.google.common.hash.Hashing;
import com.rains.proxy.core.algorithm.impl.GuavaConsistentHash;
import org.junit.Test;

import java.nio.charset.Charset;
import java.util.List;

public class GuavaHashTest {

    @Test
    public void testConsistentHash() {
        List<String> servers = Lists.newArrayList("server1", "server2", "server3", "server4", "server5");

        int bucket = Hashing.consistentHash(Hashing.md5().hashString("someId", Charset.defaultCharset()), servers.size());
        System.out.println("First time routed to: " + servers.get(bucket));

        // one of the back end servers is removed from the (middle of the) pool
        servers.remove(1);

        int bucket1 = Hashing.consistentHash(Hashing.md5().hashString("someId", Charset.defaultCharset()), servers.size());
        System.out.println("First time routed to: " + servers.get(bucket1));

        bucket = Hashing.consistentHash(Hashing.md5().hashString("blah",Charset.defaultCharset()), servers.size());
        System.out.println("Second time routed to: " + servers.get(bucket));
    }

    @Test
    public void testGuava(){
        List<String> servers = Lists.newArrayList("server1", "server2", "server3", "server4", "server5");
        GuavaConsistentHash<String> hash = new GuavaConsistentHash<>(servers);
        hash.printNodes();
        System.out.println("First time routed to: " +  hash.getNodeByKey("someId"));
        System.out.println("Second time routed to: " +  hash.getNodeByKey("blah"));


        hash.removeNode(node->"server3".equals(node));
        hash.printNodes();
        System.out.println("First1 time routed to: " +  hash.getNodeByKey("someId"));
        System.out.println("Second2 time routed to: " +  hash.getNodeByKey("blah"));

        hash.addNode("server3");
        hash.printNodes();
        System.out.println("First1 time routed to: " +  hash.getNodeByKey("someId"));
        System.out.println("Second2 time routed to: " +  hash.getNodeByKey("blah"));


    }
}
