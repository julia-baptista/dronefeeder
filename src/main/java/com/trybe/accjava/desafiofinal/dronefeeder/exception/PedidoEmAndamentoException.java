package com.trybe.accjava.desafiofinal.dronefeeder.exception;

public class PedidoEmAndamentoException extends RuntimeException {

  private static final long serialVersionUID = 1L;

  public PedidoEmAndamentoException() {
    super("Pedido entregue");
  }

}
