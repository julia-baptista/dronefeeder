package com.trybe.accjava.desafiofinal.dronefeeder.controller;

import java.util.List;
import javax.validation.Valid;
import com.trybe.accjava.desafiofinal.dronefeeder.dtos.DroneDtoEntrada;
import com.trybe.accjava.desafiofinal.dronefeeder.dtos.DroneDtoSaida;
import com.trybe.accjava.desafiofinal.dronefeeder.enums.StatusDroneEnum;
import com.trybe.accjava.desafiofinal.dronefeeder.service.DroneService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

// https://spring.io/guides/tutorials/rest/
@RestController
@RequestMapping("v1/drone")
public class DroneController {

  private DroneService service;

  public DroneController(DroneService service) {
    this.service = service;
  }

  // Explicação para usar o consumes = application/json
  // https://www.baeldung.com/spring-415-unsupported-mediatype#:~:text=The%20415%20(Unsupported%20Media%20Type,t%20supported%20by%20the%20API.
  // Como validar os campos do payload
  // https://www.baeldung.com/spring-boot-bean-validation
  @ApiOperation(value = "Operação responsável por cadastrar um drone", notes = "Cadastrar Drone")
  @ApiResponses(value = {
      @ApiResponse(code = 200, message = "Pedido cadastrado com sucesso",
          response = DroneDtoSaida.class),
      @ApiResponse(code = 401, message = "Não autorizado"),
      @ApiResponse(code = 500, message = "Erro inesperado"),
      @ApiResponse(code = 409, message = "Drone existente")})
  @PostMapping(consumes = {"application/json"}, produces = {"application/json"})
  public ResponseEntity<DroneDtoSaida> cadastrarDrone(@RequestBody @Valid DroneDtoEntrada dto) {
    DroneDtoSaida novoDrone = service.cadastrar(dto);
    return ResponseEntity.ok(novoDrone);
  }

  @ApiOperation(value = "Operação responsável por listar os drones", notes = "Listar Drones")
  @ApiResponses(
      value = {@ApiResponse(code = 200, message = "Lista de drones recuperada com sucesso"),
          @ApiResponse(code = 401, message = "Não autorizado"),
          @ApiResponse(code = 500, message = "Erro inesperado")})
  @GetMapping(produces = {"application/json"})
  public ResponseEntity<List<DroneDtoSaida>> listarDrones() {
    List<DroneDtoSaida> lista = service.listar();
    return ResponseEntity.ok(lista);
  }

  // Delete example
  // https://tedblob.com/deletemapping-spring-boot-example/
  @ApiOperation(value = "Operação responsável por deletar um drone.", notes = "Deleta drone")
  @ApiResponses(value = {@ApiResponse(code = 202, message = "Drone removido com sucesso"),
      @ApiResponse(code = 401, message = "Não autorizado"),
      @ApiResponse(code = 404, message = "Drone não encontrado"),
      @ApiResponse(code = 500, message = "Erro inesperado"),
      @ApiResponse(code = 409, message = "Drone possui pedidos")})
  @DeleteMapping(value = "/{id}")
  public ResponseEntity<Void> deletarDrone(@PathVariable("id") Long id) {
    this.service.deletar(id);
    return ResponseEntity.status(HttpStatus.ACCEPTED).body(null);
  }

  // Como validar os campos do payload
  // https://www.baeldung.com/spring-boot-bean-validation
  @ApiOperation(value = "Operação responsável por alterar dados de um drone",
      notes = "Alterar Drone")
  @ApiResponses(value = {
      @ApiResponse(code = 200, message = "Drone alterado com sucesso",
          response = DroneDtoSaida.class),
      @ApiResponse(code = 401, message = "Não autorizado"),
      @ApiResponse(code = 404, message = "Drone não encontrado"),
      @ApiResponse(code = 500, message = "Erro inesperado"),
      @ApiResponse(code = 409, message = "Drone existente")})
  @PutMapping(value = "/{id}", consumes = {"application/json"}, produces = {"application/json"})
  public ResponseEntity<DroneDtoSaida> alterarDrone(@PathVariable("id") Long id,
      @RequestBody @Valid DroneDtoEntrada dto) {
    DroneDtoSaida drone = this.service.alterar(id, dto);
    return ResponseEntity.ok(drone);
  }

  @ApiOperation(value = "Operação responsável por ativar um drone", notes = "Ativar Drone")
  @ApiResponses(value = {@ApiResponse(code = 202, message = "Drone ativado com sucesso"),
      @ApiResponse(code = 401, message = "Não autorizado"),
      @ApiResponse(code = 404, message = "Drone não encontrado"),
      @ApiResponse(code = 500, message = "Erro inesperado")})
  @PutMapping(value = "/ativar/{id}")
  public ResponseEntity<Void> ativarDrone(@PathVariable("id") Long id) {
    this.service.alterarStatus(id, StatusDroneEnum.ATIVO);
    return ResponseEntity.status(HttpStatus.ACCEPTED).body(null);
  }

  @ApiOperation(value = "Operação responsável por desativar um drone", notes = "Desativar Drone")
  @ApiResponses(value = {@ApiResponse(code = 202, message = "Drone desativado com sucesso"),
      @ApiResponse(code = 401, message = "Não autorizado"),
      @ApiResponse(code = 404, message = "Drone não encontrado"),
      @ApiResponse(code = 500, message = "Erro inesperado"),
      @ApiResponse(code = 409, message = "Pedido em andamento")})
  @PutMapping(value = "/inativar/{id}")
  public ResponseEntity<Void> inativarDrone(@PathVariable("id") Long id) {
    this.service.alterarStatus(id, StatusDroneEnum.INATIVO);
    return ResponseEntity.status(HttpStatus.ACCEPTED).body(null);
  }

}
