package com.fabio.featuretoggleapi;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import dev.openfeature.sdk.Client;

@Component
@RequiredArgsConstructor
public class FeatureToggleProvider {

    private final Client client;

    public boolean isEnabled(String feature) {
        return client.getBooleanValue(feature, false);
    }

    public boolean isEnabled(FeatureToggleKeys feature) {
        return client.getBooleanValue(feature.getKey(), false);
    }
}
