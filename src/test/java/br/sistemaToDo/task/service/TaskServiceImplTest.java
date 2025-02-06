package br.sistemaToDo.task.service;

import br.sistemaToDo.task.dto.TaskRequestDTO;
import br.sistemaToDo.task.dto.TaskResponseDTO;
import br.sistemaToDo.task.entity.TaskEntity;
import br.sistemaToDo.task.mapper.TaskMapper;
import br.sistemaToDo.task.repository.TaskRepository;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.kafka.core.KafkaTemplate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class TaskServiceImplTest {

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private TaskMapper taskMapper;

    @Mock
    private KafkaTemplate<String, String> kafkaTemplate;

    @InjectMocks
    private TaskServiceImpl taskService;

    @Test
    void shouldSendMessageToKafkaWhenTaskCreated() {
        // Arrange
        TaskRequestDTO taskRequestDTO = new TaskRequestDTO();
        taskRequestDTO.setDescription("Nova Tarefa");
        taskRequestDTO.setPriority("Alta");

        TaskEntity taskEntity = new TaskEntity();
        taskEntity.setDescription("Nova Tarefa");

        when(taskMapper.toEntity(taskRequestDTO)).thenReturn(taskEntity);
        when(taskRepository.save(taskEntity)).thenReturn(taskEntity);

        // Act
        taskService.createTask(taskRequestDTO);

        // Capture the Kafka message
        ArgumentCaptor<String> messageCaptor = ArgumentCaptor.forClass(String.class);
        verify(kafkaTemplate, times(1)).send(eq("task-created"), messageCaptor.capture());

        // Assert
        assertEquals("Tarefa criada: Nova Tarefa", messageCaptor.getValue());
    }
}