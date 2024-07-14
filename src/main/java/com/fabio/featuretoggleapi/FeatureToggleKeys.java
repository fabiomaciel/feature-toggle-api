package com.fabio.featuretoggleapi;


public enum FeatureToggleKeys {
    MESSAGE;

    public String getKey() {
        return this.name().toLowerCase();
    }
}
