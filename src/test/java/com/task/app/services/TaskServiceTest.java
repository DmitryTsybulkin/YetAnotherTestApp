package com.task.app.services;

import com.task.app.Stubs;
import com.task.app.dtos.TaskRequestDTO;
import com.task.app.dtos.TaskResponseDTO;
import com.task.app.entities.Task;
import com.task.app.exceptions.NotFoundException;
import com.task.app.repositories.TaskRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.stubbing.Answer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@TestPropertySource(properties = "spring.profiles.active=use_db_storage")
@ContextConfiguration(classes = { TaskServiceImpl.class })
class TaskServiceTest {

    @Autowired
    private TaskService dbTaskService;
    @MockBean
    private TaskRepository taskRepository;

    @Test
    void create() {
        when(taskRepository.save(any())).thenAnswer((Answer<Task>) invocation -> {
            Object[] args = invocation.getArguments();
            return (Task) args[0];
        });
        TaskRequestDTO dto = TaskRequestDTO.builder()
                .name("name")
                .description("description")
                .build();
        TaskResponseDTO taskResponseDTO = dbTaskService.create(dto);
        assertNotNull(taskResponseDTO);
        assertNotNull(taskResponseDTO.getName());
        assertNotNull(taskResponseDTO.getDescription());
        assertNotNull(taskResponseDTO.getModificationDate());
        assertEquals(dto.getName(), taskResponseDTO.getName());
        assertEquals(dto.getDescription(), taskResponseDTO.getDescription());
        assertTrue(LocalDate.now().isEqual(taskResponseDTO.getModificationDate()));
    }

    @Test
    void getById() {
        Task task = Stubs.stubTask(null);
        when(taskRepository.findById(any())).thenReturn(Optional.of(task));
        TaskResponseDTO byId = dbTaskService.getById(1L);
        assertNotNull(byId);
        assertNotNull(byId.getName());
        assertNotNull(byId.getDescription());
        assertNotNull(byId.getModificationDate());
        assertEquals(task.getName(), byId.getName());
        assertEquals(task.getDescription(), byId.getDescription());
        assertTrue(task.getModificationDate().isEqual(byId.getModificationDate()));
    }

    @Test
    void getByIdFailedNotFound() {
        when(taskRepository.findById(any())).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> dbTaskService.getById(1L), "Task by id: 1 not found");
    }

    @Test
    void getAll() {
        Task task1 = Stubs.stubTask(null);
        Task task2 = Stubs.stubTask(2L);
        Task task3 = Stubs.stubTask(3L);
        when(taskRepository.findAll(any(Pageable.class)))
                .thenReturn(new PageImpl<>(Arrays.asList(task1, task2, task3)));
        Page<TaskResponseDTO> all = dbTaskService.getAll(Pageable.unpaged());
        assertNotNull(all);
        assertEquals(3, all.getTotalElements());
    }

    @Test
    void update() {
        Long id = 1L;
        Task target = Stubs.stubTask(id);
        TaskRequestDTO source = TaskRequestDTO.builder().name("new").description("another").build();
        when(taskRepository.findById(any())).thenReturn(Optional.of(target));
        TaskResponseDTO updated = dbTaskService.update(id, source);
        assertNotNull(updated);
        assertNotNull(updated.getName());
        assertNotNull(updated.getDescription());
        assertNotNull(updated.getModificationDate());
        assertEquals(source.getName(), updated.getName());
        assertEquals(source.getDescription(), updated.getDescription());
        assertTrue(LocalDate.now().isEqual(updated.getModificationDate()));
    }

    @Test
    void deleteById() {
        Long id = 1L;
        when(taskRepository.findById(any())).thenReturn(Optional.of(Stubs.stubTask(id)));
        doNothing().when(taskRepository).delete(any(Task.class));
        dbTaskService.deleteById(id);
        verify(taskRepository, atLeastOnce()).delete(any(Task.class));
    }
}