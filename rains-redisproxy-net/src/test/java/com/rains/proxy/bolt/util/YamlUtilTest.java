package com.rains.proxy.bolt.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.rains.proxy.bolt.domain.yaml.RedisProxy;
import org.junit.Test;
import org.springframework.cglib.beans.BeanMap;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

public class YamlUtilTest{



    @Test
    public void testjacksonYaml() throws IOException {
        ObjectMapper  mapper = new ObjectMapper(new YAMLFactory());
        InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream("redisproxy.yaml");
        RedisProxy redisProxyConfiguration = mapper.readValue(inputStream, RedisProxy.class);
        System.out.println(redisProxyConfiguration);

    }

    /**
     * 将map集合中的数据转化为指定对象的同名属性中
     */
    public static <T> T mapToBean(Map map,T bean)  {
        BeanMap beanMap = BeanMap.create(bean);
        beanMap.putAll(map);
        return bean;
    }
}
