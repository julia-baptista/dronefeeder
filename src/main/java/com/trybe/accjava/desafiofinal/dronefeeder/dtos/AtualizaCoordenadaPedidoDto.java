package com.trybe.accjava.desafiofinal.dronefeeder.dtos;

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
public class AtualizaCoordenadaPedidoDto {

  @ApiModelProperty(required = true, value = "Id do pedido que queremos atualizar as coordenadas.",
      example = "20")
  @NotNull(message = "Id do pedido é obrigatório")
  private Long pedidoId;

  @ApiModelProperty(required = true, value = "Latitude do drone que esta com o pedido.",
      example = "190")
  @NotNull(message = "Latitude é obrigatório")
  private Integer latitude;

  @ApiModelProperty(required = true, value = "Longitude do drone que esta com o pedido.",
      example = "300")
  @NotNull(message = "Longitude é obrigatório")
  private Integer longitude;


}
