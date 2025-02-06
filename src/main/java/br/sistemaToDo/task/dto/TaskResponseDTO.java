package br.sistemaToDo.task.dto;

import java.time.LocalDate;

public class TaskResponseDTO {

    private Long id;
    private String description;
    private String priority;
    private boolean completed;
    private LocalDate dueDate;
    private String category;

    public TaskResponseDTO(Long id, String description, String priority, boolean completed, LocalDate dueDate , String category) {
        this.id = id;
        this.description = description;
        this.priority = priority;
        this.completed = completed;
        this.dueDate = dueDate;
        this.category = category;
    }


    public Long getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public String getPriority() {
        return priority;
    }

    public boolean isCompleted() {
        return completed;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public String getCategory() {
        return category;
    }
}
