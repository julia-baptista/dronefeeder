package com.trybe.accjava.desafiofinal.dronefeeder.model;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;

@Entity
public class Video {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String nomeArquivo;

  @OneToOne(mappedBy = "video", cascade = CascadeType.ALL, orphanRemoval = true,
      fetch = FetchType.LAZY)
  private Pedido pedido;


  public Video() {
    super();
  }

  public Video(Long id, String nomeArquivo, Pedido pedido) {
    super();
    this.id = id;
    this.nomeArquivo = nomeArquivo;
    this.pedido = pedido;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getNomeArquivo() {
    return nomeArquivo;
  }

  public void setNomeArquivo(String nomeArquivo) {
    this.nomeArquivo = nomeArquivo;
  }

  public Pedido getPedido() {
    return pedido;
  }

  public void setPedido(Pedido pedido) {
    this.pedido = pedido;
  }

}
