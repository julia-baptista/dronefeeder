package com.trybe.accjava.desafiofinal.dronefeeder.exception;

public class PedidoEmAbertoException extends RuntimeException {

  private static final long serialVersionUID = 1L;

  public PedidoEmAbertoException() {
    super("Existem pedidos em aberto.");
  }

}
