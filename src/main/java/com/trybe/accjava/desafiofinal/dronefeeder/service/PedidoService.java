package com.trybe.accjava.desafiofinal.dronefeeder.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.transaction.Transactional;
import org.springframework.stereotype.Service;
import com.trybe.accjava.desafiofinal.dronefeeder.dtos.AtualizaCoordenadaPedidoDto;
import com.trybe.accjava.desafiofinal.dronefeeder.dtos.PedidoDtoEntrada;
import com.trybe.accjava.desafiofinal.dronefeeder.dtos.PedidoDtoSaida;
import com.trybe.accjava.desafiofinal.dronefeeder.enums.StatusDroneEnum;
import com.trybe.accjava.desafiofinal.dronefeeder.enums.StatusPedidoEnum;
import com.trybe.accjava.desafiofinal.dronefeeder.exception.DroneInativoException;
import com.trybe.accjava.desafiofinal.dronefeeder.exception.DroneNaoEncontradoException;
import com.trybe.accjava.desafiofinal.dronefeeder.exception.ErroInesperadoException;
import com.trybe.accjava.desafiofinal.dronefeeder.exception.HorarioDoPedidoSobrepostoException;
import com.trybe.accjava.desafiofinal.dronefeeder.exception.PedidoCanceladoException;
import com.trybe.accjava.desafiofinal.dronefeeder.exception.PedidoEmAndamentoException;
import com.trybe.accjava.desafiofinal.dronefeeder.exception.PedidoEntregueException;
import com.trybe.accjava.desafiofinal.dronefeeder.exception.PedidoNaoEncontradoException;
import com.trybe.accjava.desafiofinal.dronefeeder.exception.PesoExcedidoException;
import com.trybe.accjava.desafiofinal.dronefeeder.exception.VolumeExcedidoException;
import com.trybe.accjava.desafiofinal.dronefeeder.model.Drone;
import com.trybe.accjava.desafiofinal.dronefeeder.model.Pedido;
import com.trybe.accjava.desafiofinal.dronefeeder.repository.DroneRepository;
import com.trybe.accjava.desafiofinal.dronefeeder.repository.PedidoRepository;
import com.trybe.accjava.desafiofinal.dronefeeder.util.DataUtil;
import lombok.extern.log4j.Log4j2;

