package br.sistemaToDo.task.entity;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "task_entity") // Define o nome da tabela
public class TaskEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String description;
    private String priority;
    private boolean completed;
    private LocalDate dueDate;
    private String category;

    public TaskEntity() {}

    public TaskEntity(String description, String priority, LocalDate dueDate) {
        this.description = description;
        this.priority = priority;
        this.completed = false;
        this.dueDate = dueDate;
        this.category =  category;

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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