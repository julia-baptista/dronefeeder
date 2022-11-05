package com.trybe.accjava.desafiofinal.dronefeeder.controller;

import java.util.List;
import javax.validation.Valid;
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
import com.trybe.accjava.desafiofinal.dronefeeder.dtos.AtualizaCoordenadaPedidoDto;
import com.trybe.accjava.desafiofinal.dronefeeder.dtos.PedidoDto;
import com.trybe.accjava.desafiofinal.dronefeeder.enums.StatusPedidoEnum;
import com.trybe.accjava.desafiofinal.dronefeeder.service.PedidoService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.log4j.Log4j2;

@Log4j2
@RestController
@RequestMapping("v1/pedido")
public class PedidoController {

  private PedidoService pedidoService;

  public PedidoController(PedidoService pedidoService) {
    this.pedidoService = pedidoService;
  }

  @ApiOperation(value = "Operação responsável por cadastrar um pedido", notes = "Cadastrar Pedido")
  @ApiResponses(value = {
      @ApiResponse(code = 200, message = "Pedido cadastrado com sucesso",
          response = PedidoDto.class),
      @ApiResponse(code = 401, message = "Não autorizado"),
      @ApiResponse(code = 500, message = "Erro inesperado"),
      @ApiResponse(code = 404, message = "Drone não encontrado"),
      @ApiResponse(code = 409, message = "Peso e/ou volume excedido, Data/Hora indisponível")})
  @PostMapping(consumes = {"application/json"}, produces = {"application/json"})
  public ResponseEntity<PedidoDto> cadastrarPedido(@RequestBody @Valid PedidoDto dto) {
    log.info("Requisição para cadastrar o pedido recebida.");
    PedidoDto novoPedido = pedidoService.cadastrar(dto);
    log.info(String.format("Pedido id [%s] cadastrado com sucesso.", novoPedido.getId()));
    return ResponseEntity.ok(novoPedido);
  }

  @ApiOperation(value = "Operação responsável por listar os pedidos", notes = "Listar Pedidos")
  @ApiResponses(
      value = {@ApiResponse(code = 200, message = "Lista de pedidos recuperada com sucesso"),
          @ApiResponse(code = 401, message = "Não autorizado"),
          @ApiResponse(code = 500, message = "Erro inesperado")})
  @GetMapping(produces = {"application/json"})
  public ResponseEntity<List<PedidoDto>> listarPedidos() {
    List<PedidoDto> lista = pedidoService.listar();
    return ResponseEntity.ok(lista);
  }

  @ApiOperation(value = "Operação responsável por listar os pedidos do drone",
      notes = "Listar Pedidos do drone")
  @ApiResponses(value = {
      @ApiResponse(code = 200, message = "Lista de pedidos do drone recuperadas com sucesso"),
      @ApiResponse(code = 401, message = "Não autorizado"),
      @ApiResponse(code = 500, message = "Erro inesperado")})
  @GetMapping(value = "/drone/{id}", produces = {"application/json"})
  public ResponseEntity<List<PedidoDto>> listarPedidosPorDrone(@PathVariable("id") Long id) {
    List<PedidoDto> lista = pedidoService.listarPorDrone(id);
    return ResponseEntity.ok(lista);
  }

  @ApiOperation(value = "Operação responsável por deletar um pedido.", notes = "Deleta pedido")
  @ApiResponses(value = {@ApiResponse(code = 202, message = "Pedido removido com sucesso"),
      @ApiResponse(code = 401, message = "Não autorizado"),
      @ApiResponse(code = 404, message = "Pedido não encontrado"),
      @ApiResponse(code = 500, message = "Erro inesperado")})
  @DeleteMapping(value = "/{id}")
  public ResponseEntity<Void> deletarPedido(@PathVariable("id") Long id) {
    this.pedidoService.deletar(id);
    return ResponseEntity.status(HttpStatus.ACCEPTED).body(null);
  }

  @ApiOperation(value = "Operação responsável por alterar um pedido.", notes = "Altera pedido")
  @ApiResponses(value = {@ApiResponse(code = 200, message = "Pedido alterado com sucesso"),
      @ApiResponse(code = 401, message = "Não autorizado"),
      @ApiResponse(code = 404, message = "Pedido não encontrado"),
      @ApiResponse(code = 500, message = "Erro inesperado"),
      @ApiResponse(code = 409,
          message = "Peso e/ou volume excedido, Data/Hora indisponível, Pedido cancelado, em andamento ou entregue, DroneInativo,")})

  @PutMapping(value = "/{id}", consumes = {"application/json"}, produces = {"application/json"})
  public ResponseEntity<PedidoDto> alterarPedido(@PathVariable("id") Long id,
      @RequestBody @Valid PedidoDto dto) {
    PedidoDto pedido = this.pedidoService.alterar(id, dto);
    return ResponseEntity.ok(pedido);
  }

  @ApiOperation(value = "Operação responsável por cancelar um pedido.", notes = "Cancelar pedido")
  @ApiResponses(value = {@ApiResponse(code = 202, message = "Pedido removido com sucesso"),
      @ApiResponse(code = 401, message = "Não autorizado"),
      @ApiResponse(code = 404, message = "Pedido não encontrado"),
      @ApiResponse(code = 500, message = "Erro inesperado")})
  @PutMapping(value = "/cancelar/{id}")
  public ResponseEntity<Void> cancelarPedido(@PathVariable("id") Long id) {
    this.pedidoService.alterarStatus(id, StatusPedidoEnum.CA);
    return ResponseEntity.status(HttpStatus.ACCEPTED).body(null);
  }

  @ApiOperation(value = "Operação responsável por atualizar as coodernadas da entrega pelo drone.",
      notes = "Atualiza as coordenadas do pedido")
  @ApiResponses(value = {@ApiResponse(code = 200, message = "Coordenada atualizada com sucesso"),
      @ApiResponse(code = 401, message = "Não autorizado"),
      @ApiResponse(code = 404, message = "Pedido não encontrado"),
      @ApiResponse(code = 500, message = "Erro inesperado")})
  @PutMapping(value = "/atualizacoordenadas", consumes = {"application/json"},
      produces = {"application/json"})
  public ResponseEntity<PedidoDto> atualizarCoordenadas(
      @RequestBody @Valid AtualizaCoordenadaPedidoDto dto) {
    PedidoDto pedidoAlterado = this.pedidoService.atualizarCoordenadas(dto);
    return ResponseEntity.ok(pedidoAlterado);
  }
}
