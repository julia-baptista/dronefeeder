package com.trybe.accjava.desafiofinal.dronefeeder.exception;

public class DronePossuiPedidosException extends RuntimeException {

  private static final long serialVersionUID = 1L;

  public DronePossuiPedidosException() {
    super(
        "Este drone possui pedidos. Ele não poderá ser deletado. Verifique a possibilidade de inativá-lo.");
  }

}
