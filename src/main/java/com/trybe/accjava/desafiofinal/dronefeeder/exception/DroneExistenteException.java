package com.trybe.accjava.desafiofinal.dronefeeder.exception;

public class DroneExistenteException extends RuntimeException {

  private static final long serialVersionUID = 1L;

  public DroneExistenteException() {
    super("Drone Existente");
  }
}

