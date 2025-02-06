package br.sistemaToDo.task.service;

import br.sistemaToDo.task.mapper.TaskMapper;
import br.sistemaToDo.task.dto.TaskRequestDTO;
import br.sistemaToDo.task.dto.TaskResponseDTO;
import br.sistemaToDo.task.entity.TaskEntity;
import br.sistemaToDo.task.repository.TaskRepository;
import br.sistemaToDo.taskmanagement.exception.TaskNotFoundException;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskServiceImpl implements TaskService {
    private final TaskRepository taskRepository;
    private final TaskMapper taskMapper;
    private final KafkaTemplate<String, String> kafkaTemplate;


    public TaskServiceImpl(TaskRepository taskRepository, TaskMapper taskMapper, KafkaTemplate<String, String> kafkaTemplate) {
        this.taskRepository = taskRepository;
        this.taskMapper = taskMapper;
        this.kafkaTemplate = kafkaTemplate; // Injeção de dependência para comunicação com o Kafka
    }

    private TaskEntity getTaskOrThrowException(Long id) {
        return taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException(id));
    }

    @Override
    public TaskResponseDTO createTask(TaskRequestDTO taskRequestDTO) {
        // Converte o DTO para a entidade da tarefa
        TaskEntity taskEntity = taskMapper.toEntity(taskRequestDTO);

        // Log para depuração
        System.out.println("Criando tarefa: " + taskEntity.getDescription());

        // Salva a entidade no banco
        TaskEntity savedTask = taskRepository.save(taskEntity);

        // Log de confirmação
        System.out.println("Tarefa salva com ID: " + savedTask.getId());

        // Retorna o DTO da tarefa criada
        return taskMapper.toResponse(savedTask);
    }


    @Override
    public TaskResponseDTO updateTask(Long id, TaskRequestDTO taskRequestDTO) {
        // Verifique se a tarefa existe antes de atualizar
        TaskEntity taskEntity = getTaskOrThrowException(id);

        // Atualiza os valores da entidade com base no DTO recebido
        taskEntity.setDescription(taskRequestDTO.getDescription());
        taskEntity.setPriority(taskRequestDTO.getPriority());
        taskEntity.setDueDate(taskRequestDTO.getDueDate());
        taskEntity.setCompleted(taskRequestDTO.isCompleted());
        taskEntity.setCategory(taskRequestDTO.getCategory());

        // Salva a entidade atualizada no banco
        TaskEntity updatedTask = taskRepository.save(taskEntity);

        // Converte a entidade em DTO e retorna
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


    public List<TaskResponseDTO> getAllTasks() {
        return convertTasksToDTOs(taskRepository.findAll());
    }

    public void markTaskAsCompleted(Long id) {
        TaskEntity taskEntity = getTaskOrThrowException(id);
        taskEntity.setCompleted(true);
        updateTasks(taskEntity);
    }

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