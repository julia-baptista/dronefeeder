package com.trybe.accjava.desafiofinal.dronefeeder.enums;

public enum StatusPedidoEnum {

  AB("aberto"), EA("em andamento"), CA("cancelado"), AT("atrasado"), EN("entregue");

  private String status;

  private StatusPedidoEnum(String status) {
    this.status = status;
  }

  public String getStatus() {
    return status;
  }
}
