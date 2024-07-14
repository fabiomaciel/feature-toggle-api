package com.fabio.featuretoggleapi;

import io.lettuce.core.RedisClient;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GracefulShutdownHook implements ApplicationListener<ContextClosedEvent> {

    private final RedisClient redisClient;

    @Override
    public void onApplicationEvent(ContextClosedEvent event) {
        System.out.println("Performing cleanup tasks before shutdown");
        redisClient.shutdown();
    }
}