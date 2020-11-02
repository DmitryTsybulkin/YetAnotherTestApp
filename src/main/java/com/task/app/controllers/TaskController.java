package com.task.app.controllers;

import com.task.app.dtos.TaskRequestDTO;
import com.task.app.dtos.TaskResponseDTO;
import com.task.app.services.TaskService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
public class TaskController {

    private final TaskService taskService;

    @Autowired
    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @ApiOperation(value = "Get Task by id", produces = MediaType.APPLICATION_JSON_VALUE, httpMethod = "GET")
    @GetMapping("/tasks/{id}")
    public TaskResponseDTO getTaskById(@PathVariable("id") Long id) {
        return this.taskService.getById(id);
    }

    @ApiOperation(value = "Get Tasks", produces = MediaType.APPLICATION_JSON_VALUE, httpMethod = "GET")
    @GetMapping("/tasks")
    public Page<TaskResponseDTO> getAllTasks(@RequestBody Pageable pageable) {
        return this.taskService.getAll(pageable);
    }

    @ApiOperation(value = "Create Task", produces = MediaType.APPLICATION_JSON_VALUE, httpMethod = "POST")
    @PostMapping("/tasks")
    public TaskResponseDTO createTask(@Valid @RequestBody TaskRequestDTO dto) {
        return this.taskService.create(dto);
    }

    @ApiOperation(value = "Update Task by id", produces = MediaType.APPLICATION_JSON_VALUE, httpMethod = "PATCH")
    @PatchMapping("/tasks/{id}")
    public TaskResponseDTO updateTask(@PathVariable Long id, @Valid @RequestBody TaskRequestDTO dto) {
        return this.taskService.update(id, dto);
    }

    @ApiOperation(value = "Delete Task by id", produces = MediaType.APPLICATION_JSON_VALUE, httpMethod = "DELETE")
    @DeleteMapping("/tasks/{id}")
    public void deleteTask(@PathVariable Long id) {
        this.taskService.deleteById(id);
    }
}
