package com.task.app.services;

import com.task.app.dtos.TaskRequestDTO;
import com.task.app.dtos.TaskResponseDTO;
import com.task.app.entities.Task;
import com.task.app.exceptions.NotFoundException;
import com.task.app.repositories.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Profile("use_db_storage")
@Service
public class TaskServiceImpl implements TaskService {

    private final String NOT_FOUND_MSG = "Task by id: %d not found";
    private final TaskRepository taskRepository;

    @Autowired
    public TaskServiceImpl(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    @Transactional
    @Override
    public TaskResponseDTO create(TaskRequestDTO dto) {
        Task task = Task.builder()
                .name(dto.getName())
                .description(dto.getDescription())
                .modificationDate(LocalDate.now())
                .build();
        Task saved = this.taskRepository.save(task);
        return toDto(saved);
    }

    @Transactional(readOnly = true)
    @Override
    public TaskResponseDTO getById(Long id) {
        Task task = this.taskRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(String.format(NOT_FOUND_MSG, id)));
        return toDto(task);
    }

    @Transactional(readOnly = true)
    @Override
    public Page<TaskResponseDTO> getAll(Pageable pageable) {
        return this.taskRepository.findAll(pageable).map(this::toDto);
    }

    @Transactional
    @Override
    public TaskResponseDTO update(Long id, TaskRequestDTO dto) {
        Task task = this.taskRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(String.format(NOT_FOUND_MSG, id)));
        task.setName(dto.getName());
        task.setDescription(dto.getDescription());
        task.setModificationDate(LocalDate.now());
        return toDto(task);
    }

    @Transactional
    @Override
    public void deleteById(Long id) {
        Task task = this.taskRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(String.format(NOT_FOUND_MSG, id)));
        this.taskRepository.delete(task);
    }

    private TaskResponseDTO toDto(Task task) {
        return TaskResponseDTO.builder()
                .id(task.getId())
                .name(task.getName())
                .description(task.getDescription())
                .modificationDate(task.getModificationDate())
                .build();
    }
}
