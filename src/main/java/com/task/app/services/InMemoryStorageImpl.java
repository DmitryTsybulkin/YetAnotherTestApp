package com.task.app.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

/**
 * Made in the likeness of:
 * @see org.apache.tomcat.util.collections.ConcurrentCache
 * @author tsydmitry
 *
 * todo: generalize key
 */
public class InMemoryStorageImpl<V> implements Storage<Long, V> {

    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final Map<Long, V> eden;
    private final Map<Long, V> longterm;
    private final int size;
    private final AtomicLong lastSavedId = new AtomicLong(0L);

    public InMemoryStorageImpl(int size) {
        this.size = size;
        this.eden = new ConcurrentHashMap<>(size);
        this.longterm = new WeakHashMap<>(size);
    }

    @Override
    public Optional<Map.Entry<Long, V>> get(Long key) {
        try {
            verify(key);
            Optional<V> result = Optional.ofNullable(this.eden.get(key));
            if (!result.isPresent()) {
                synchronized (longterm) {
                    result = Optional.ofNullable(this.longterm.get(key));
                }
            }
            if (result.isPresent()) {
                return Optional.of(new AbstractMap.SimpleEntry<>(key, result.get()));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    /**
     * @param key - can be null
     * @param value
     * @return
     */
    @Override
    public Optional<Map.Entry<Long, V>> put(Long key, V value) {
        try {
            verify(value);
            if (Objects.isNull(key)) {
                key = this.lastSavedId.get();
            }
            if (this.eden.size() >= size) {
                synchronized (longterm) {
                    this.longterm.putAll(this.eden);
                }
                this.logger.debug("Eden is full, cleaning...");
                this.eden.clear();
            }
            this.eden.put(key, value);
            this.lastSavedId.incrementAndGet();
            return Optional.of(new AbstractMap.SimpleEntry<>(key, value));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public List<Map.Entry<Long, V>> getAll() {
        try {
            List<Map.Entry<Long, V>> entries = Collections.synchronizedList(new ArrayList<>());
            // todo refactor
            synchronized (longterm) {
                entries.addAll(this.longterm.entrySet().stream()
                        .map(entry -> new AbstractMap.SimpleEntry<>(entry.getKey(), entry.getValue()))
                        .collect(Collectors.toList()));
            }
            entries.addAll(this.eden.entrySet().stream()
                    .map(entry -> new AbstractMap.SimpleEntry<>(entry.getKey(), entry.getValue()))
                    .collect(Collectors.toList()));
            return entries;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Collections.emptyList();
    }

    @Override
    public Optional<Boolean> isExist(Long key) {
        try {
            verify(key);
            if (!this.eden.containsKey(key)) {
                synchronized (longterm) {
                    return Optional.of(this.longterm.containsKey(key));
                }
            } else {
                return Optional.of(true);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public Optional<Boolean> remove(Long key) {
        try {
            verify(key);
            V removed = this.eden.remove(key);
            if (Objects.isNull(removed)) {
                synchronized (longterm) {
                    removed = this.longterm.remove(key);
                }
                return Optional.of(!Objects.isNull(removed));
            } else {
                return Optional.of(true);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public void verify(Object... objects) throws NullPointerException {
        for (int i = 0; i < objects.length; i++) {
            if (Objects.isNull(objects[i])) {
                throw new NullPointerException(
                        String.format("Provided parameter by index: %d must not be null", i)
                );
            }
        }
    }

    @Override
    public Integer getSize() {
        return size;
    }

    @Override
    public Long getLastSavedId() {
        return lastSavedId.get();
    }
}
