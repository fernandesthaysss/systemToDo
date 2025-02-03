package com.example.taskmanagement.exception;

public class TaskNotFoundException extends RuntimeException {

    public static final String TASK_NOT_FOUND_MESSAGE = "Tarefa com o ID %d não encontrada.";

    public TaskNotFoundException(Long id) {
        super(String.format(TASK_NOT_FOUND_MESSAGE, id));
    }
}