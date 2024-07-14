package com.fabio.featuretoggleapi;

import dev.openfeature.sdk.Client;
import dev.openfeature.sdk.OpenFeatureAPI;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenFeatureConfig {

    @Bean
    public Client x(RedisProvider redisProvider) {
        OpenFeatureAPI api = OpenFeatureAPI.getInstance();
        api.setProviderAndWait(redisProvider);
        return api.getClient();
    }
}
