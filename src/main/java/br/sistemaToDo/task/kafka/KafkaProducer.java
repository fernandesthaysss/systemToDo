package br.sistemaToDo.task.kafka;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaProducer {

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    private static final String TOPIC = "task-topic-new";

    public void sendMessage(String message) {
        kafkaTemplate.send(TOPIC, message);
        System.out.println("Mensagem enviada ao tópico Kafka: " + message);
    }
}