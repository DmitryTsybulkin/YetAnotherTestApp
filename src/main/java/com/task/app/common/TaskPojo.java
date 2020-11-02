package com.task.app.common;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TaskPojo {
    private String name;
    private String description;
    private LocalDate modificationDate;
}