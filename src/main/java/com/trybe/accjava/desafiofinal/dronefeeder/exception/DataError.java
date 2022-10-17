package com.trybe.accjava.desafiofinal.dronefeeder.exception;

public class DataError {

  /**
   * Atributos.
   **/
  private String error;

  /**
   * Construtor da classe DataError.
   */
  public DataError(String error) {
    this.error = error;
  }

  public DataError() {}

  /**
   * Métodos.
   **/
  public String getError() {
    return error;
  }

  public void setError(String error) {
    this.error = error;
  }
}
