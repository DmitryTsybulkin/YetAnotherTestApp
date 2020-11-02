package com.task.app.dtos;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class TaskResponseDTO {
    private Long id;
    private String name;
    private String description;
    private LocalDate modificationDate;
}
