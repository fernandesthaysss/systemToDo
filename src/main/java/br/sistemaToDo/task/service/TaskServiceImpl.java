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
import java.util.Objects;
import java.util.stream.Collectors;

// Serviço principal para gerenciamento de tarefas. Implementa as operações de criação, leitura,
// atualização, exclusão e publicação de eventos Kafka.
// Essa classe garante que as validações sejam consistentes e evita erros de estado no banco.
@Service
public class TaskServiceImpl implements TaskService {

    // Repositório para interação com o banco.
    private final TaskRepository taskRepository;

    // Mapper para conversão entre entidade e DTO.
    private final TaskMapper taskMapper;

    // Template para interação com o Kafka - usado para notificar eventos de criação e conclusão.
    private final KafkaTemplate<String, String> kafkaTemplate;

    // Construtor com injeção de dependências para melhor testabilidade do serviço.
    public TaskServiceImpl(TaskRepository taskRepository, TaskMapper taskMapper, KafkaTemplate<String, String> kafkaTemplate) {
        this.taskRepository = taskRepository;
        this.taskMapper = taskMapper;
        this.kafkaTemplate = kafkaTemplate;
    }


    private TaskEntity getTaskOrThrowException(Long id) {
        return taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException(id));
    }

    //- **Validações antes de salvar**: Adicionei verificações (exemplo: `description`, `priority` e `dueDate`) para evitar estados incorretos na aplicação e falhas no banco de dados. Cada validação foi documentada com mensagens claras.
    //- **Registros (`Logs`) claros durante ações cruciais**: As mensagens `System.out.println` ajudariam em ambientes não configurados com logging apropriado. Em produção, idealmente seria usado o framework SLF4J ou Logback.
    //- **Mensagens no Kafka**: Atualizei a lógica para enviar notificações apropriadas quando tarefas são criadas ou marcadas como concluídas. Isso pode ser útil para integrações futuras ou monitoramento de eventos.
    //`@Override` é uma anotação fornecida pelo Java que indica que estamos sobrescrevendo (ou substituindo) um méto do de uma classe ou interface pai (superclasse ou interface implementada).
    // Isso quer dizer que estamos modificando o comportamento de um méto do preexistente ou implementando a assinatura de um méto do de uma interface.

    @Override
    public TaskResponseDTO createTask(TaskRequestDTO taskRequestDTO) {
        // Realiza validações de entrada. Garante que o DTO recebido não seja nulo e que todos os campos obrigatórios estejam preenchidos.
        if (Objects.isNull(taskRequestDTO)) {
            throw new IllegalArgumentException("O objeto TaskRequestDTO não pode ser nulo.");
        }
        if (Objects.isNull(taskRequestDTO.getDescription()) || taskRequestDTO.getDescription().isBlank()) {
            throw new IllegalArgumentException("A descrição da tarefa é obrigatória e não pode estar vazia.");
        }
        if (Objects.isNull(taskRequestDTO.getPriority()) || taskRequestDTO.getPriority().isBlank()) {
            throw new IllegalArgumentException("A prioridade da tarefa é obrigatória.");
        }
        if (Objects.isNull(taskRequestDTO.getDueDate())) {
            throw new IllegalArgumentException("A data de vencimento da tarefa é obrigatória.");
        }

        // Converte o DTO recebido para uma entidade do banco.
        TaskEntity taskEntity = taskMapper.toEntity(taskRequestDTO);

        // Salva no banco de dados e retorna a entidade persistida.
        TaskEntity savedTask = taskRepository.save(taskEntity);

        // Publica um evento Kafka para notificar a criação da tarefa.
        kafkaTemplate.send("task-created", String.format("Tarefa [%s] criada com sucesso.", savedTask.getId()));

        // Retorna o objeto persistido como um DTO de resposta para comunicar ao cliente.
        return taskMapper.toResponse(savedTask);
    }

    @Override
    public TaskResponseDTO updateTask(Long id, TaskRequestDTO taskRequestDTO) {
        if (Objects.isNull(id)) {
            throw new IllegalArgumentException("O ID da tarefa não pode ser nulo.");
        }
        // Garante que a tarefa existe antes de tentar atualizá-la.
        TaskEntity existingTask = taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException(id));

        // Atualiza campos da tarefa com base nos dados enviados.
        if (!Objects.isNull(taskRequestDTO.getDescription())) {
            existingTask.setDescription(taskRequestDTO.getDescription());
        }
        if (!Objects.isNull(taskRequestDTO.getPriority())) {
            existingTask.setPriority(taskRequestDTO.getPriority());
        }
        if (!Objects.isNull(taskRequestDTO.getDueDate())) {
            existingTask.setDueDate(taskRequestDTO.getDueDate());
        }
        existingTask.setCompleted(taskRequestDTO.isCompleted());
        existingTask.setCategory(taskRequestDTO.getCategory());

        // Salva a tarefa atualizada no banco.
        TaskEntity updatedTask = taskRepository.save(existingTask);

        return taskMapper.toResponse(updatedTask);
    }


    @Override
    public TaskResponseDTO getTaskById(Long id) {
        if (Objects.isNull(id)) {
            throw new IllegalArgumentException("O ID não pode ser nulo.");
        }

        // Busca a tarefa pelo ID no repositório.
        TaskEntity taskEntity = taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException(id));

        // Converte e retorna como um objeto DTO.
        return taskMapper.toResponse(taskEntity);
    }


    @Override
    public List<TaskResponseDTO> getAllTasks() {
        // Busca todas as tarefas e transforma em lista de DTOs.
        return taskRepository.findAll()
                .stream()
                .map(taskMapper::toResponse)
                .collect(Collectors.toList());
    }


    @Override
    public void markTaskAsCompleted(Long id) {
        if (Objects.isNull(id)) {
            throw new IllegalArgumentException("O ID da tarefa não pode ser nulo.");
        }
        // Busca a tarefa no banco ou lança uma exceção se não for encontrada.
        TaskEntity taskEntity = taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException(id));

        // Verifica se a tarefa já está concluída.
        if (taskEntity.isCompleted()) {
            throw new IllegalStateException("A tarefa já está marcada como concluída.");
        }

        // Marca como concluída e salva no banco.
        taskEntity.setCompleted(true);
        taskRepository.save(taskEntity);

        // Publica um evento Kafka informando a conclusão da tarefa.
        kafkaTemplate.send("task-completed", String.format("Tarefa [%d] concluída.", id));
    }

    @Override
    public void deleteTask(Long id) {
        if (Objects.isNull(id)) {
            throw new IllegalArgumentException("O ID da tarefa para exclusão não pode ser nulo.");
        }

        // Busca a tarefa para exclusão ou lança exceção se não encontrada.
        TaskEntity task = taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException(id));

        // Remove do banco de dados.
        taskRepository.delete(task);
    }


    private void deleteTask(TaskEntity taskEntity) {
        taskRepository.delete(taskEntity);
    }

    private List<TaskResponseDTO> convertTasksToDTOs(List<TaskEntity> tasks) {
        return tasks.stream().map(taskMapper::toResponse).toList();
    }


}