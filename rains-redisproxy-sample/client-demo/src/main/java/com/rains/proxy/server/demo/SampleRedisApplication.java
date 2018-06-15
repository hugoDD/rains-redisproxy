/*
 * Copyright 2012-2015 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.rains.proxy.server.demo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.system.ApplicationPidFileWriter;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

@SpringBootApplication
@EnableAutoConfiguration
//@EnableEurekaClient
//@EnableFeignClients
public class SampleRedisApplication implements CommandLineRunner {
private static final Logger logger = LoggerFactory.getLogger(SampleRedisApplication.class);
	@Autowired
	private StringRedisTemplate template;

	Executor executor = Executors.newFixedThreadPool(300);

	@Override
	public void run(String... args) throws Exception {
		ValueOperations<String, String> ops = this.template.opsForValue();
		String key = "spring.boot.redis.test";

			executor.execute(()->{
				while (true){
					ops.set(key, "foo");
					logger.debug("Found key {}, value={}", key , ops.get(key));
				}

        });
//		if (!this.template.hasKey(key)) {
//			ops.set(key, "foo");
//		}
		//System.out.println("Found key " + key + ", value=" + ops.get(key));
	}

	public static void main(String[] args) throws Exception {
		// Close the context so it doesn't stay awake listening for redis
        ConfigurableApplicationContext commonContext =
                new SpringApplicationBuilder(SampleRedisApplication.class).web(false).run(args);
        commonContext.addApplicationListener(new ApplicationPidFileWriter());

    }

}
