# DroneFeeder

<h3>O sistema de delivery da sua empresa! </h3>

    Este aplicativo permitirá a entrega de pacotes com drones. Ele é
    responsável pelo serviço de Back-end da aplicação. Este serviço irá
    recebere fornecer informações aos drones, receber informações do
    cadastrador de pedidos, e fornecer algumas informações para o Front-end.
     
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

```[
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


<h4> 2. Operação responsável por cadastrar um drone <h4>
<h3>Request</h3>

`POST /dronefeeder/v1/drone`

    curl -X POST "http://localhost:8080/dronefeeder/v1/drone" -H "accept: application/json" -H "Content-Type: application/json" -d "{ \"altitudeMax\": 4000, \"capacidadeKg\": 20, \"capacidadeM3\": 10, \"duracaoBateria\": 300, \"fabricante\": \"‎DJI\", \"marca\": \"Mini 2\", \"nome\": \"Drone DJI Mini 2\"}"
    
<h3>Parameters</h3>

```{
  "altitudeMax": 4000,
  "capacidadeKg": 20,
  "capacidadeM3": 10,
  "duracaoBateria": 300,
  "fabricante": "‎DJI",
  "marca": "Mini 2",
  "nome": "Drone DJI Mini 2"
}
```    

<h3> Response body </h3>

```{
  "id": 5,
  "nome": "Drone DJI Mini 2",
  "marca": "Mini 2",
  "fabricante": "‎DJI",
  "altitudeMax": 4000,
  "duracaoBateria": 300,
  "capacidadeKg": 20,
  "capacidadeM3": 10,
  "status": "Ativo"
}
```

<h4> 3. Operação responsável por alterar dados de um drone <h4>
<h3>Request</h3>

`PUT /dronefeeder/v1/drone/{id}`

    curl -X PUT "http://localhost:8080/dronefeeder/v1/drone/5" -H "accept: application/json" -H "Content-Type: application/json" -d "{ \"altitudeMax\": 5000, \"capacidadeKg\": 20, \"capacidadeM3\": 10, \"duracaoBateria\": 300, \"fabricante\": \"‎DJI\", \"marca\": \"Mini 2\", \"nome\": \"Drone2 DJI Mini 2\"}"
    
<h3>Parameters</h3>

```{
  "altitudeMax": 5000,
  "capacidadeKg": 20,
  "capacidadeM3": 10,
  "duracaoBateria": 300,
  "fabricante": "‎DJI",
  "marca": "Mini 2",
  "nome": "Drone DJI Mini 2"
}
```    

<h3> Response body </h3>

```{
  "id": 5,
  "nome": "Drone DJI Mini 2",
  "marca": "Mini 2",
  "fabricante": "‎DJI",
  "altitudeMax": 5000,
  "duracaoBateria": 300,
  "capacidadeKg": 20,
  "capacidadeM3": 10,
  "status": "Ativo"
}
```

<h4> 4. Operação responsável por deletar um drone <h4>
<h3>Request</h3>

`DELETE /dronefeeder/v1/drone/{id}`

    curl -X DELETE "http://localhost:8080/dronefeeder/v1/drone/5" -H "accept: */*"

<h3> Response headers </h3>

```cache-control: no-cacheno-storemax-age=0must-revalidate 
 connection: keep-alive 
 content-length: 0 
 date: Tue08 Nov 2022 21:38:16 GMT 
 expires: 0 
 keep-alive: timeout=60 
 pragma: no-cache 
 x-content-type-options: nosniff 
 x-frame-options: DENY 
 x-xss-protection: 1; mode=block 
```

<h4> 5. Operação responsável por ativar um drone <h4>
<h3>Request</h3>

`PUT /dronefeeder/v1/drone/ativar/{id}`

    curl -X PUT "http://localhost:8080/dronefeeder/v1/drone/ativar/1" -H "accept: */*"

<h3> Response headers </h3>

``` cache-control: no-cacheno-storemax-age=0must-revalidate 
 connection: keep-alive 
 content-length: 0 
 date: Tue08 Nov 2022 21:41:30 GMT 
 expires: 0 
 keep-alive: timeout=60 
 pragma: no-cache 
 x-content-type-options: nosniff 
 x-frame-options: DENY 
 x-xss-protection: 1; mode=block 
```

<h4> 6. Operação responsável por desativar um drone <h4>
<h3>Request</h3>

`PUT /dronefeeder/v1/drone/inativar/{id}`

    curl -X PUT "http://localhost:8080/dronefeeder/v1/drone/inativar/1" -H "accept: */*"

<h3> Response headers </h3>

```  cache-control: no-cacheno-storemax-age=0must-revalidate 
 connection: keep-alive 
 content-length: 0 
 date: Tue08 Nov 2022 21:43:47 GMT 
 expires: 0 
 keep-alive: timeout=60 
 pragma: no-cache 
 x-content-type-options: nosniff 
 x-frame-options: DENY 
 x-xss-protection: 1; mode=block 
