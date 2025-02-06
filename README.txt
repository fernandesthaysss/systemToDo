Esta aplicação é uma **API RESTful** para o gerenciamento de tarefas.
Usuários podem criar tarefas, atualizar os detalhes de tarefas, marcar tarefas como concluídas, excluí-las, e listar tarefas existentes.
A aplicação foi construída seguindo princípios modernos de **arquitetura multicamadas**, **princípios SOLID**, e **boas práticas de Clean Code**.
### **Funcionalidades**:
- Criar tarefas.
- Atualizar tarefas (ex: marcar como concluída).
- Excluir tarefas.
- Listar todas as tarefas.
- Consultar detalhes de uma tarefa específica.
- Integração com **Kafka** para processamentos assíncronos/eventos.

## **Tecnologias e Bibliotecas Utilizadas**
Esta aplicação utiliza as seguintes tecnologias:
### **Backend:**
- **Linguagem de Programação**: Java (JDK 17 ou superior)
- **Framework Principal**: Spring Boot
    - **Módulos Usados**:
        - **Spring Web**: Facilita a criação de APIs RESTful.
        - **Spring Data JPA**: Para acesso ao banco de dados relacional.
        - **Spring Kafka**: Integração com Apache Kafka para mensageria.
        - **Spring Validation**: Validação de dados de entrada com anotações.

- **Banco de Dados**: PostgreSQL
- **Mensageria Assíncrona**: Apache Kafka
- **Mappers**: MapStruct (para conversão entre entidades e DTOs)
- **Documentação de API**: Swagger/OpenAPI
- **Testes**:
    - JUnit 5
    - Mockito (para criação de mocks em testes unitários)

### **Outras Ferramentas:**
- Docker e Docker Compose (para gerenciar banco de dados ou cluster Kafka localmente).

