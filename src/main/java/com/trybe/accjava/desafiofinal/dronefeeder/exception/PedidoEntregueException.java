package com.trybe.accjava.desafiofinal.dronefeeder.exception;

public class PedidoEntregueException extends RuntimeException {

  private static final long serialVersionUID = 1L;

  public PedidoEntregueException() {
    super("Pedido entregue");
  }

}
