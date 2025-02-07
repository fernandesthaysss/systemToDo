package br.sistemaToDo.task.kafka;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class KafkaConsumer {

    //- **Condicional para mensagens nulas/vazias**: Isso é crucial para evitar o processamento de mensagens inesperadas ou malformadas.
    //- **Tratamento robusto de exceções no listener**: Um erro no processamento da mensagem não deve interromper o consumidor Kafka.
    //- **Responsabilidade isolada para processamento**: Mensagens podem ser registradas, processadas ou persistidas dependendo dos requisitos.

        // Ouvinte para o tópico "task-created" que processa mensagens de criação de tarefas
            @KafkaListener(topics = "task-created", groupId = "task-group")
            public void consumeTaskCreated(String message) {
                // Valida se a mensagem é nula ou vazia usando Objects.isNull
                if (Objects.isNull(message) || message.trim().isEmpty()) {
                    System.err.println("Mensagem vazia ou nula recebida no Kafka. Ignorando...");
                    return;
                }

                // Lógica de processamento da mensagem
                try {
                    System.out.println("Processando mensagem do Kafka (Task Created): " + message);

                } catch (Exception ex) {
                    // Tratamento de exceções para evitar falhas no listener
                    System.err.println("Erro ao processar a mensagem do Kafka: " + ex.getMessage());
                }
            }
        }

