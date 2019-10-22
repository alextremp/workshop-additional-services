package com.github.alextremp.additionalservices.application.mapper;

import java.util.HashMap;
import java.util.Objects;

public class FluentMap<V> extends HashMap<String, V> {

    public V getNotNull(Object key) {
        V v = super.get(key);
        Objects.requireNonNull(v, "No value for: " + key);
        return v;
    }

    public FluentMap<V> fluentPut(String key, V value) {
        super.put(key, value);
        return this;
    }
}