```


### * Pedido-controller

<h4> 1. Operação responsável por listar os pedidos <h4>
<h3>Request</h3>

`GET /dronefeeder/v1/pedido`

    curl -X GET "http://localhost:8080/dronefeeder/v1/pedido" -H "accept: application/json"

<h3> Response body </h3>

```[
    {
    "id": 6,
    "dataEntregaProgramada": "20/11/2022 12:00",
    "duracaoDoPercurso": 60,
    "dataProgramadaDaSaida": "20/11/2022 11:00",
    "dataConfirmacaoEntrega": "",
    "enderecoDeEntrega": "Avenida Campeche, 100",
    "status": "aberto",
    "descricaoPedido": "Jogo de pratos",
    "valorDoPedido": 1000.99,
    "droneId": 2,
    "pesoKg": 5,
    "volumeM3": 5,
    "latitude": null,
    "longitude": null
  },
  ...
]
```

<h4> 2. Operação responsável por cadastrar um pedido <h4>
<h3>Request</h3>

`POST /dronefeeder/v1/pedido`

    curl -X POST "http://localhost:8080/dronefeeder/v1/pedido" -H "accept: application/json" -H "Content-Type: application/json" -d "{ \"dataEntregaProgramada\": \"20/11/2022 12:00\", \"descricaoPedido\": \"Jogo de pratos\", \"droneId\": 2, \"duracaoDoPercurso\": 60, \"enderecoDeEntrega\": \"Avenida Campeche, 100\", \"pesoKg\": 5, \"valorDoPedido\": 1000.99, \"volumeM3\": 5}"
    
 <h3>Parameters</h3>
 ```{
  "dataEntregaProgramada": "20/11/2022 12:00",
  "descricaoPedido": "Jogo de pratos",
  "droneId": 2,
  "duracaoDoPercurso": 60,
  "enderecoDeEntrega": "Avenida Campeche, 100",
  "pesoKg": 5,
  "valorDoPedido": 1000.99,
  "volumeM3": 5
}
```

<h3> Response body </h3>

```{
  "id": 6,
  "dataEntregaProgramada": "20/11/2022 12:00",
  "duracaoDoPercurso": 60,
  "dataProgramadaDaSaida": "20/11/2022 11:00",
  "dataConfirmacaoEntrega": "",
  "enderecoDeEntrega": "Avenida Campeche, 100",
  "status": "aberto",
  "descricaoPedido": "Jogo de pratos",
  "valorDoPedido": 1000.99,
  "droneId": 2,
  "pesoKg": 5,
  "volumeM3": 5,
  "latitude": null,
  "longitude": null
}
```


<h4> 3. Operação responsável por alterar um pedido <h4>
<h3>Request</h3>

`POST /dronefeeder/v1/pedido`

    curl -X PUT "http://localhost:8080/dronefeeder/v1/pedido/6" -H "accept: application/json" -H "Content-Type: application/json" -d "{ \"dataEntregaProgramada\": \"20/11/2022 12:00\", \"descricaoPedido\": \"Jogo de pratos\", \"droneId\": 4, \"duracaoDoPercurso\": 60, \"enderecoDeEntrega\": \"Avenida Campeche, 100\", \"pesoKg\": 5, \"valorDoPedido\": 1000.99, \"volumeM3\": 5}"
    
 <h3>Parameters</h3>
 ```{
  "dataEntregaProgramada": "20/11/2022 12:00",
  "descricaoPedido": "Jogo de pratos",
  "droneId": 5,
  "duracaoDoPercurso": 60,
  "enderecoDeEntrega": "Avenida Campeche, 100",
  "pesoKg": 4,
  "valorDoPedido": 1000.99,
  "volumeM3": 5
}
```

<h3> Response body </h3>

```{
  "id": 6,
  "dataEntregaProgramada": "20/11/2022 12:00",
  "duracaoDoPercurso": 60,
  "dataProgramadaDaSaida": "20/11/2022 11:00",
  "dataConfirmacaoEntrega": "",
  "enderecoDeEntrega": "Avenida Campeche, 100",
  "status": "aberto",
  "descricaoPedido": "Jogo de pratos",
  "valorDoPedido": 1000.99,
  "droneId": 4,
  "pesoKg": 5,
  "volumeM3": 5,
  "latitude": null,
  "longitude": null
}
```

<h4> 4. Operação responsável por deletar um pedido <h4>
<h3>Request</h3>

`DELETE /dronefeeder/v1/pedido/{id}`

    curl -X DELETE "http://localhost:8080/dronefeeder/v1/pedido/6" -H "accept: */*"

<h3> Response headers </h3>

``` cache-control: no-cacheno-storemax-age=0must-revalidate 
 connection: keep-alive 
 content-length: 0 
 date: Tue08 Nov 2022 22:55:17 GMT 
 expires: 0 
 keep-alive: timeout=60 
 pragma: no-cache 
 x-content-type-options: nosniff 
 x-frame-options: DENY 
 x-xss-protection: 1; mode=block 
