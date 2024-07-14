package com.fabio.featuretoggleapi;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.openfeature.sdk.*;
import io.lettuce.core.api.sync.RedisCommands;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.function.Function;

@Component
@RequiredArgsConstructor
public class RedisProvider implements FeatureProvider {

    private final RedisCommands<String, String> redisCommands;
    private final ObjectMapper objectMapper;

    private final static String FEATURE_TOGGLE_REDIS_PREFIX = "feature:";

    @Override
    public Metadata getMetadata() {
        return null;
    }

    @Override
    public ProviderEvaluation<Boolean> getBooleanEvaluation(String s, Boolean aBoolean, EvaluationContext evaluationContext) {
        return getFeatureToggle(s, aBoolean, Boolean::parseBoolean);
    }

    @Override
    public ProviderEvaluation<String> getStringEvaluation(String s, String s1, EvaluationContext evaluationContext) {
        return getFeatureToggle(s, s1);
    }

    @Override
    public ProviderEvaluation<Integer> getIntegerEvaluation(String s, Integer integer, EvaluationContext evaluationContext) {
        return getFeatureToggle(s, integer, Integer::parseInt);
    }

    @Override
    public ProviderEvaluation<Double> getDoubleEvaluation(String s, Double aDouble, EvaluationContext evaluationContext) {
        return getFeatureToggle(s, aDouble, Double::parseDouble);
    }

    @Override
    public ProviderEvaluation<Value> getObjectEvaluation(String s, Value value, EvaluationContext evaluationContext) {
        return getFeatureToggle(s, value, (o) -> {
            try {
                return objectMapper.readValue(o, Value.class);
            } catch (Exception e) {
                return null;
            }
        });
    }

    private <T> ProviderEvaluation<T> getFeatureToggle(String key, T defaultValue) {
        return getFeatureToggle(key, defaultValue, (o) -> (T) o);
    }

    private <T> ProviderEvaluation<T> getFeatureToggle(String featureName, T defaultValue, Function<String, T> resultToType) {
        if (featureName == null) {
            throw new IllegalArgumentException("Feature name cannot be null");
        }
        return Optional.of(featureName)
                .map(this::getFeatureToggleRedisKey)
                .map(redisKey -> evaluateValue(redisCommands.get(redisKey), defaultValue, resultToType))
                .orElseThrow(() -> new IllegalArgumentException("Feature not found"));
    }

    private <T> ProviderEvaluation<T> evaluateValue(String value, T defaultValue, Function<String, T> resultToType) {
        return Optional.ofNullable(value)
                .map(v -> ProviderEvaluation.<T>builder().value(resultToType.apply(v)).reason(Reason.TARGETING_MATCH.name()).build())
                .orElse(ProviderEvaluation.<T>builder().value(defaultValue).reason(Reason.ERROR.name()).build());
    }

    private String getFeatureToggleRedisKey(String featureName) {
        return String.format("%s%s", FEATURE_TOGGLE_REDIS_PREFIX, featureName);
    }

}
