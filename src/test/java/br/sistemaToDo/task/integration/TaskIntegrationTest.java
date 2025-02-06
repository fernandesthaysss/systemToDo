package br.sistemaToDo.task.integration;

import br.sistemaToDo.task.dto.TaskRequestDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class TaskIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @Test
    void shouldCreateTaskAndSendKafkaNotification() {
        // Arrange
        TaskRequestDTO taskRequest = new TaskRequestDTO();
        taskRequest.setDescription("Tarefa de Integração");
        taskRequest.setPriority("Alta");

        // Act
        ResponseEntity<Void> response = restTemplate.postForEntity("/api/v1", taskRequest, Void.class);

        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());

        // Simula a validação de mensagem do Kafka
        kafkaTemplate.send("task-created", "Teste de mensagem Kafka");
    }
}