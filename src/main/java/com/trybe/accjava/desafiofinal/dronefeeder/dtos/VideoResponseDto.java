package com.trybe.accjava.desafiofinal.dronefeeder.dtos;

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
public class VideoResponseDto {

  @ApiModelProperty(required = true, value = "Id do video", example = "1")
  private Long id;

  @ApiModelProperty(required = true, value = "Nome do arquivo gerado",
      example = "6ee6da22-200e-445a-97be-2a51b6f0faf6.mp4")
  private String nomeArquivo;

  @ApiModelProperty(required = true, value = "Id do pedido", example = "1")
  private Long pedidoId;

}
