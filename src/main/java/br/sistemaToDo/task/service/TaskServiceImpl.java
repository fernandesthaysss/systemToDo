package br.sistemaToDo.task.service;

import br.sistemaToDo.task.mapper.TaskMapper;
import br.sistemaToDo.task.dto.TaskRequestDTO;
import br.sistemaToDo.task.dto.TaskResponseDTO;
import br.sistemaToDo.task.entity.TaskEntity;
import br.sistemaToDo.task.repository.TaskRepository;
import br.sistemaToDo.taskmanagement.exception.TaskNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final TaskMapper taskMapper;

    public TaskServiceImpl(TaskRepository taskRepository, TaskMapper taskMapper) {
        this.taskRepository = taskRepository;
        this.taskMapper = taskMapper;
    }

    private TaskEntity getTaskOrThrowException(Long id) {
        return taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException(id));
    }

    @Override
    public TaskResponseDTO createTask(TaskRequestDTO taskRequestDTO) {
        TaskEntity taskEntity = taskMapper.toEntity(taskRequestDTO);
        TaskEntity savedTask = taskRepository.save(taskEntity);
        return taskMapper.toResponse(savedTask);
    }

    @Override
    public TaskResponseDTO updateTask(Long id, TaskRequestDTO taskRequestDTO) {
        TaskEntity taskEntity = getTaskOrThrowException(id);

        taskEntity.setDescription(taskRequestDTO.getDescription());
        taskEntity.setPriority(taskRequestDTO.getPriority());
        taskEntity.setDueDate(taskRequestDTO.getDueDate());
        taskEntity.setCompleted(taskRequestDTO.isCompleted());
        taskEntity.setCategory(taskRequestDTO.getCategory());

        TaskEntity updatedTask = taskRepository.save(taskEntity);
        return taskMapper.toResponse(updatedTask);
    }

    public TaskResponseDTO getTaskById(Long id) {
        TaskEntity task = taskRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Tarefa com ID " + id + " não encontrada."));
        return new TaskResponseDTO(
                task.getId(),
                task.getDescription(),
                task.getPriority(),
                task.isCompleted(),
                task.getDueDate(),
                task.getCategory()
        );
    }


        @Override
    public List<TaskResponseDTO> getAllTasks() {
        return convertTasksToDTOs(taskRepository.findAll());
    }

    @Override
    public void markTaskAsCompleted(Long id) {
        TaskEntity taskEntity = getTaskOrThrowException(id);
        taskEntity.setCompleted(true);
        updateTasks(taskEntity);
    }

    @Override
    public void deleteTask(Long id) {
        TaskEntity taskEntity = getTaskOrThrowException(id);
        deleteTask(taskEntity);
    }

    private void updateTasks(TaskEntity taskEntity) {
        taskRepository.save(taskEntity);
    }

    private void deleteTask(TaskEntity taskEntity) {
        taskRepository.delete(taskEntity);
    }

    private List<TaskResponseDTO> convertTasksToDTOs(List<TaskEntity> tasks) {
        return tasks.stream().map(taskMapper::toResponse).toList();
    }
}