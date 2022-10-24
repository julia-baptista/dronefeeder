package com.trybe.accjava.desafiofinal.dronefeeder.enums;

public enum StatusDroneEnum {

  ATIVO("Ativo"), INATIVO("Inativo");

  private String status;

  private StatusDroneEnum(String status) {
    this.status = status;
  }

  public String getStatus() {
    return status;
  }
}
