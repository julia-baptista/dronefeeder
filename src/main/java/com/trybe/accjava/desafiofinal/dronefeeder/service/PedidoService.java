package com.trybe.accjava.desafiofinal.dronefeeder.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.transaction.Transactional;
import org.springframework.stereotype.Service;
import com.trybe.accjava.desafiofinal.dronefeeder.dtos.AtualizaCoordenadaPedidoDto;
import com.trybe.accjava.desafiofinal.dronefeeder.dtos.PedidoDto;
import com.trybe.accjava.desafiofinal.dronefeeder.enums.StatusPedidoEnum;
import com.trybe.accjava.desafiofinal.dronefeeder.exception.DroneNaoEncontradoException;
import com.trybe.accjava.desafiofinal.dronefeeder.exception.ErroInesperadoException;
import com.trybe.accjava.desafiofinal.dronefeeder.exception.HorarioDoPedidoSobrepostoException;
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
  public PedidoDto cadastrar(PedidoDto dto) {

    try {

      Optional<Drone> drone = droneRepository.findById(dto.getDroneId());

      if (!drone.isPresent()) {
        throw new DroneNaoEncontradoException();
      }

      if (dto.getPesoKg() > drone.get().getCapacidadeKg()) {
        throw new PesoExcedidoException();
      }

      if (dto.getVolumeM3() > drone.get().getCapacidadeM3()) {
        throw new VolumeExcedidoException();
      }

      // dataEntregaProgramada
      // duracaoDoPercurso
      // dataProgramadaDaSaida

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
          converterPedidoDtoParaPedido(dto, dataEntregaProgramada, dataProgramadaDaSaida);
      novoPedido.setDrone(drone.get());

      Pedido pedidoCadastrado = this.pedidoRepository.save(novoPedido);

      return converterPedidoParaPedidoDto(pedidoCadastrado);
    } catch (Exception e) {
      log.error(String.format("Erro ao cadastrar o pedido. ErrorMessage: [%s]", e.getMessage()), e);
      if (e instanceof DroneNaoEncontradoException) {
        throw (DroneNaoEncontradoException) e;
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


  private Pedido converterPedidoDtoParaPedido(PedidoDto dto, LocalDateTime dataEntregaProgramada,
      LocalDateTime dataProgramadaDaSaida) {
    Pedido pedido = new Pedido(dataEntregaProgramada, dto.getDuracaoDoPercurso(),
        dataProgramadaDaSaida, DataUtil.converteStringParaData(dto.getDataConfirmacaoEntrega()),
        dto.getEnderecoDeEntrega(), StatusPedidoEnum.AB, dto.getDescricaoPedido(),
        dto.getValorDoPedido(), dto.getPesoKg(), dto.getVolumeM3(), dto.getLatitude(),
        dto.getLongitude(), null);
    return pedido;
  }



  private PedidoDto converterPedidoParaPedidoDto(Pedido pedido) {
    return PedidoDto.builder().id(pedido.getId())
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
  public List<PedidoDto> listar() {
    try {
      List<PedidoDto> pedidosDto = new ArrayList<PedidoDto>();
      List<Pedido> pedidos = pedidoRepository.findAll();
      pedidos.stream().forEach(pedido -> {
        pedidosDto.add(converterPedidoParaPedidoDto(pedido));
      });
      return pedidosDto;
    } catch (Exception e) {
      throw new ErroInesperadoException();
    }
  }

  /**
   * Listar pedidos por Drone.
   */
  public List<PedidoDto> listarPorDrone(Long droneId) {
    try {
      List<PedidoDto> pedidosDto = new ArrayList<PedidoDto>();
      List<Pedido> pedidos = pedidoRepository.findAll().stream()
          .filter(p -> p.getDrone().getId().equals(droneId)).collect(Collectors.toList());
      pedidos.stream().forEach(pedido -> {
        pedidosDto.add(converterPedidoParaPedidoDto(pedido));
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
  public PedidoDto atualizarCoordenadas(AtualizaCoordenadaPedidoDto dto) {

    try {

      Optional<Pedido> pedido = this.pedidoRepository.findById(dto.getPedidoId());

      if (!pedido.isPresent()) {
        throw new PedidoNaoEncontradoException();
      }

      pedido.get().setLatitude(dto.getLatitude());
      pedido.get().setLongitude(dto.getLongitude());

      this.pedidoRepository.save(pedido.get());

      return converterPedidoParaPedidoDto(pedido.get());

    } catch (Exception e) {
      if (e instanceof PedidoNaoEncontradoException) {
        throw (PedidoNaoEncontradoException) e;
      }

      throw new ErroInesperadoException();
    }

  }

  public List<Pedido> listaPedidosPorStatus(List<StatusPedidoEnum> status) {
    return this.pedidoRepository.findByStatusIn(status);
  }
}
