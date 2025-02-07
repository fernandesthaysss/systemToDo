package br.sistemaToDo.task;

import br.sistemaToDo.task.entity.TaskEntity;
import br.sistemaToDo.task.repository.TaskRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.time.LocalDate;

@SpringBootApplication
public class TaskServiceApplication {
	public static void main(String[] args) {
		ConfigurableApplicationContext context = SpringApplication.run(TaskServiceApplication.class, args);

		TaskRepository taskRepository = context.getBean(TaskRepository.class);

		TaskEntity task = new TaskEntity("Test task with due date", "Medium", LocalDate.now().plusDays(7));

		// Validação antes de persistir no banco
		if (task.getDescription() == null || task.getDescription().trim().isEmpty()) {
			throw new IllegalStateException("Descrição da tarefa obrigatória ao inicializar.");
		}

		task = taskRepository.save(task);

		System.out.println("Task salva: " + task.getId());
	}
}