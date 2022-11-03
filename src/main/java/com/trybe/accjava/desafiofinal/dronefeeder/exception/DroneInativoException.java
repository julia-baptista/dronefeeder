package com.trybe.accjava.desafiofinal.dronefeeder.exception;

public class DroneInativoException extends RuntimeException {

  private static final long serialVersionUID = 1L;

  public DroneInativoException() {
    super("Drone inativo");
  }

}
