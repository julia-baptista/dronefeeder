package com.trybe.accjava.desafiofinal.dronefeeder.service;

import org.springframework.stereotype.Service;
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
  public Drone cadastrar(Drone drone) {

    try {
      if (this.droneRepository.existsByNome(drone.getNome())) {
        throw new DroneExistenteException();
      }
      return this.droneRepository.save(drone);
    } catch (Exception e) {
      if (e instanceof DroneExistenteException) {
        throw (DroneExistenteException) e;
      }
      throw new ErroInesperadoException();
    }
  }
}
