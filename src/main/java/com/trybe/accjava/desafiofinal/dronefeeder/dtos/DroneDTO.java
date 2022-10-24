package com.trybe.accjava.desafiofinal.dronefeeder.dtos;

import java.util.ArrayList;
import java.util.List;
import com.trybe.accjava.desafiofinal.dronefeeder.model.Pedido;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DroneDTO {

  private Long id;

  private String nome;

  private String marca;

  private String fabricante;

  private Double altitudeMax;

  private Integer duracaoBateria;

  private Double capacidadeKg;

  private Double capacidadeM3;

  private String status;

  private List<Pedido> pedidos = new ArrayList<>();

}
