package com.trybe.accjava.desafiofinal.dronefeeder.exception;

public class PesoExcedidoException extends RuntimeException {

  private static final long serialVersionUID = 1L;

  public PesoExcedidoException() {
    super("Peso Excedido");
  }

}
