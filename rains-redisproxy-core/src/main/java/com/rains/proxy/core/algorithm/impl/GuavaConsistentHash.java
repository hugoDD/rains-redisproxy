package com.rains.proxy.core.algorithm.impl;

import com.google.common.hash.HashFunction;
import com.google.common.hash.Hashing;

import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * @author ly-dourx
 */
public class GuavaConsistentHash<T> {
    /**------------------ 一致性哈希算法的java实现 ------------------*/
    private SortedMap<Long,T> ketamaNodes = new TreeMap<>();
    private static final int NUMBER = 1024;
    private int numberOfReplicas = NUMBER;
    private HashFunction hashFunction = Hashing.md5();
    private List<T> nodes;
    /**标志是否初始化完成*/
    private volatile boolean init = false;
    /**有参数构造函数*/
    public GuavaConsistentHash(List<T> nodes){
       this(NUMBER,nodes);
    }

    public GuavaConsistentHash(int numberOfReplicas,List<T> nodes){
        this.numberOfReplicas = numberOfReplicas;
        this.nodes = nodes;
        init();
    }

    /**
     * 根据key的哈希值，找到最近的一个节点（服务器）
     * @param key 哈希值
     * @return 找到最近的一个节点
     */
    public T getNodeByKey(String key){
        if(!init) {
            throw new RuntimeException("init uncomplete...");
        }
        // 注意，这里是NIO包 java.nio.charset.Charset
        byte[] digest = hashFunction.hashString(key, Charset.forName("UTF-8")).asBytes();
        long hash = hash(digest,0);
        //如果找到这个节点，直接取节点，返回
        if(!ketamaNodes.containsKey(hash)){
            //得到大于当前key的那个子Map，然后从中取出第一个key，就是大于且离它最近的那个key
            SortedMap<Long,T> tailMap = ketamaNodes.tailMap(hash);
            if(tailMap.isEmpty()){
                hash = ketamaNodes.firstKey();
            }else{
                hash = tailMap.firstKey();
            }

        }
        return ketamaNodes.get(hash);
    }



    /**
     * 新增节点
     * @param node 节点
     */
    public synchronized void addNode(T node){
        init = false;
        nodes.add(node);
        init();
    }
    /**
     * 新增节点
     */
    public synchronized void removeNode(Predicate<T> consumer){
        init = false;
        nodes.removeIf(consumer);
        ketamaNodes.clear();
        init();
    }

    private void init(){
        //对所有节点，生成numberOfReplicas个虚拟节点
        for(T node : nodes){
            //每四个虚拟节点为1组
            for(int i=0;i<numberOfReplicas/4;i++){
                //为这组虚拟结点得到惟一名称
                byte[] digest = hashFunction.hashString(node.toString()+i, Charset.forName("UTF-8")).asBytes();
                //Md5是一个16字节长度的数组，将16字节的数组每四个字节一组，分别对应一个虚拟结点，这就是为什么上面把虚拟结点四个划分一组的原因
                for(int h=0;h<4;h++){
                    Long k = hash(digest,h);
                    ketamaNodes.put(k,node);
                }
            }
        }
        init = true;
    }

    public void printNodes(){
        Map<String, Long> collect = ketamaNodes.values().stream().collect(Collectors.groupingBy(Object::toString, Collectors.counting()));
        System.out.println(collect);
//        for(Long key:ketamaNodes.keySet()){
//            System.out.println(key+":"+ketamaNodes.get(key));
//        }
    }
    /** 哈希算法*/
    public static long hash(byte[] digest, int nTime)
    {
        long rv = ((long)(digest[3 + nTime * 4] & 0xFF) << 24)
                | ((long)(digest[2 + nTime * 4] & 0xFF) << 16)
                | ((long)(digest[1 + nTime * 4] & 0xFF) << 8)
                | ((long)digest[0 + nTime * 4] & 0xFF);
        return rv;
    }

}
