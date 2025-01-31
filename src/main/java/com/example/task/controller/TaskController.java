package com.example.task.controller;

import com.example.task.service.TaskService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/tasks")
public class TaskController {
    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping
    public String listTasks(Model model) {
        model.addAttribute("tasks", taskService.getTasks());
        return "index";
    }

    @PostMapping("/add")
    public String addTask(@RequestParam String description) {
        taskService.addTask(description);
        return "redirect:/tasks";
    }

    @PostMapping("/complete")
    public String completeTask(@RequestParam int taskId) {
        taskService.completeTask(taskId);
        return "redirect:/tasks";
    }

    @PostMapping("/delete")
    public String deleteTask(@RequestParam int taskId) {
        taskService.deleteTask(taskId);
        return "redirect:/tasks";
    }
}