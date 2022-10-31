package com.trybe.accjava.desafiofinal.dronefeeder.exception;

public class DroneNaoEncontradoException extends RuntimeException {

  private static final long serialVersionUID = 1L;

  public DroneNaoEncontradoException() {
    super("Drone não encontrado");
  }

}
