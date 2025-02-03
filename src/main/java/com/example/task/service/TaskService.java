package com.example.task.service;

import com.example.task.dto.TaskRequestDTO;
import com.example.task.dto.TaskResponseDTO;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public interface TaskService {

    TaskResponseDTO createTask(TaskRequestDTO taskRequestDTO);

    List<TaskResponseDTO> getAllTasks();

    void markTaskAsCompleted(Long id);

    void deleteTask(Long id);

    TaskResponseDTO updateTask(Long id, TaskRequestDTO taskRequestDTO);

    TaskResponseDTO getTaskById(Long id);



}
