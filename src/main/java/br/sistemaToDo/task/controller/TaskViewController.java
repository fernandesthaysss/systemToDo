package br.sistemaToDo.task.controller;

import br.sistemaToDo.task.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/tasks")
public class TaskViewController {

    @GetMapping("/")
    public String listTasks(Model model) {
        return "tasks/index";
    }

    @GetMapping("/{id}")
    public String showTask(@PathVariable Long id, Model model) {
        return "tasks/show";
    }
}
