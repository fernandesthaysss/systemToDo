package com.example.task.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public class TaskRequestDTO {

    private Long id;

    @NotBlank(message = "A descrição da tarefa é obrigatória.")
    private String description;

    @NotNull(message = "Prioridade não pode ser nula.")
    private String priority;

    private boolean completed;

    @NotNull(message = "A data de vencimento é obrigatória.")
    private LocalDate dueDate;

    @NotBlank(message = "A categoria da tarefa é obrigatória.")
    private String category;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}