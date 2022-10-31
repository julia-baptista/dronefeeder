package com.trybe.accjava.desafiofinal.dronefeeder.controller;

import java.util.List;
import javax.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.trybe.accjava.desafiofinal.dronefeeder.dtos.PedidoDto;
import com.trybe.accjava.desafiofinal.dronefeeder.model.Pedido;
import com.trybe.accjava.desafiofinal.dronefeeder.service.PedidoService;

@RestController
@RequestMapping("/pedido")
public class PedidoController {

  private PedidoService pedidoService;

  public PedidoController(PedidoService pedidoService) {
    this.pedidoService = pedidoService;
  }

  @PostMapping(consumes = {"application/json"}, produces = {"application/json"})
  public ResponseEntity<PedidoDto> cadastrarPedido(@RequestBody @Valid PedidoDto dto) {
    PedidoDto novoPedido = pedidoService.cadastrar(dto);
    return ResponseEntity.ok(novoPedido);
  }

  @GetMapping
  public ResponseEntity<List<Pedido>> listarPedidos() {
    List<Pedido> lista = pedidoService.listar();
    return ResponseEntity.ok(lista);
  }

  @DeleteMapping(value = "/{id}")
  public ResponseEntity<Void> deletarDrone(@PathVariable("id") Long id) {
    this.pedidoService.deletar(id);
    return ResponseEntity.status(HttpStatus.ACCEPTED).body(null);
  }

}
