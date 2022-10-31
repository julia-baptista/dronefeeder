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
  private Integer latitude;
  private Integer longitude;

  @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
  @JoinColumn(name = "video_id", referencedColumnName = "id")
  private Video video;

  public Pedido() {}

  public Pedido(Long id, LocalDateTime dataEntregaProgramada, Integer duracaoDoPercurso,
      LocalDateTime dataProgramadaDaSaida, LocalDateTime dataConfirmacaoEntrega,
      StatusPedidoEnum status, String descricaoPedido, BigDecimal valorDoPedido, Drone drone,
      Double pesoKg, Double volumeM3, Integer latitude, Integer longitude, Video video) {
    super();
    this.id = id;
    this.dataEntregaProgramada = dataEntregaProgramada;
    this.duracaoDoPercurso = duracaoDoPercurso;
    this.dataProgramadaDaSaida = dataProgramadaDaSaida;
    this.dataConfirmacaoEntrega = dataConfirmacaoEntrega;
    this.status = status;
    this.descricaoPedido = descricaoPedido;
    this.valorDoPedido = valorDoPedido;
    this.drone = drone;
    this.pesoKg = pesoKg;
    this.volumeM3 = volumeM3;
    this.latitude = latitude;
    this.longitude = longitude;
    this.video = video;
  }

  public Pedido(LocalDateTime dataEntregaProgramada, Integer duracaoDoPercurso,
      LocalDateTime dataProgramadaDaSaida, LocalDateTime dataConfirmacaoEntrega,
      StatusPedidoEnum status, String descricaoPedido, BigDecimal valorDoPedido, Double pesoKg,
      Double volumeM3, Integer latitude, Integer longitude, Video video) {
    super();
    this.dataEntregaProgramada = dataEntregaProgramada;
    this.duracaoDoPercurso = duracaoDoPercurso;
    this.dataProgramadaDaSaida = dataProgramadaDaSaida;
    this.dataConfirmacaoEntrega = dataConfirmacaoEntrega;
    this.status = status;
    this.descricaoPedido = descricaoPedido;
    this.valorDoPedido = valorDoPedido;
    this.pesoKg = pesoKg;
    this.volumeM3 = volumeM3;
    this.latitude = latitude;
    this.longitude = longitude;
  }

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

  public Integer getLatitude() {
    return latitude;
  }

  public void setLatitude(Integer latitude) {
    this.latitude = latitude;
  }

  public Integer getLongitude() {
    return longitude;
  }

  public void setLongitude(Integer longitude) {
    this.longitude = longitude;
  }

  public Video getVideo() {
    return video;
  }

  public void setVideo(Video video) {
    this.video = video;
  }



}
