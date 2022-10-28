package com.trybe.accjava.desafiofinal.dronefeeder.service;

import java.util.List;
import org.springframework.stereotype.Service;
import com.trybe.accjava.desafiofinal.dronefeeder.dtos.DroneDTO;
import com.trybe.accjava.desafiofinal.dronefeeder.enums.StatusDroneEnum;
import com.trybe.accjava.desafiofinal.dronefeeder.exception.DroneExistenteException;
import com.trybe.accjava.desafiofinal.dronefeeder.exception.ErroInesperadoException;
import com.trybe.accjava.desafiofinal.dronefeeder.model.Drone;
import com.trybe.accjava.desafiofinal.dronefeeder.repository.DroneRepository;

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
  public DroneDTO cadastrar(DroneDTO dto) {

    try {
      if (this.droneRepository.existsByNome(dto.getNome())) {
        throw new DroneExistenteException();
      }
      Drone drone = this.droneRepository.save(convertDroneDtoToDrone(dto));
      return convertDroneToDroneDTO(drone);
    } catch (Exception e) {
      if (e instanceof DroneExistenteException) {
        throw (DroneExistenteException) e;
      }
      throw new ErroInesperadoException();
    }
  }

  private Drone convertDroneDtoToDrone(DroneDTO dto) {
    Drone drone = new Drone(dto.getNome(), dto.getMarca(), dto.getFabricante(),
        dto.getAltitudeMax(), dto.getDuracaoBateria(), dto.getCapacidadeKg(), dto.getCapacidadeM3(),
        StatusDroneEnum.ATIVO);
    return drone;
  }

  private DroneDTO convertDroneToDroneDTO(Drone drone) {
    return DroneDTO.builder().id(drone.getId()).nome(drone.getNome()).marca(drone.getMarca())
        .fabricante(drone.getFabricante()).altitudeMax(drone.getAltitudeMax())
        .duracaoBateria(drone.getDuracaoBateria()).capacidadeKg(drone.getCapacidadeKg())
        .capacidadeM3(drone.getCapacidadeM3()).status(drone.getStatus().getStatus()).build();
  }

  /**
   * Método listarSeries.
   */
  public List<Drone> listar() {

    try {
      return droneRepository.findAll();

    } catch (Exception e) {
      throw new ErroInesperadoException();
    }

  }
}
