package com.trybe.accjava.desafiofinal.dronefeeder.dtos;

import java.math.BigDecimal;
import java.time.LocalDateTime;
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
public class PedidoDto {

  private Long id;

  @NotNull(message = "Data da entrega é obrigatório")
  private LocalDateTime dataEntregaProgramada;
  @NotNull(message = "Duração do percurso é obrigatória")
  private Integer duracaoDoPercurso;
  private LocalDateTime dataProgramadaDaSaida;
  private LocalDateTime dataConfirmacaoEntrega;
  @NotBlank(message = "Endereco de entrega é obrigatório")
  private String enderecoDeEntrega;
  private String status;
  @NotBlank(message = "Descricao do pedido é obrigatório")
  private String descricaoPedido;
  @NotNull(message = "Valor do pedido é obrigatório")
  private BigDecimal valorDoPedido;
  @NotNull(message = "Id do drone é obrigatório")
  private Long droneId;
  @NotNull(message = "Peso do pedido é obrigatório")
  private Double pesoKg;
  @NotNull(message = "Volume do pedido é obrigatório")
  private Double volumeM3;
  private Integer latitude;
  private Integer longitude;
}
