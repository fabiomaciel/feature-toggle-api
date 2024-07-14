package com.fabio.featuretoggleapi;

import io.lettuce.core.RedisClient;
import io.lettuce.core.RedisURI;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.sync.RedisCommands;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RedisConfiguration {

    @Bean
    public RedisClient redisFeatureRepository() {
        RedisURI redisUri = RedisURI.Builder.redis("localhost").withPort(6379).build();
        return RedisClient.create(redisUri);
    }


    @Bean
    public RedisCommands<String, String> redisCommands(RedisClient redisClient) {
        return redisClient.connect().sync();
    }


}
