# DroneFeeder - O sistema de delivery da sua empresa!

## Table of contents
* [Setup e execução da aplicação](#setup-e-execução-da-aplicação)
* [Tecnologias](#tecnologias)

Link para o swagger
http://localhost:8080/swagger-ui/#/


** https://www.baeldung.com/tag/swagger

## Tecnologias
Esta aplicação foi criada usando as seguintes tecnologias:

1. Java 11
2. Maven Dependency Management
3. Spring Boot:

    + Spring Web
    + Spring Data JPA
    + Actuator

4. MySQL Database
5. Docker

## Setup e execução da aplicação
A aplicação pode ser facilmente configurada e executada com o comando:
```bash
docker-compose up
```

O docker fará o pull das imagem do MySQL e do JDK (se você não as tiver na sua máquina).

(Recomendado) Os serviços podem ser executados em background com o comando:
```bach
docker-compose up -d
```

## Parar aplicação
Para parar todos os containers em execução basta rodas o comando:
```bash
docker-compose down
````

Se você precisar parar e removar todos containers, networks e todas imagens usadas por qualquer serviço do arquivo <em>docker-compose.yml</eml>, use o comando:
```bash
docker-compose down --rmi all
```
