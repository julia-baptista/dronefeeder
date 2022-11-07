package com.trybe.accjava.desafiofinal.dronefeeder.exception;

public class PedidoCanceladoException extends RuntimeException {

  private static final long serialVersionUID = 1L;

  public PedidoCanceladoException() {
    super("Pedido cancelado");
  }

}
