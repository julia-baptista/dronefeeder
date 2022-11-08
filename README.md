# DroneFeeder

<h3>O sistema de delivery da sua empresa! </h3>

    Este aplicativo permitirá a entrega de pacotes com drones.
    Ele é responsável pelo serviço de Back-end da aplicação. Este serviço irá receber
    e fornecer informações aos drones, receber informações do cadastrador
    de pedidos, e fornecer algumas informações para o Front-end.
    
    
    &nbsp;
    O Front-end irá fornecer as informações dos pedidos tais como latitude
    e longitude e data e horário da entrega. Também permitirá a listagem de
    todos os videos das entregas bem como o download de vídeos relacionado a
    algum pedido específico.


## Tecnologias
<h3>Esta aplicação foi criada usando as seguintes tecnologias:</h3>

1. Java 11
2. Maven Dependency Management
3. Spring Boot:

    + Spring Web
    + Spring Data JPA
    + Actuator

4. Hibernate ORM
5. MySQL Database
6. JUnit
7. Docker

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


&nbsp;
## A API REST para este aplicativo está descrita abaixo.

### * Drone-controller

<h4> 1. Operação responsável por listar os drones <h4>
<h3>Request</h3>

`GET /dronefeeder/v1/drone`

    curl -X GET "http://localhost:8080/dronefeeder/v1/drone" -H "accept: application/json"

<h3> Response body </h3>
```
[
  {
    "id": 1, 
    "nome": "Drone A7", 
    "marca": "Marca A", 
    "fabricante": "‎Fabricante A",  
    "altitudeMax": 1000,  
    "duracaoBateria": 180, 
    "capacidadeKg": 50,  
    "capacidadeM3": 50,   
    "status": "Ativo"  
  },
  ...
]
```

    
## Link para o swagger
http://localhost:8081/dronefeeder/swagger-ui/#/