package com.trybe.accjava.desafiofinal.dronefeeder.dtos;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
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
public class DroneDto {

  private Long id;

  @NotBlank(message = "Nome é obrigatório")
  private String nome;

  @NotBlank(message = "Marca é obrigatória")
  private String marca;

  @NotBlank(message = "Fabricante é obrigatório")
  private String fabricante;

  @NotNull(message = "Altitute Máxima é obrigatória")
  private Double altitudeMax;

  @NotNull(message = "Duração da bateria é obrigatória")
  private Integer duracaoBateria;

  @NotNull(message = "Capacidade em KG é obrigatória")
  private Double capacidadeKg;

  @NotNull(message = "Capacidade em M3 é obrigatória")
  private Double capacidadeM3;

  private String status;

}
