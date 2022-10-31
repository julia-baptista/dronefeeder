package com.trybe.accjava.desafiofinal.dronefeeder.service;

import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import com.trybe.accjava.desafiofinal.dronefeeder.dtos.PedidoDto;
import com.trybe.accjava.desafiofinal.dronefeeder.enums.StatusPedidoEnum;
import com.trybe.accjava.desafiofinal.dronefeeder.exception.DroneNaoEncontradoException;
import com.trybe.accjava.desafiofinal.dronefeeder.exception.ErroInesperadoException;
import com.trybe.accjava.desafiofinal.dronefeeder.exception.PedidoNaoEncontradoException;
import com.trybe.accjava.desafiofinal.dronefeeder.model.Drone;
import com.trybe.accjava.desafiofinal.dronefeeder.model.Pedido;
import com.trybe.accjava.desafiofinal.dronefeeder.repository.DroneRepository;
import com.trybe.accjava.desafiofinal.dronefeeder.repository.PedidoRepository;

@Service
public class PedidoService {

  final PedidoRepository pedidoRepository;
  final DroneRepository droneRepository;

  public PedidoService(PedidoRepository pedidoRepository, DroneRepository droneRepository) {
    super();
    this.pedidoRepository = pedidoRepository;
    this.droneRepository = droneRepository;

  }

  /**
   * Cadastrar.
   */
  public PedidoDto cadastrar(PedidoDto dto) {

    try {

      Optional<Drone> drone = droneRepository.findById(dto.getDroneId());

      if (!drone.isPresent()) {
        throw new DroneNaoEncontradoException();
      }



      Pedido novoPedido = converterPedidoDtoParaPedido(dto);
      novoPedido.setDrone(drone.get());

      Pedido pedidoCadastrado = this.pedidoRepository.save(novoPedido);

      return converterPedidoParaPedidoDto(pedidoCadastrado);
    } catch (Exception e) {
      if (e instanceof DroneNaoEncontradoException) {
        throw (DroneNaoEncontradoException) e;
      }

      throw new ErroInesperadoException();
    }
  }

  private Pedido converterPedidoDtoParaPedido(PedidoDto dto) {
    Pedido pedido = new Pedido(dto.getDataEntregaProgramada(), dto.getDuracaoDoPercurso(),
        dto.getDataProgramadaDaSaida(), dto.getDataConfirmacaoEntrega(), StatusPedidoEnum.AB,
        dto.getDescricaoPedido(), dto.getValorDoPedido(), dto.getPesoKg(), dto.getVolumeM3(),
        dto.getLatitude(), dto.getLongitude(), null);
    return pedido;
  }

  private PedidoDto converterPedidoParaPedidoDto(Pedido pedido) {
    return PedidoDto.builder().id(pedido.getId())
        .dataEntregaProgramada(pedido.getDataEntregaProgramada())
        .duracaoDoPercurso(pedido.getDuracaoDoPercurso())
        .dataProgramadaDaSaida(pedido.getDataProgramadaDaSaida())
        .dataConfirmacaoEntrega(pedido.getDataConfirmacaoEntrega())
        .status(pedido.getStatus().getStatus()).descricaoPedido(pedido.getDescricaoPedido())
        .valorDoPedido(pedido.getValorDoPedido()).droneId(pedido.getDrone().getId())
        .pesoKg(pedido.getPesoKg()).volumeM3(pedido.getVolumeM3()).latitude(pedido.getLatitude())
        .longitude(pedido.getLongitude()).build();

  }

  /**
   * Listar.
   */
  public List<Pedido> listar() {

    try {
      return pedidoRepository.findAll();

    } catch (Exception e) {
      throw new ErroInesperadoException();
    }

  }

  /**
   * Deletar.
   */
  public void deletar(Long id) {

    try {
      if (!this.pedidoRepository.existsById(id)) {
        throw new PedidoNaoEncontradoException();
      }

      pedidoRepository.deleteById(id);

    } catch (Exception e) {
      if (e instanceof PedidoNaoEncontradoException) {
        throw (PedidoNaoEncontradoException) e;
      }

      throw new ErroInesperadoException();
    }

  }



}
