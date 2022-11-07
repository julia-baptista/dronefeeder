package com.trybe.accjava.desafiofinal.dronefeeder.exception;

public class HorarioDoPedidoSobrepostoException extends RuntimeException {

  private static final long serialVersionUID = 1L;

  public HorarioDoPedidoSobrepostoException() {
    super("DataHora do pedido inválida.");
  }

}
