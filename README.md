# DroneFeeder - O sistema de delivery da sua empresa!

## Table of contents
* [Setup e execução da aplicação](#setup-e-execução-da-aplicação)

Link para o swagger
http://localhost:8080/swagger-ui/#/


** https://www.baeldung.com/tag/swagger

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
