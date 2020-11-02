package com.task.app.services;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;


// todo
@Disabled
@ExtendWith(SpringExtension.class)
@TestPropertySource(properties = {"spring.profiles.active=use_mem_storage", "storage.memory.size="})
@ContextConfiguration(classes = { InMemoryTaskServiceImpl.class })
public class InMemoryTaskServiceTest {

    @Autowired
    private TaskService inMemTaskService;
    @MockBean
    private Storage<Long, Object> storage;

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void create() {
    }

    @Test
    void getById() {
    }

    @Test
    void getAll() {
    }

    @Test
    void update() {
    }

    @Test
    void deleteById() {
    }

}
