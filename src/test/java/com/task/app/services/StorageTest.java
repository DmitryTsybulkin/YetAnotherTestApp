package com.task.app.services;

import com.task.app.Stubs;
import com.task.app.entities.Task;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class StorageTest {

    @Test
    void test() {
        Storage<Long, Object> storage = new InMemoryStorageImpl<>(2);
        assertNotNull(storage);
        assertNotNull(storage.getSize());
        assertNotNull(storage.getLastSavedId());
        assertEquals(0, storage.getLastSavedId());
        assertEquals(2, storage.getSize());
    }

    @Test
    void get() {
        Task task = Stubs.stubTask(null);
        Storage<Long, Object> storage = new InMemoryStorageImpl<>(1);
        assertEquals(0, storage.getLastSavedId());
        Long key = storage.put(null, task).get().getKey();
        assertEquals(1, storage.getLastSavedId());
        Task value = (Task) storage.get(key).get().getValue();
        assertNotNull(value);
        assertNotNull(value.getId());
        assertNotNull(value.getName());
        assertNotNull(value.getDescription());
        assertNotNull(value.getModificationDate());
        assertEquals(task.getId(), value.getId());
        assertEquals(task.getName(), value.getName());
        assertEquals(task.getDescription(), value.getDescription());
        assertTrue(task.getModificationDate().isEqual(value.getModificationDate()));
    }

    @Test
    void getNotFound() {
        Storage<Long, Object> storage = new InMemoryStorageImpl<>(1);
        assertEquals(0, storage.getLastSavedId());
        Optional<Map.Entry<Long, Object>> entry = storage.get(1L);
        assertFalse(entry.isPresent());
    }

    @Test
    void put() {
        Task task1 = Stubs.stubTask(1L);
        Task task2 = Stubs.stubTask(2L);
        Task task3 = Stubs.stubTask(3L);
        Storage<Long, Task> storage = new InMemoryStorageImpl<>(1);
        Map.Entry<Long, Task> saved1 = storage.put(null, task1).get();
        Map.Entry<Long, Task> saved2 = storage.put(null, task2).get();
        Map.Entry<Long, Task> saved3 = storage.put(null, task3).get();
        Optional<Map.Entry<Long, Task>> target1 = storage.get(saved1.getKey());
        Optional<Map.Entry<Long, Task>> target2 = storage.get(saved2.getKey());
        Optional<Map.Entry<Long, Task>> target3 = storage.get(saved3.getKey());

        assertTrue(target1.isPresent());
        assertNotNull(target1.get().getKey());
        assertNotNull(target1.get().getValue());
        assertNotNull(target1.get().getValue().getId());
        assertNotNull(target1.get().getValue().getName());
        assertNotNull(target1.get().getValue().getDescription());
        assertNotNull(target1.get().getValue().getModificationDate());
        assertEquals(task1.getId(), target1.get().getValue().getId());
        assertEquals(task1.getName(), target1.get().getValue().getName());
        assertEquals(task1.getDescription(), target1.get().getValue().getDescription());
        assertTrue(task1.getModificationDate().isEqual(target1.get().getValue().getModificationDate()));

        assertTrue(target2.isPresent());
        assertNotNull(target2.get().getKey());
        assertNotNull(target2.get().getValue());
        assertNotNull(target2.get().getValue().getId());
        assertNotNull(target2.get().getValue().getName());
        assertNotNull(target2.get().getValue().getDescription());
        assertNotNull(target2.get().getValue().getModificationDate());
        assertEquals(task2.getId(), target2.get().getValue().getId());
        assertEquals(task2.getName(), target2.get().getValue().getName());
        assertEquals(task2.getDescription(), target2.get().getValue().getDescription());
        assertTrue(task2.getModificationDate().isEqual(target2.get().getValue().getModificationDate()));

        assertTrue(target3.isPresent());
        assertNotNull(target3.get().getKey());
        assertNotNull(target3.get().getValue());
        assertNotNull(target3.get().getValue().getId());
        assertNotNull(target3.get().getValue().getName());
        assertNotNull(target3.get().getValue().getDescription());
        assertNotNull(target3.get().getValue().getModificationDate());
        assertEquals(task3.getId(), target3.get().getValue().getId());
        assertEquals(task3.getName(), target3.get().getValue().getName());
        assertEquals(task3.getDescription(), target3.get().getValue().getDescription());
        assertTrue(task3.getModificationDate().isEqual(target3.get().getValue().getModificationDate()));
        assertEquals(3, storage.getAll().size());
    }

    @Test
    void putOversize() {
        Storage<Long, Object> storage = new InMemoryStorageImpl<>(1);
        storage.put(null, new Object()).get();
        storage.put(null, new Object()).get();
        storage.put(null, new Object()).get();
        assertEquals(3, storage.getAll().size());
    }

    @Test
    void getAll() {
        Storage<Long, Object> storage = new InMemoryStorageImpl<>(2);
        storage.put(null, new Object()).get();
        storage.put(null, new Object()).get();
        storage.put(null, new Object()).get();
        assertEquals(3, storage.getAll().size());
    }

    @Test
    void isExist() {
        Task task = Stubs.stubTask(null);
        Storage<Long, Object> storage = new InMemoryStorageImpl<>(1);
        assertEquals(0, storage.getLastSavedId());
        Long key = storage.put(null, task).get().getKey();
        assertEquals(1, storage.getLastSavedId());
        Optional<Boolean> exist = storage.isExist(key);
        assertTrue(exist.isPresent());
        assertTrue(exist.get());
    }

    @Test
    void remove() {
        Task task = Stubs.stubTask(null);
        Storage<Long, Object> storage = new InMemoryStorageImpl<>(1);
        assertEquals(0, storage.getLastSavedId());
        Long key = storage.put(null, task).get().getKey();
        Optional<Boolean> removed = storage.remove(key);
        assertTrue(removed.isPresent());
        assertTrue(removed.get());
        assertFalse(storage.isExist(key).get());
    }

    @Test
    void verify() {
        Storage<Long, Object> storage = new InMemoryStorageImpl<>(1);
        assertThrows(Exception.class, () ->
                storage.verify(null, new Object()), "Provided parameter by index: 0 must not be null");
        assertDoesNotThrow(() -> storage.verify(new Object(), new Object()));
    }
}