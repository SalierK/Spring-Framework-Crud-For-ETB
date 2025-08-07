package com.codecraft.Crud_app.controller;

import com.codecraft.Crud_app.model.Task;
import com.codecraft.Crud_app.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.validation.annotation.Validated;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import java.util.List;

@RestController
@RequestMapping("/api/tasks")
@RequiredArgsConstructor
@Validated
public class TaskController {

    private final TaskService taskService;

    @GetMapping
    @PreAuthorize("hasRole('PERSON') or hasRole('ADMIN')")
    public List<Task> getAllTask() {
        return taskService.getAllTasks();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('PERSON') or hasRole('ADMIN')")
    public ResponseEntity<Task> getTaskById(@PathVariable @Positive(message = "ID must be positive") Long id) {
        return taskService.getTaskById(id)
                .map(task -> new ResponseEntity<>(task, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Task> createTask(@Valid @RequestBody Task task) {
        // Ensure ID is null for new tasks
        task.setId(null);
        Task savedTask = taskService.saveTask(task);
        return new ResponseEntity<>(savedTask, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Task> updateTask(@PathVariable @Positive(message = "ID must be positive") Long id, 
                                         @Valid @RequestBody Task taskDetails) {
       return taskService.getTaskById(id)
                .map(task -> {
                    task.setTitle(taskDetails.getTitle());
                    task.setDescription(taskDetails.getDescription());
                    task.setStatus(taskDetails.getStatus());
                    task.setAsigneedTo(taskDetails.getAsigneedTo());

                    return new ResponseEntity<>(taskService.saveTask(task), HttpStatus.OK);
                }).orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<HttpStatus> deleteTaskById(@PathVariable @Positive(message = "ID must be positive") Long id) {
        if (taskService.getTaskById(id).isPresent()) {
            taskService.deleteTaskById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}