@Log4j2
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
  // Usar transactional para solucionar erro: failed to lazily initialize a collection of role:
  // https://stackoverflow.com/questions/11746499/how-to-solve-the-failed-to-lazily-initialize-a-collection-of-role-hibernate-ex
  @Transactional
  public PedidoDtoSaida cadastrar(PedidoDtoEntrada dto) {

    try {

      Optional<Drone> drone = droneRepository.findById(dto.getDroneId());

      if (!drone.isPresent()) {
        throw new DroneNaoEncontradoException();
      }

      if (drone.get().getStatus().equals(StatusDroneEnum.INATIVO)) {
        throw new DroneInativoException();
      }

      if (dto.getPesoKg() > drone.get().getCapacidadeKg()) {
        throw new PesoExcedidoException();
      }

      if (dto.getVolumeM3() > drone.get().getCapacidadeM3()) {
        throw new VolumeExcedidoException();
      }

      LocalDateTime dataEntregaProgramada =
          DataUtil.converteStringParaData(dto.getDataEntregaProgramada());

      LocalDateTime dataProgramadaDaSaida =
          DataUtil.geraDataSaidaProgramada(dto.getDuracaoDoPercurso(), dataEntregaProgramada);


      boolean isDataHoraProgramaSobreposta = drone.get().getPedidos().stream()
          .anyMatch(p -> DataUtil.isDataHoraPedidoSobreposta(p.getDataProgramadaDaSaida(),
              p.getDataEntregaProgramada(), dataProgramadaDaSaida, dataEntregaProgramada));

      if (isDataHoraProgramaSobreposta) {
        throw new HorarioDoPedidoSobrepostoException();
      }


      Pedido novoPedido =
          converterPedidoDtoEntradaParaPedido(dto, dataEntregaProgramada, dataProgramadaDaSaida);

      novoPedido.setDrone(drone.get());

      Pedido pedidoCadastrado = this.pedidoRepository.save(novoPedido);

      return converterPedidoParaPedidoDtoSaida(pedidoCadastrado);
    } catch (Exception e) {
      log.error(String.format("Erro ao cadastrar o pedido. ErrorMessage: [%s]", e.getMessage()), e);
      if (e instanceof DroneNaoEncontradoException) {
        throw (DroneNaoEncontradoException) e;
      }

      if (e instanceof DroneInativoException) {
        throw (DroneInativoException) e;
      }

      if (e instanceof PesoExcedidoException) {
        throw (PesoExcedidoException) e;
      }

      if (e instanceof VolumeExcedidoException) {
        throw (VolumeExcedidoException) e;
      }

      if (e instanceof HorarioDoPedidoSobrepostoException) {
        throw (HorarioDoPedidoSobrepostoException) e;
      }

      throw new ErroInesperadoException();
    }
  }


  private Pedido converterPedidoDtoEntradaParaPedido(PedidoDtoEntrada dto,
      LocalDateTime dataEntregaProgramada, LocalDateTime dataProgramadaDaSaida) {
    Pedido pedido =
        new Pedido(dataEntregaProgramada, dto.getDuracaoDoPercurso(), dataProgramadaDaSaida, null,
            dto.getEnderecoDeEntrega(), StatusPedidoEnum.AB, dto.getDescricaoPedido(),
            dto.getValorDoPedido(), dto.getPesoKg(), dto.getVolumeM3(), null, null, null);
    return pedido;
  }


  private PedidoDtoSaida converterPedidoParaPedidoDtoSaida(Pedido pedido) {
    return PedidoDtoSaida.builder().id(pedido.getId())
        .dataEntregaProgramada(DataUtil.converteDataParaString(pedido.getDataEntregaProgramada()))
        .duracaoDoPercurso(pedido.getDuracaoDoPercurso())
        .dataProgramadaDaSaida(DataUtil.converteDataParaString(pedido.getDataProgramadaDaSaida()))
        .dataConfirmacaoEntrega(DataUtil.converteDataParaString(pedido.getDataConfirmacaoEntrega()))
        .enderecoDeEntrega(pedido.getEnderecoDeEntrega()).status(pedido.getStatus().getStatus())
        .descricaoPedido(pedido.getDescricaoPedido()).valorDoPedido(pedido.getValorDoPedido())
        .droneId(pedido.getDrone().getId()).pesoKg(pedido.getPesoKg())
        .volumeM3(pedido.getVolumeM3()).latitude(pedido.getLatitude())
        .longitude(pedido.getLongitude()).build();

  }


  /**
   * Listar.
   */
  public List<PedidoDtoSaida> listar() {
    try {
      List<PedidoDtoSaida> pedidosDto = new ArrayList<PedidoDtoSaida>();
      List<Pedido> pedidos = pedidoRepository.findAll();
      pedidos.stream().forEach(pedido -> {
        pedidosDto.add(converterPedidoParaPedidoDtoSaida(pedido));
      });
      return pedidosDto;
    } catch (Exception e) {
      throw new ErroInesperadoException();
    }
  }

  /**
   * Listar pedidos por Drone.
   */
  public List<PedidoDtoSaida> listarPorDrone(Long droneId) {
    try {
      List<PedidoDtoSaida> pedidosDto = new ArrayList<PedidoDtoSaida>();
      List<Pedido> pedidos = pedidoRepository.findAll().stream()
          .filter(p -> p.getDrone().getId().equals(droneId)).collect(Collectors.toList());
      pedidos.stream().forEach(pedido -> {
        pedidosDto.add(converterPedidoParaPedidoDtoSaida(pedido));
      });
      return pedidosDto;
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

  /**
   * Alterar o Pedido.
   */
  @Transactional
  public PedidoDtoSaida alterar(Long id, PedidoDtoEntrada dto) {
    try {

      Optional<Pedido> pedido = this.pedidoRepository.findById(id);

      if (!pedido.isPresent()) {
        throw new PedidoNaoEncontradoException();
      }

      if (pedido.get().getStatus().equals(StatusPedidoEnum.CA)) {
        throw new PedidoCanceladoException();
      }

      if (pedido.get().getStatus().equals(StatusPedidoEnum.EA)
          || pedido.get().getStatus().equals(StatusPedidoEnum.AT)) {
        throw new PedidoEmAndamentoException();
      }

      if (pedido.get().getStatus().equals(StatusPedidoEnum.EN)) {
        throw new PedidoEntregueException();
      }

      Optional<Drone> drone = droneRepository.findById(dto.getDroneId());

      if (drone.get().getStatus().equals(StatusDroneEnum.INATIVO)) {
        throw new DroneInativoException();
      }

      if (dto.getPesoKg() > drone.get().getCapacidadeKg()) {
        throw new PesoExcedidoException();
      }

      if (dto.getVolumeM3() > drone.get().getCapacidadeM3()) {
        throw new VolumeExcedidoException();
      }

      LocalDateTime dataEntregaProgramada =
          DataUtil.converteStringParaData(dto.getDataEntregaProgramada());

      LocalDateTime dataProgramadaDaSaida =
          DataUtil.geraDataSaidaProgramada(dto.getDuracaoDoPercurso(), dataEntregaProgramada);

      boolean isDataHoraProgramaSobreposta =
          drone.get().getPedidos().stream().filter(p -> !p.getId().equals(id))
              .anyMatch(p -> DataUtil.isDataHoraPedidoSobreposta(p.getDataProgramadaDaSaida(),
                  p.getDataEntregaProgramada(), dataProgramadaDaSaida, dataEntregaProgramada));

      if (isDataHoraProgramaSobreposta) {
        throw new HorarioDoPedidoSobrepostoException();
      }

      Pedido novoPedido =
          converterPedidoDtoEntradaParaPedido(dto, dataEntregaProgramada, dataProgramadaDaSaida);
      novoPedido.setDrone(drone.get());

      novoPedido.setId(id);

      Pedido pedidoCadastrado = this.pedidoRepository.save(novoPedido);

      return converterPedidoParaPedidoDtoSaida(pedidoCadastrado);


    } catch (Exception e) {
      if (e instanceof PedidoNaoEncontradoException) {
        throw (PedidoNaoEncontradoException) e;
      }

      if (e instanceof PedidoCanceladoException) {
        throw (PedidoCanceladoException) e;
      }

      if (e instanceof PedidoEmAndamentoException) {
        throw (PedidoEmAndamentoException) e;
      }

      if (e instanceof PedidoEntregueException) {
        throw (PedidoEntregueException) e;
      }

      if (e instanceof DroneInativoException) {
        throw (DroneInativoException) e;
      }

      if (e instanceof PesoExcedidoException) {
        throw (PesoExcedidoException) e;
      }

      if (e instanceof VolumeExcedidoException) {
        throw (VolumeExcedidoException) e;
      }

      if (e instanceof HorarioDoPedidoSobrepostoException) {
        throw (HorarioDoPedidoSobrepostoException) e;
      }

      throw new ErroInesperadoException();
    }
  }

  /**
   * Alterar Status.
   */
  public void alterarStatus(Long id, StatusPedidoEnum status) {

    try {

      Optional<Pedido> pedido = this.pedidoRepository.findById(id);

      if (!pedido.isPresent()) {
        throw new PedidoNaoEncontradoException();
      }

      pedido.get().setStatus(status);

      this.pedidoRepository.save(pedido.get());

    } catch (Exception e) {
      if (e instanceof PedidoNaoEncontradoException) {
        throw (PedidoNaoEncontradoException) e;
      }

      throw new ErroInesperadoException();
    }

  }

  /**
   * Alterar Latitude.
   */
  public PedidoDtoSaida atualizarCoordenadas(AtualizaCoordenadaPedidoDto dto) {

    try {

      Optional<Pedido> pedido = this.pedidoRepository.findById(dto.getPedidoId());

      if (!pedido.isPresent()) {
        throw new PedidoNaoEncontradoException();
      }

      if (StatusPedidoEnum.EN.equals(pedido.get().getStatus())) {
        throw new PedidoEntregueException();
      }

      pedido.get().setLatitude(dto.getLatitude());
      pedido.get().setLongitude(dto.getLongitude());

      this.pedidoRepository.save(pedido.get());

      return converterPedidoParaPedidoDtoSaida(pedido.get());

    } catch (Exception e) {
      if (e instanceof PedidoNaoEncontradoException) {
        throw (PedidoNaoEncontradoException) e;
      }

      if (e instanceof PedidoEntregueException) {
        throw (PedidoEntregueException) e;
      }

      throw new ErroInesperadoException();
    }

  }

  /**
   * Listar pedidos por status.
   */
  public List<Pedido> listaPedidosPorStatus(List<StatusPedidoEnum> status) {
    return this.pedidoRepository.findByStatusIn(status);
  }


}
