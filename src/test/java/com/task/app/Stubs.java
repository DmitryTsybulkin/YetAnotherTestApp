package com.task.app;

import com.task.app.entities.Task;

import java.time.LocalDate;
import java.util.Objects;

public class Stubs {

    public static Task stubTask(Long id) {
        return Task.builder()
                .id(Objects.isNull(id) ? 1L : id)
                .modificationDate(LocalDate.now())
                .name("name")
                .description("description")
                .build();
    }

}
