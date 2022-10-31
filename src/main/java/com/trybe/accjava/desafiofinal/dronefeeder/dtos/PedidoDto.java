package com.trybe.accjava.desafiofinal.dronefeeder.dtos;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import com.trybe.accjava.desafiofinal.dronefeeder.model.Drone;
import com.trybe.accjava.desafiofinal.dronefeeder.model.Video;
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
public class PedidoDto {

  private Long id;

  private LocalDateTime dataEntregaProgramada;
  private Integer duracaoDoPercurso;
  private LocalDateTime dataProgramadaDaSaida;
  private LocalDateTime dataConfirmacaoEntrega;
  private String status;
  private String descricaoPedido;
  private BigDecimal valorDoPedido;
  private Long droneId;
  private Double pesoKg;
  private Double volumeM3;
  private Integer latitude;
  private Integer longitude;
}
