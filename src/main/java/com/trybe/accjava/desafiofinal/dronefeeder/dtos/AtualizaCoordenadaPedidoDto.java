package com.trybe.accjava.desafiofinal.dronefeeder.dtos;

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
public class AtualizaCoordenadaPedidoDto {

  @NotNull(message = "Id do pedido é obrigatório")
  private Long pedidoId;

  @NotNull(message = "Latitude é obrigatório")
  private Integer latitude;

  @NotNull(message = "Longitude é obrigatório")
  private Integer longitude;


}
