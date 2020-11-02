package com.task.app.services;

import com.task.app.dtos.TaskRequestDTO;
import com.task.app.dtos.TaskResponseDTO;
import com.task.app.common.TaskPojo;
import com.task.app.exceptions.InternalServerError;
import com.task.app.exceptions.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Profile("use_mem_storage")
@Service
public class InMemoryTaskServiceImpl implements TaskService {

    @Value("${storage.memory.size}")
    private Integer storageSize;
    private final Storage<Long, TaskPojo> storage;

    @Autowired
    public InMemoryTaskServiceImpl() {
        this.storage = new InMemoryStorageImpl<>(
                Objects.nonNull(this.storageSize) ? this.storageSize : 1000
        );
    }

    @Override
    public TaskResponseDTO create(TaskRequestDTO dto) {
        TaskPojo taskPojo = TaskPojo.builder()
                .name(dto.getName())
                .description(dto.getDescription())
                .modificationDate(LocalDate.now())
                .build();
        Map.Entry<Long, TaskPojo> saved = this.storage.put(null, taskPojo)
                .orElseThrow(() -> new InternalServerError("Something went wrong..."));
        return TaskResponseDTO.builder()
                .id(saved.getKey())
                .name(saved.getValue().getName())
                .description(saved.getValue().getDescription())
                .modificationDate(saved.getValue().getModificationDate())
                .build();
    }

    @Override
    public TaskResponseDTO getById(Long id) {
        Map.Entry<Long, TaskPojo> entry = this.storage.get(id)
                .orElseThrow(() -> new NotFoundException(String.format("Task by id: %s didn't found", id)));
        return TaskResponseDTO.builder()
                .id(entry.getKey())
                .name(entry.getValue().getName())
                .description(entry.getValue().getDescription())
                .modificationDate(entry.getValue().getModificationDate())
                .build();
    }

    @Override
    public Page<TaskResponseDTO> getAll(Pageable pageable) {
        List<TaskResponseDTO> list = this.storage.getAll().stream()
                .map(entry -> TaskResponseDTO.builder()
                        .id(entry.getKey())
                        .name(entry.getValue().getName())
                        .description(entry.getValue().getDescription())
                        .modificationDate(entry.getValue().getModificationDate())
                        .build())
                .sorted(Comparator.comparing(TaskResponseDTO::getModificationDate))
                .collect(Collectors.toList());
        return new PageImpl<>(list, pageable, list.size());
    }

    @Override
    public TaskResponseDTO update(Long id, TaskRequestDTO dto) {
        if (!this.storage.isExist(id)
                .orElseThrow(() -> new InternalServerError("Something went wrong..."))) {
            throw new NotFoundException(String.format("Task by id: %d doesn't exist", id));
        }
        Map.Entry<Long, TaskPojo> saved = this.storage.put(id, TaskPojo.builder()
                .name(dto.getName())
                .description(dto.getDescription())
                .modificationDate(LocalDate.now())
                .build())
                .orElseThrow(() -> new InternalServerError("Something went wrong..."));
        return TaskResponseDTO.builder()
                .id(saved.getKey())
                .name(saved.getValue().getName())
                .modificationDate(saved.getValue().getModificationDate())
                .description(saved.getValue().getDescription()).build();
    }

    @Override
    public void deleteById(Long id) {
        if (!this.storage.isExist(id).orElseThrow(() -> new InternalServerError("Something went wrong..."))) {
            throw new NotFoundException(String.format("Task by id: %d doesn't exist", id));
        }
        this.storage.remove(id).orElseThrow(() -> new InternalServerError("Something went wrong..."));
    }
}
