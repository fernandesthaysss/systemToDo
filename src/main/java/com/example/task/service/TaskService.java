package com.example.task.service;

import com.example.task.Task;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class TaskService {
    private final List<Task> tasks = new ArrayList<>();
    private int nextId = 1;

    public List<Task> getTasks() {
        return tasks;
    }

    public void addTask(String description) {
        Task task = new Task(nextId++, description);
        tasks.add(task);
    }

    public void completeTask(int taskId) {
        Optional<Task> taskOptional = tasks.stream()
                .filter(task -> task.getId() == taskId)
                .findFirst();
        taskOptional.ifPresent(task -> task.setCompleted(true));
    }

    public void deleteTask(int taskId) {
        tasks.removeIf(task -> task.getId() == taskId);
    }
}