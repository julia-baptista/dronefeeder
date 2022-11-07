package com.trybe.accjava.desafiofinal.dronefeeder.dtos;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import io.swagger.annotations.ApiModelProperty;
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

  @ApiModelProperty(required = false,
      value = "Id do Drone. Cadastrado automaticamente ao cadastrar o drone no banco de dados",
      example = "12")
  private Long id;

  @ApiModelProperty(required = true, value = "Nome do drone.", example = "Drone DJI Mini 2")
  @NotBlank(message = "Nome é obrigatório")
  private String nome;

  @ApiModelProperty(required = true, value = "Marca do drone.", example = "Mini 2")
  @NotBlank(message = "Marca é obrigatória")
  private String marca;

  @ApiModelProperty(required = true, value = "Fabricante do drone.", example = "‎DJI")
  @NotBlank(message = "Fabricante é obrigatório")
  private String fabricante;

  @ApiModelProperty(required = true, value = "Altitude máxima do drone.", example = "4000")
  @NotNull(message = "Altitute máxima é obrigatória")
  private Double altitudeMax;

  @ApiModelProperty(required = true, value = "Duração da bateria do drone.", example = "300")
  @NotNull(message = "Duração da bateria é obrigatória")
  private Integer duracaoBateria;

  @ApiModelProperty(required = true, value = "Capacidade em kg do drone.", example = "20")
  @NotNull(message = "Capacidade em KG é obrigatória")
  private Double capacidadeKg;

  @ApiModelProperty(required = true, value = "Capacidade em m3 do drone.", example = "10")
  @NotNull(message = "Capacidade em M3 é obrigatória")
  private Double capacidadeM3;

  @ApiModelProperty(required = false,
      value = "Satus do drone. Pode ser Ativo ou Inativo. É automaticamente cadastrado como ativo.",
      example = "Ativo")
  private String status;

}
