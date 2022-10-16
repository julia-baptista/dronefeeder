package com.trybe.accjava.desafiofinal.dronefeeder.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.trybe.accjava.desafiofinal.dronefeeder.enums.StatusPedidoEnum;

@Entity
public class Pedido {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  private LocalDateTime dataEntregaProgramada;
  private Integer duracaoDoPercurso;
  private LocalDateTime dataProgramadaDaSaida;
  private LocalDateTime dataConfirmacaoEntrega;

  @Enumerated(EnumType.STRING)
  private StatusPedidoEnum status;

  private String descricaoPedido;
  private BigDecimal valorDoPedido;

  @ManyToOne
  @JoinColumn(name = "drone_id")
  private Drone drone;

  private Double pesoKg;
  private Double volumeM3;
  private Integer latitudeDestino;
  private Integer longitudeDestino;
  private Integer latitudeAtual;
  private Integer longitudeAtual;

  @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
  @JoinColumn(name = "video_id", referencedColumnName = "id")
  private Video video;

  public Pedido() {}


  public Long getId() {
    return id;
  }


  public void setId(Long id) {
    this.id = id;
  }


  public LocalDateTime getDataEntregaProgramada() {
    return dataEntregaProgramada;
  }


  public void setDataEntregaProgramada(LocalDateTime dataEntregaProgramada) {
    this.dataEntregaProgramada = dataEntregaProgramada;
  }


  public Integer getDuracaoDoPercurso() {
    return duracaoDoPercurso;
  }


  public void setDuracaoDoPercurso(Integer duracaoDoPercurso) {
    this.duracaoDoPercurso = duracaoDoPercurso;
  }


  public LocalDateTime getDataProgramadaDaSaida() {
    return dataProgramadaDaSaida;
  }


  public void setDataProgramadaDaSaida(LocalDateTime dataProgramadaDaSaida) {
    this.dataProgramadaDaSaida = dataProgramadaDaSaida;
  }


  public LocalDateTime getDataConfirmacaoEntrega() {
    return dataConfirmacaoEntrega;
  }


  public void setDataConfirmacaoEntrega(LocalDateTime dataConfirmacaoEntrega) {
    this.dataConfirmacaoEntrega = dataConfirmacaoEntrega;
  }


  public StatusPedidoEnum getStatus() {
    return status;
  }


  public void setStatus(StatusPedidoEnum status) {
    this.status = status;
  }


  public String getDescricaoPedido() {
    return descricaoPedido;
  }


  public void setDescricaoPedido(String descricaoPedido) {
    this.descricaoPedido = descricaoPedido;
  }


  public BigDecimal getValorDoPedido() {
    return valorDoPedido;
  }


  public void setValorDoPedido(BigDecimal valorDoPedido) {
    this.valorDoPedido = valorDoPedido;
  }


  public Drone getDrone() {
    return drone;
  }


  public void setDrone(Drone drone) {
    this.drone = drone;
  }


  public Double getPesoKg() {
    return pesoKg;
  }


  public void setPesoKg(Double pesoKg) {
    this.pesoKg = pesoKg;
  }


  public Double getVolumeM3() {
    return volumeM3;
  }


  public void setVolumeM3(Double volumeM3) {
    this.volumeM3 = volumeM3;
  }


  public Integer getLatitudeDestino() {
    return latitudeDestino;
  }


  public void setLatitudeDestino(Integer latitudeDestino) {
    this.latitudeDestino = latitudeDestino;
  }


  public Integer getLongitudeDestino() {
    return longitudeDestino;
  }


  public void setLongitudeDestino(Integer longitudeDestino) {
    this.longitudeDestino = longitudeDestino;
  }


  public Integer getLatitudeAtual() {
    return latitudeAtual;
  }


  public void setLatitudeAtual(Integer latitudeAtual) {
    this.latitudeAtual = latitudeAtual;
  }


  public Integer getLongitudeAtual() {
    return longitudeAtual;
  }


  public void setLongitudeAtual(Integer longitudeAtual) {
    this.longitudeAtual = longitudeAtual;
  }


  public Video getVideo() {
    return video;
  }


  public void setVideo(Video video) {
    this.video = video;
  }

}
