package com.fabio.featuretoggleapi;

import io.lettuce.core.api.sync.RedisCommands;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/redis")
@RequiredArgsConstructor
public class TestController {

    private final FeatureToggleProvider featureToggleProvider;

    @GetMapping
    public String test() {
        if(featureToggleProvider.isEnabled(FeatureToggleKeys.MESSAGE)) {
            return "Feature is enabled";
        }
        return "Feature is disabled";
    }

    @PostConstruct
    public void init() {
        System.out.println("FeatureToggleProvider is enabled");
    }
}