```

<h4> 5. Operação responsável por alterar as coordenadas da entrega pelo drone. <h4>
<h3>Request</h3>

`PUT /dronefeeder/v1/pedido/atualizacoordenadas`

    curl -X PUT "http://localhost:8080/dronefeeder/v1/pedido/atualizacoordenadas" -H "accept: application/json" -H "Content-Type: application/json" -d "{ \"latitude\": 190, \"longitude\": 300, \"pedidoId\": 2}"
    
 <h3>Parameters</h3>
 ```{
  "latitude": 190,
  "longitude": 300,
  "pedidoId": 2
}
```

<h3> Response body </h3>

```{
  "id": 2,
  "dataEntregaProgramada": "05/11/2022 10:00",
  "duracaoDoPercurso": 3,
  "dataProgramadaDaSaida": "05/11/2022 09:57",
  "dataConfirmacaoEntrega": "",
  "enderecoDeEntrega": "Rua São João",
  "status": "em andamento",
  "descricaoPedido": "caixa de bombons",
  "valorDoPedido": 100,
  "droneId": 2,
  "pesoKg": 10,
  "volumeM3": 5,
  "latitude": 190,
  "longitude": 300
}
```

<h4> 6. Operação responsável por cancelar um pedido <h4>
<h3>Request</h3>

`PUT /dronefeeder/v1/pedido/cancelar/{id}`

    curl -X PUT "http://localhost:8080/dronefeeder/v1/pedido/cancelar/7" -H "accept: */*"

<h3> Response headers </h3>

``` cache-control: no-cacheno-storemax-age=0must-revalidate 
 connection: keep-alive 
 content-length: 0 
 date: Tue08 Nov 2022 23:08:41 GMT 
 expires: 0 
 keep-alive: timeout=60 
 pragma: no-cache 
 x-content-type-options: nosniff 
 x-frame-options: DENY 
 x-xss-protection: 1; mode=block  
```


<h4> 7. Operação responsável por listar os pedidos do drone <h4>
<h3>Request</h3>

`GET /dronefeeder/v1/pedido/drone/{id}`

    curl -X GET "http://localhost:8080/dronefeeder/v1/pedido/drone/2" -H "accept: application/json"

<h3> Response body </h3>

```[
	  {
	    "id": 7,
	    "dataEntregaProgramada": "20/11/2022 12:00",
	    "duracaoDoPercurso": 60,
	    "dataProgramadaDaSaida": "20/11/2022 11:00",
	    "dataConfirmacaoEntrega": "",
	    "enderecoDeEntrega": "Avenida Campeche, 100",
	    "status": "cancelado",
	    "descricaoPedido": "Jogo de pratos",
	    "valorDoPedido": 1000.99,
	    "droneId": 2,
	    "pesoKg": 5,
	    "volumeM3": 5,
	    "latitude": null,
	    "longitude": null
	  },
  ...
]
```

### * Video-controller

<h4> 1. Operação responsável por listar os vídeos enviados pelos drones<h4>
<h3>Request</h3>

`GET /dronefeeder/v1/video`

    curl -X GET "http://localhost:8080/dronefeeder/v1/video" -H "accept: application/json"

<h3> Response body </h3>

```[
    
    "id": 2,
    "nomeArquivo": "918c4141-def7-43b7-8708-d2ea6bbd8ada.mp4",
    "pedidoId": 5
  },
  ...
]
```

<h4> 2. Operação responsável por baixar o vídeo de um pedido<h4>
<h3>Request</h3>

`GET /dronefeeder/v1/video/download/{idPedido}`

    curl -X GET "http://localhost:8080/dronefeeder/v1/video/download/5" -H "accept: */*"

<h3> Response body </h3>

	[Download file]
	

<h4> 3. Operação responsável cadastrar o video enviado pelo drone. <h4>
<h3>Request</h3>

`PUT /dronefeeder/v1/pedido/atualizacoordenadas`

    curl -X PUT "http://localhost:8080/dronefeeder/v1/pedido/atualizacoordenadas" -H "accept: application/json" -H "Content-Type: application/json" -d "{ \"latitude\": 190, \"longitude\": 300, \"pedidoId\": 2}"
    
 <h3>Parameters</h3>
 ```{
  "latitude": 190,
  "longitude": 300,
  "pedidoId": 2
}
```

<h3> Response body </h3>

```{
  "id": 2,
  "dataEntregaProgramada": "05/11/2022 10:00",
  "duracaoDoPercurso": 3,
  "dataProgramadaDaSaida": "05/11/2022 09:57",
  "dataConfirmacaoEntrega": "",
  "enderecoDeEntrega": "Rua São João",
  "status": "em andamento",
  "descricaoPedido": "caixa de bombons",
  "valorDoPedido": 100,
  "droneId": 2,
  "pesoKg": 10,
  "volumeM3": 5,
  "latitude": 190,
  "longitude": 300
}
```
    
## Link para o swagger
http://localhost:8081/dronefeeder/swagger-ui/#/

## Link para o Heroku
https://dronefeeder-api.herokuapp.com/dronefeeder/swagger-ui/#/