## **Arquitetura e Boas Práticas**
### **Design Patterns Aplicados:**
1. **Strategy Pattern**: Utilizado para abstrair operações através da interface `TaskService`. Garante flexibilidade de adicionar ou modificar implementações específicas, seguindo os princípios de inversão de dependência (DIP).
2. **Factory Pattern**: Impresso na conversão automática de DTOs para Entidades via `MapStruct`. Centraliza e simplifica a conversão de objetos entre camadas, garantindo um formato consistente.
3. **Observer Pattern**: Implementado para event-driven integration com Kafka (Producer/Consumer). O serviço publica eventos como "task-created" no Kafka, e consumidores externos podem reagir de maneira desacoplada.
4. **Adapter Pattern**: Utilizo DTOs junto com Mapper para adaptar entre camada de API e Banco de Dados. Essa abordagem desacopla os dados recebidos pela API do modelo interno do sistema.
5. **Template Method Pattern**: Métodos utilitários reutilizados em diversas operações, como **getTaskOrThrowException** (padrão para busca de tarefas no banco com validação), reforçando DRY (Don't Repeat Yourself).
6. **Singleton Pattern**: Gerenciado automaticamente pelo Spring para Beans anotados com `@Service`, `@Repository`, e outros. Garante que existam instâncias únicas no ciclo de vida da aplicação, otimizando recursos e consistência.
7. **Dependency Injection (DI)**: Prática fundamental no Spring, utilizada para prover dependências como `TaskRepository` e `KafkaTemplate` dentro das classes, garantindo desacoplamento e facilidade para testes.
8. **Builder Pattern**: Aplicado implicitamente na criação fluida e encadeada de entidades ou DTOs, permitindo construção clara e abrangente de objetos. Exemplo com entidades: `TaskEntity.builder().title("title").deadline(date).build();`.
9. **Repository Pattern**: Implementado via Spring Data JPA para lidar com a persistência em banco de dados. Permite definir um acesso de dados limpo e abstrato utilizando métodos pré-definidos ou personalizados.
10. **Facade Pattern**: A camada de serviço (`TaskService`) atua como uma fachada, simplificando o acesso a múltiplos processos ou integrações de forma consolidada. Exemplo: `createTask()` combina lógica de validação, persistência e mapeamento.


### **Arquitetura**:
A aplicação segue uma arquitetura multicamadas:
- **Controller Layer (Apresentação):**
    - Define as APIs RESTful utilizando a anotação `@RestController`.
    - Responsável exclusivamente pelo recebimento de requisições HTTP e envio de respostas, delegando a lógica principal à camada de serviço.
    - Utiliza DTOs para entrada e saída de dados (evitando o acoplamento com entidades do banco de dados).

- **Service Layer (Lógica de Negócio):**
    - Implementa regras de negócio principais da aplicação.
    - Centraliza chamadas para outras camadas ou integrações externas (e.g., Kafka Producer, Mappers, ou Repository).
    - Faz uso dos princípios SOLID para desacoplamento.

- **Repository Layer (Persistência de Dados):**
    - Camada dedicada ao acesso ao Banco de Dados.
    - Utiliza o **Spring Data JPA** para realizar operações CRUD de forma eficiente e segura.

- **Camada de Integração:**
    - Kafka Producer: Publica eventos como "task-created" em tópicos Kafka para serem consumidos por outros serviços.
    - Kafka Consumer: Escuta eventos de Kafka e realiza processamento assíncrono.

### **Princípios de Engenharia Aplicados**:
- **SOLID Princípios:**
    - **Single Responsibility Principle (SRP):** Cada classe tem uma única responsabilidade.
    - **Open/Closed Principle (OCP):** As classes podem ser estendidas sem necessidade de modificação.
    - **Dependency Inversion:** As classes dependem de abstrações (interfaces como `TaskService`) em vez de implementações.

- **Clean Code:**
    - Métodos curtos e coesos.
    - Nomeação explícita e autodocumentada (exemplo: `createTask`, `markTaskAsCompleted`).
    - Sem duplicação de código, graças ao uso de Mappers e serviços reutilizáveis.

- **Boas Práticas:**
    - Isolamento de responsabilidades por meio de DTOs.
    - Centralização de erros em handlers globais (`GlobalExceptionHandler`).
    - Documentação automatizada para APIs usando Swagger.

## **Instalação e Configuração**
Para rodar a aplicação, siga os passos abaixo.
### **Requisitos**
Certifique-se de que você tem as ferramentas listadas a seguir instaladas:
- **[Java 17](https://adoptium.net/)** ou superior
- **[Apache Maven](https://maven.apache.org/)** (opcional, caso não use IDE com suporte integrado)
- **[PostgreSQL](https://www.postgresql.org/download/)**
- **[Apache Kafka](https://kafka.apache.org/downloads)** (opcional, utilize Docker como alternativa)
- **[Docker](https://www.docker.com)** e **Docker Compose** (opcional, mas recomendado)

### **Configuração do Banco de Dados PostgreSQL**
1. Instale o PostgreSQL e crie um banco chamado `task_entity`:
``` sql
CREATE DATABASE task_entity;
```
1. Configure o usuário e senha para acessar o banco.
2. No arquivo **`application.yml`** (ou em variáveis de ambiente), adicione as credenciais e URL do banco:
``` yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/task_entity
    username: seu_usuario
    password: sua_senha
    driver-class-name: org.postgresql.Driver
  jpa:
    hibernate:
      ddl-auto: update
```
### **Configuração do Kafka**
Você pode rodar o Kafka localmente usando **Docker Compose**. Adicione o seguinte conteúdo no arquivo `docker-compose.yml`:
``` yaml
version: '3'
services:
  zookeeper:
    image: confluentinc/cp-zookeeper:latest
    ports:
      - "2181:2181"
    environment:
      ZOOKEEPER_CLIENT_PORT: 2181
      ZOOKEEPER_TICK_TIME: 2000

  kafka:
    image: confluentinc/cp-kafka:latest
    ports:
      - "9092:9092"
    environment:
      KAFKA_BROKER_ID: 1
      KAFKA_ZOOKEEPER_CONNECT: zookeeper:2181
      KAFKA_ADVERTISED_LISTENERS: PLAINTEXT://localhost:9092
      KAFKA_OFFSETS_TOPIC_REPLICATION_FACTOR: 1
```
Para subir o Kafka e Zookeeper, utilize:
``` bash
docker-compose up -d
```
Altere o `application.yml` para usar o Kafka:
``` yaml
spring:
  kafka:
    bootstrap-servers: localhost:9092
    consumer:
      group-id: task-group
    producer:
      key-serializer: org.apache.kafka.common.serialization.StringSerializer
      value-serializer: org.apache.kafka.common.serialization.StringSerializer
```
## **Como Executar a Aplicação**
1. Clone o repositório:
``` bash
git clone <URL_DO_REPOSITORIO>
cd pasta_do_projeto
```
1. Compile o projeto usando Maven:
``` bash
./mvnw clean install
```
1. Rode a aplicação:
``` bash
./mvnw spring-boot:run
```
1. A aplicação estará acessível em **[http://localhost:8080](http://localhost:8080)**.

## **APIs Disponíveis**
A documentação completa das APIs pode ser acessada pela interface Swagger em **[http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html) **.
### **Exemplo de Endpoints:**

| Método | Endpoint | Descrição |
| --- | --- | --- |
| GET | `/tasks` | Lista todas as tarefas |
| GET | `/tasks/{id}` | Consulta uma tarefa específica |
| POST | `/tasks` | Cria uma nova tarefa |
| PUT | `/tasks/{id}` | Atualiza uma tarefa existente |
| DELETE | `/tasks/{id}` | Exclui uma tarefa |
### **Exemplo de Payload:**
**POST `/tasks`**
``` json
{
  "title": "Concluir projeto",
  "description": "Finalizar o projeto de arquitetura",
  "deadline": "2023-12-30"
}
```
## **Execução de Testes**
Os testes foram implementados usando **JUnit 5** e **Mockito**.
Para executar todos os testes unitários:
``` bash
./mvnw test
```

## **Estrutura de Arquivos**
``` bash
task-service/              # Root do projeto
├── .idea/                 # Configurações do IntelliJ IDEA (se aplicável)
├── .mvn/                  # Scripts de execução Maven
├── node_modules/          # Dependências Node.js (para Tailwind e front estático)
├── src/                   # Código fonte da aplicação
│   ├── main/
│   │   ├── java/br/sistemaToDo/
│   │   │   ├── controller/   # Endpoints REST (Controller API)
│   │   │   ├── service/      # Regras de negócio
│   │   │   ├── repository/   # Repositórios e acessos a dados
│   │   │   ├── entity/       # Entidades JPA (modelos do banco)
│   │   │   ├── dto/          # Data Transfer Objects
│   │   │   ├── kafka/        # Producer e Consumer do Kafka
│   │   │   ├── exception/    # Tratamento centralizado de erros
│   │   └── resources/        # Recursos da aplicação
│   │       ├── application.yml       # Configurações de ambiente do Spring Boot
│   │       ├── schema.sql            # (Opcional) Script para criação de tabelas
│   │       └── data.sql              # (Opcional) Dados iniciais para testes
│   ├── test/
│   │   ├── java/br/sistemaToDo/      # Testes automatizados (unitários)
│   │
├── target/                 # Arquivos gerados na compilação (compilados pelo Maven)
├── .gitattributes          # Configurações para versionamento Git
├── .gitignore              # Ignorar arquivos temporários no Git
├── HELP.md                 # Arquivo de ajuda gerado automaticamente (Spring Boot)
├── maven_settings.xml      # Configurações adicionais Maven (opcional)
├── mvnw                    # Script para execução do Maven (Unix)
├── mvnw.cmd                # Script para execução do Maven (Windows)
├── package.json            # Configuração do Node.js e dependências JS
├── package-lock.json       # Versões bloqueadas para dependências Node.js
├── pom.xml                 # Arquivo de configuração Maven
├── postcss.config.js       # Configuração do TailwindCSS (CSS dinâmico)
├── README.txt              # Arquivo de Leitura (placeholder inicial)
└── tailwind.config.js      # Configuração Tailwind (opcional para estilos)

### **Descrição dos Diretórios Principais**
- **`controller/`**: Implementa os **endpoints REST** da aplicação, seguindo o padrão MVC. As classes aqui se limitam a delegar chamadas à camada de serviço.
- **`service/`**: Contém toda a lógica de negócios da aplicação (implementações concretas para ações relacionadas às tarefas).
- **`repository/`**: Define interfaces de persistência com JPA e Spring Data.
- **`kafka/`**: Gerencia o envio (`Producer`) e recebimento (`Consumer`) de mensagens via Apache Kafka.
- **`entity/`**: Modelo de dados persistentes na base, mapeados com JPA.
- **`exception/`**: Agrupa classes para tratamento de exceções e respostas customizadas de erro.
- **`dto/`**: Representa transferências seguras e organizadas entre o cliente e a API.

```
