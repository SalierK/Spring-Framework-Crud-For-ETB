package com.codecraft.Crud_app;

import com.codecraft.Crud_app.controller.TaskController;
import com.codecraft.Crud_app.model.Task;
import com.codecraft.Crud_app.service.TaskService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.test.context.support.WithMockUser;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class CrudAppApplicationTests {

    @Autowired
    private TaskService taskService;

    @BeforeEach
    public void setUp() {
        Task task1 = new Task();
        task1.setTitle("Task 1");
        task1.setDescription("Description 1");
        task1.setAsigneedTo("user1");
        task1.setStatus(0);
        taskService.saveTask(task1);

        Task task2 = new Task();
        task2.setTitle("Task 2");
        task2.setDescription("Description 2");
        task2.setAsigneedTo("user2");
        task2.setStatus(1);
        taskService.saveTask(task2);
    }

    @AfterEach
    public void tearDown() {
        for (Task task : taskService.getAllTasks()) {
            taskService.deleteTaskById(task.getId());
        }
    }


    @Test
    @Order(1)
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void test_getTaskById() {
        ResponseEntity<Task> idTask = null;
        TaskController taskController = new TaskController(taskService);
        for (Task task : taskService.getAllTasks()) {
            idTask = taskController.getTaskById(task.getId());
            if (idTask.getBody().getId() != null) {
                break;
            }
        }
        assertNotNull(idTask.getBody().getId());
    }

    @Test
    @Order(2)
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void test_getAllTasks() {
        TaskController taskController = new TaskController(taskService);
        List<Task> list = taskController.getAllTask();
        assertNotNull(list.get(0).getId());
    }

    @Test
    @Order(3)
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void test_createTask() {
        Task task = new Task();
        String title = "Test title for creatTask";
        String description = "Test description for creatTask";
        String assignedTo = "test user";
        Integer status = 1; // Changed to valid status range
        task.setTitle(title);
        task.setDescription(description);
        task.setAsigneedTo(assignedTo);
        task.setStatus(status);
        TaskController taskController = new TaskController(taskService);
        ResponseEntity<Task> response = taskController.createTask(task);
        Task newTask = response.getBody();
        assertEquals(title, taskController.getTaskById(newTask.getId()).getBody().getTitle());
        assertEquals(description, taskController.getTaskById(newTask.getId()).getBody().getDescription());
        assertEquals(status, taskController.getTaskById(newTask.getId()).getBody().getStatus());
    }

    @Test
    @Order(4)
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void test_updateTask() {
        Task task = new Task();
        String title = "Test title for updateTask";
        String description = "Test description for updateTask";
        String assignedTo = "test user";
        Integer status = 1; // Changed to valid status range
        task.setTitle(title);
        task.setDescription(description);
        task.setAsigneedTo(assignedTo);
        task.setStatus(status);
        TaskController taskController = new TaskController(taskService);
        ResponseEntity<Task> response = taskController.createTask(task);
        Task newTask = response.getBody();
        String newTitle = "New title for updateTask";
        String newDescription = "New description for updateTask";
        Integer newStatus = 2; // Changed to valid status range
        Task taskDetails = new Task();
        taskDetails.setTitle(newTitle);
        taskDetails.setDescription(newDescription);
        taskDetails.setAsigneedTo("updated user");
        taskDetails.setStatus(newStatus);
        Task updatedTask = taskController.updateTask(newTask.getId(), taskDetails).getBody();
        assertEquals(newTitle, updatedTask.getTitle());
        assertEquals(newDescription, updatedTask.getDescription());
        assertEquals(newStatus, updatedTask.getStatus());
    }

    @Test
    @Order(5)
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void test_deleteTaskById() {
        Task task = new Task();
        String title = "Test title for deleteTaskById";
        String description = "Test description for deleteTaskById";
        String assignedTo = "test user";
        Integer status = 1; // Changed to valid status range
        task.setTitle(title);
        task.setDescription(description);
        task.setAsigneedTo(assignedTo);
        task.setStatus(status);
        TaskController taskController = new TaskController(taskService);
        ResponseEntity<Task> response = taskController.createTask(task);
        Task newTask = response.getBody();
        taskController.deleteTaskById(newTask.getId());
        ResponseEntity<Task> deletedTaskResponse = taskController.getTaskById(newTask.getId());
        assertEquals(HttpStatus.NOT_FOUND, deletedTaskResponse.getStatusCode());
    }

    @Test
    @Order(6)
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void test_deleteTaskById_notFound() {
        TaskController taskController = new TaskController(taskService);
        ResponseEntity<HttpStatus> response = taskController.deleteTaskById(999999999L);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }
}