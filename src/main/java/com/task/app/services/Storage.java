package com.task.app.services;


import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface Storage<K, V> {
    Optional<Map.Entry<K, V>> get(K key);
    Optional<Map.Entry<K, V>> put(K key, V value);
    List<Map.Entry<K, V>> getAll();
    Optional<Boolean> isExist(K key);
    Optional<Boolean> remove(K key);
    void verify(Object... objects);
    Integer getSize();
    Long getLastSavedId();
}
