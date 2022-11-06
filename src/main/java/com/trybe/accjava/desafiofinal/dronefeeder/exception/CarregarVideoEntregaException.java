package com.trybe.accjava.desafiofinal.dronefeeder.exception;

public class CarregarVideoEntregaException extends RuntimeException {

  private static final long serialVersionUID = 1L;

  public CarregarVideoEntregaException() {
    super("Erro ao carregar o arquivo!");
  }
}
