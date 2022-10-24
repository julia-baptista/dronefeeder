package com.trybe.accjava.desafiofinal.dronefeeder.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.trybe.accjava.desafiofinal.dronefeeder.dtos.DroneDTO;
import com.trybe.accjava.desafiofinal.dronefeeder.service.DroneService;

// https://spring.io/guides/tutorials/rest/
@RestController
@RequestMapping("/drone")
public class DroneController {

  private DroneService service;

  public DroneController(DroneService service) {
    this.service = service;
  }

  @PostMapping
  public ResponseEntity<DroneDTO> newDrone(@RequestBody DroneDTO dto) {
    DroneDTO response = service.cadastrar(dto);
    return ResponseEntity.ok(response);
  }
}
