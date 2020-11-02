package com.task.app.services;

import com.task.app.dtos.TaskRequestDTO;
import com.task.app.dtos.TaskResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TaskService {
    TaskResponseDTO create(TaskRequestDTO dto);
    TaskResponseDTO getById(Long id);
    Page<TaskResponseDTO> getAll(Pageable pageable);
    TaskResponseDTO update(Long id, TaskRequestDTO dto);
    void deleteById(Long id);
}
