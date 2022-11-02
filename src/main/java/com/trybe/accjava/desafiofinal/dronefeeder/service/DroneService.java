package com.trybe.accjava.desafiofinal.dronefeeder.service;

import java.util.List;
import java.util.Optional;
import com.trybe.accjava.desafiofinal.dronefeeder.dtos.DroneDto;
import com.trybe.accjava.desafiofinal.dronefeeder.enums.StatusDroneEnum;
import com.trybe.accjava.desafiofinal.dronefeeder.exception.DroneExistenteException;
import com.trybe.accjava.desafiofinal.dronefeeder.exception.DroneNaoEncontradoException;
import com.trybe.accjava.desafiofinal.dronefeeder.exception.ErroInesperadoException;
import com.trybe.accjava.desafiofinal.dronefeeder.model.Drone;
import com.trybe.accjava.desafiofinal.dronefeeder.repository.DroneRepository;
import org.springframework.stereotype.Service;

@Service
public class DroneService {

  final DroneRepository droneRepository;

  public DroneService(DroneRepository droneRepository) {
    super();
    this.droneRepository = droneRepository;
  }

  /**
   * Cadastrar.
   */
  public DroneDto cadastrar(DroneDto dto) {

    try {
      if (this.droneRepository.existsByNome(dto.getNome())) {
        throw new DroneExistenteException();
      }
      Drone drone = this.droneRepository.save(converterDroneDtoParaDrone(dto));
      return converterDroneParaDroneDto(drone);
    } catch (Exception e) {
      if (e instanceof DroneExistenteException) {
        throw (DroneExistenteException) e;
      }
      throw new ErroInesperadoException();
    }
  }

  private Drone converterDroneDtoParaDrone(DroneDto dto) {
    Drone drone = new Drone(dto.getNome(), dto.getMarca(), dto.getFabricante(),
        dto.getAltitudeMax(), dto.getDuracaoBateria(), dto.getCapacidadeKg(), dto.getCapacidadeM3(),
        StatusDroneEnum.ATIVO);
    return drone;
  }

  private DroneDto converterDroneParaDroneDto(Drone drone) {
    return DroneDto.builder().id(drone.getId()).nome(drone.getNome()).marca(drone.getMarca())
        .fabricante(drone.getFabricante()).altitudeMax(drone.getAltitudeMax())
        .duracaoBateria(drone.getDuracaoBateria()).capacidadeKg(drone.getCapacidadeKg())
        .capacidadeM3(drone.getCapacidadeM3()).status(drone.getStatus().getStatus()).build();
  }

  /**
   * Listar.
   */
  public List<Drone> listar() {

    try {
      return droneRepository.findAll();
    } catch (Exception e) {
      throw new ErroInesperadoException();
    }

  }

  /**
   * Deletar.
   */
  public void deletar(Long id) {

    try {
      if (!this.droneRepository.existsById(id)) {
        throw new DroneNaoEncontradoException();
      }

      droneRepository.deleteById(id);

    } catch (Exception e) {
      if (e instanceof DroneNaoEncontradoException) {
        throw (DroneNaoEncontradoException) e;
      }
      throw new ErroInesperadoException();
    }

  }

  /**
   * Alterar o Drone.
   */
  public DroneDto alterar(Long id, DroneDto dto) {
    try {

      Optional<Drone> drone = this.droneRepository.findById(id);

      if (!drone.isPresent()) {
        throw new DroneNaoEncontradoException();
      }

      /**
       * altera para não precisar fazer duas condicionais.
       */
      // if (!dto.getNome().equals(drone.get().getNome())) {
      // if (this.droneRepository.existsByNome(dto.getNome())) {
      // throw new DroneExistenteException();
      // }
      // }
      if (!dto.getNome().equals(drone.get().getNome())
          && this.droneRepository.existsByNome(dto.getNome())) {
        throw new DroneExistenteException();
      }

      Drone droneParaAlterar = converterDroneDtoParaDrone(dto);
      droneParaAlterar.setId(id);
      droneParaAlterar.setStatus(drone.get().getStatus());
      this.droneRepository.save(droneParaAlterar);

      return converterDroneParaDroneDto(droneParaAlterar);
    } catch (Exception e) {
      if (e instanceof DroneNaoEncontradoException) {
        throw (DroneNaoEncontradoException) e;
      }

      if (e instanceof DroneExistenteException) {
        throw (DroneExistenteException) e;
      }

      throw new ErroInesperadoException();
    }
  }

  /**
   * Ativar e Desativar o Drone.
   */
  public void alterarStatus(Long id, StatusDroneEnum status) {

    try {

      Optional<Drone> drone = this.droneRepository.findById(id);

      if (!drone.isPresent()) {
        throw new DroneNaoEncontradoException();
      }

      drone.get().setStatus(status);

      this.droneRepository.save(drone.get());

    } catch (Exception e) {
      if (e instanceof DroneNaoEncontradoException) {
        throw (DroneNaoEncontradoException) e;
      }

      throw new ErroInesperadoException();
    }

  }



}


