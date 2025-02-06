package br.sistemaToDo.task.kafka;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class KafkaConsumer {

    // Ouvinte para o tópico "task-created" que processa mensagens de criação de tarefas
    @KafkaListener(topics = "task-created", groupId = "task-group")
    public void consumeTaskCreated(String message) {
        // Exibe a mensagem recebida no console
        System.out.println("Notificação recebida no Kafka (Task Created): " + message);
    }
}