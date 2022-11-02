package com.trybe.accjava.desafiofinal.dronefeeder.controller;

import java.util.List;
import javax.validation.Valid;
import com.trybe.accjava.desafiofinal.dronefeeder.dtos.DroneDto;
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
  @PostMapping(consumes = {"application/json"}, produces = {"application/json"})
  public ResponseEntity<DroneDto> cadastrarDrone(@RequestBody @Valid DroneDto dto) {
    DroneDto novoDrone = service.cadastrar(dto);
    return ResponseEntity.ok(novoDrone);
  }

  @GetMapping(produces = {"application/json"})
  public ResponseEntity<List<DroneDto>> listarDrones() {
    List<DroneDto> lista = service.listar();
    return ResponseEntity.ok(lista);
  }

  // Delete example
  // https://tedblob.com/deletemapping-spring-boot-example/
  @DeleteMapping(value = "/{id}")
  public ResponseEntity<Void> deletarDrone(@PathVariable("id") Long id) {
    this.service.deletar(id);
    return ResponseEntity.status(HttpStatus.ACCEPTED).body(null);
  }

  // Como validar os campos do payload
  // https://www.baeldung.com/spring-boot-bean-validation
  @PutMapping(value = "/{id}", consumes = {"application/json"}, produces = {"application/json"})
  public ResponseEntity<DroneDto> alterarDrone(@PathVariable("id") Long id,
      @RequestBody @Valid DroneDto dto) {
    DroneDto drone = this.service.alterar(id, dto);
    return ResponseEntity.ok(drone);
  }

  @PutMapping(value = "/ativar/{id}")
  public ResponseEntity<Void> ativarDrone(@PathVariable("id") Long id) {
    this.service.alterarStatus(id, StatusDroneEnum.ATIVO);
    return ResponseEntity.status(HttpStatus.ACCEPTED).body(null);
  }

  @PutMapping(value = "/inativar/{id}")
  public ResponseEntity<Void> inativarDrone(@PathVariable("id") Long id) {
    this.service.alterarStatus(id, StatusDroneEnum.INATIVO);
    return ResponseEntity.status(HttpStatus.ACCEPTED).body(null);
  }

}
