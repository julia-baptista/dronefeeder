package com.trybe.accjava.desafiofinal.dronefeeder.model;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import com.trybe.accjava.desafiofinal.dronefeeder.enums.StatusDroneEnum;

@Entity
public class Drone {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  private String nome;

  private String marca;

  private String fabricante;

  private Double altitudeMax;

  private Integer duracaoBateria;

  private Double capacidadeKg;

  private Double capacidadeM3;

  @Enumerated(EnumType.STRING)
  private StatusDroneEnum status;

  @OneToMany(mappedBy = "drone", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<Pedido> pedidos = new ArrayList<>();

  public Drone() {
    super();
  }

  public Drone(Long id, String nome, String marca, String fabricante, Double altitudeMax,
      Integer duracaoBateria, Double capacidadeKg, Double capacidadeM3, StatusDroneEnum status) {
    super();
    this.id = id;
    this.nome = nome;
    this.marca = marca;
    this.fabricante = fabricante;
    this.altitudeMax = altitudeMax;
    this.duracaoBateria = duracaoBateria;
    this.capacidadeKg = capacidadeKg;
    this.capacidadeM3 = capacidadeM3;
    this.status = status;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getNome() {
    return nome;
  }

  public void setNome(String nome) {
    this.nome = nome;
  }

  public String getMarca() {
    return marca;
  }

  public void setMarca(String marca) {
    this.marca = marca;
  }

  public String getFabricante() {
    return fabricante;
  }

  public void setFabricante(String fabricante) {
    this.fabricante = fabricante;
  }

  public Double getAltitudeMax() {
    return altitudeMax;
  }

  public void setAltitudeMax(Double altitudeMax) {
    this.altitudeMax = altitudeMax;
  }

  public Integer getDuracaoBateria() {
    return duracaoBateria;
  }

  public void setDuracaoBateria(Integer duracaoBateria) {
    this.duracaoBateria = duracaoBateria;
  }

  public Double getCapacidadeKg() {
    return capacidadeKg;
  }

  public void setCapacidadeKg(Double capacidadeKg) {
    this.capacidadeKg = capacidadeKg;
  }

  public Double getCapacidadeM3() {
    return capacidadeM3;
  }

  public void setCapacidadeM3(Double capacidadeM3) {
    this.capacidadeM3 = capacidadeM3;
  }

  public StatusDroneEnum getStatus() {
    return status;
  }

  public void setStatus(StatusDroneEnum status) {
    this.status = status;
  }


}
