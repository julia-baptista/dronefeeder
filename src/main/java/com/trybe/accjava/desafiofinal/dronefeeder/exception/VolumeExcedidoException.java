package com.trybe.accjava.desafiofinal.dronefeeder.exception;

public class VolumeExcedidoException extends RuntimeException {

  private static final long serialVersionUID = 1L;

  public VolumeExcedidoException() {
    super("Volume Excedido");
  }

}
