package com.rains.proxy.bolt.util;

import com.rains.proxy.bolt.domain.RedisProxyConfiguration;
import junit.framework.TestCase;
import org.junit.Test;
import org.yaml.snakeyaml.Yaml;

import java.io.InputStream;
import java.util.Map;

public class YamlUtilTest{

    @Test
    public void testLoadYaml() {
        InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream("redisproxy.yaml");
        Yaml yaml = new Yaml();
        Map<String,Object> map = (Map<String, Object>) yaml.load(inputStream);
        Map<String,Object> redisMasters = (Map<String, Object>) map.get("nodes");
       // RedisProxyConfiguration redisProxyConfiguration = yaml.loadAs(inputStream, RedisProxyConfiguration.class);
        System.out.println(yaml.load(inputStream));
    }
}
