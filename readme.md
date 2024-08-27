# API RESTful SPRING
Este projeto é uma API RESTful que gerencia livros, pessoas e arquivos. Ela oferece operações CRUD básicas (Create, Read, Update e Delete), além de oferecer autenticação de usuários.
Ela permite realizar operações de CRUD em livros e pessoas, buscar pessoas pelo nome, e gerenciar arquivos através de upload e download.
A API também inclui autenticação JWT para garantir a segurança das operações.

Esse projeto foi elaborado durante o curso: [REST API's RESTFul do 0 à AWS c. Spring Boot 3 Java e Docker.](https://www.udemy.com/course/restful-apis-do-0-a-nuvem-com-springboot-e-docker/)

## 💻 Tecnologias
- Java
- Spring Boot
- Spring Security
- PostgreSQL
- Docker
- Maven
- JUnit 5
- Mockito
- Flyway
- Model Mapper
- Testcontainers
- Swagger

## 📚 Assuntos ensinados no curso
- Padrão de Projetos: Value Object
- Versionamento da API
- Documentação com Swagger
- Cors
- Autenticação com JWT
- Custom JSON Serialization e content negotiation
- Migrations com Flyway
- HATEOAS
- Implementação de Upload e Download de arquivos
- Testes unitários e de integração
- Query params e Busca Paginada
- "Dockerizar" a aplicação spring
- Github Actions

## ✅ Pré-requisitos para rodar o projeto
1 - Java (versão 17)  
2 - PostgreSQL  
3 - Maven (para gerar um .jar, caso queira rodar em um container docker)  
4 - Docker (Se quiser rodar os testes de integração ou rodar o projeto em um container)  
5 - IDE de sua preferência
##### *Obs: antes de rodar o projeto, certifique-se de que você configurou a url e as variáveis de ambiente "USERNAME" e "PASSWORD" do seu PostgreSQL que estão no application.yml 
```
spring:
datasource:
url: jdbc:postgresql://localhost:5432/nome_do_banco?timezone=America/Sao_Paulo
username: ${USERNAME}
password: ${PASSWORD}
```
## Como rodar o projeto em um container docker
1. abra um terminal na pasta do projeto e rode o seguinte comando:
    ```
    mvn clean package -DskipTests
    ```
   ###### ** pulamos os testes porque há testes de HATEOAS que vão falhar pelo fato de estarem apontando para a "porta errada".
2. Depois rode esse:
    ````
   docker compose up -d --build 
   ````
## 🔗 Endpoints
base url: `http://localhost:8080`  
caso você esteja rodando no container docker:  
`http://localhost:80`

### Swagger
- **GET** `/swagger-ui/index.html`

### Books
Endpoints para Gerenciamento de Livros

- **GET** `/api/book/v1`
  - Retorna todos os livros

- **PUT** `/api/book/v1`
  - Atualiza um livro

- **POST** `/api/book/v1`
  - Adiciona um novo livro

- **GET** `/api/book/v1/{id}`
  - Retorna um livro pelo ID

- **DELETE** `/api/book/v1/{id}`
  - Deleta um livro pelo ID

### People
Endpoints para Gerenciamento de Pessoas

- **GET** `/api/person/v1`
  - Retorna todas as pessoas

- **PUT** `/api/person/v1`
  - Atualiza uma pessoa

- **POST** `/api/person/v1`
  - Adiciona uma nova pessoa

- **GET** `/api/person/v1/{id}`
  - Retorna uma pessoa pelo ID

- **DELETE** `/api/person/v1/{id}`
  - Deleta uma pessoa pelo ID

- **PATCH** `/api/person/v1/{id}`
  - Desabilita uma pessoa

- **GET** `/api/person/v1/findPersonByName/{firstName}`
  - Retorna pessoas pelo primeiro nome

### Authentication Endpoint

- **PUT** `/auth/refresh/{username}`
  - Atualiza o token do usuário autenticado e retorna um novo token

- **POST** `/auth/signin`
  - Autentica um usuário e retorna um token

### File Endpoint

- **POST** `/api/file/v1/uploadMultipleFiles`
  - Faz upload de múltiplos arquivos

- **POST** `/api/file/v1/uploadFile`
  - Faz upload de um único arquivo

- **GET** `/api/file/v1/downloadFile/{filename}`
  - Faz o download de um arquivo pelo nome

## 👨‍💻 Desenvolvido por
Renato Farias  
https://www.linkedin.com/in/renatofari4s/