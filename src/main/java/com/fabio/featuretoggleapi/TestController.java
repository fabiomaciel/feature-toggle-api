package com.fabio.featuretoggleapi;

import io.lettuce.core.api.sync.RedisCommands;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/redis")
@RequiredArgsConstructor
public class TestController {

    private final RedisCommands<String, String> redisCommands;

    @GetMapping
    public String test(@RequestParam String key) {
        return redisCommands.get(key);
    }

    @PostConstruct
    public void init() {
        redisCommands.set("x", "value");
    }
}
