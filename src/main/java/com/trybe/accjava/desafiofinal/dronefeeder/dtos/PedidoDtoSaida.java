package com.trybe.accjava.desafiofinal.dronefeeder.dtos;

import java.math.BigDecimal;
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
public class PedidoDtoSaida {

  @ApiModelProperty(required = false, value = "Id do pedido.", example = "1")
  private Long id;

  @ApiModelProperty(required = true, value = "Data da entrega programada do pedido.",
      example = "dd/mm/YYYY HH:mm")
  @NotNull(message = "Data da entrega é obrigatório")
  private String dataEntregaProgramada;

  @ApiModelProperty(required = true, value = "Duração do percurso da entrega. (em minutos)",
      example = "90")
  @NotNull(message = "Duração do percurso é obrigatória")
  private Long duracaoDoPercurso;

  @ApiModelProperty(required = false,
      value = "Data programada de saída do pedido. (Calculada automaticamente, não é necessário preencher)",
      example = "dd/mm/YYYY HH:mm")
  private String dataProgramadaDaSaida;

  @ApiModelProperty(required = false,
      value = "Data da confirmação da entrega do pedido. (Atualizada na entrega pelo drone, não é necessário preencher)",
      example = "dd/mm/YYYY HH:mm")
  private String dataConfirmacaoEntrega;

  @ApiModelProperty(required = true, value = "Endereço de entrega do pedido.",
      example = "Avenida Campeche, 100")
  @NotBlank(message = "Endereco de entrega é obrigatório")
  private String enderecoDeEntrega;

  @ApiModelProperty(required = false,
      value = "Status do pedido. (Atualizado automaticamente, não é necessário preencher)",
      example = "aberto")
  private String status;

  @ApiModelProperty(required = true, value = "Descrição do pedido.", example = "Jogo de pratos")
  @NotBlank(message = "Descricao do pedido é obrigatório")
  private String descricaoPedido;

  @ApiModelProperty(required = true, value = "Valor do pedido.", example = "1000.99")
  @NotNull(message = "Valor do pedido é obrigatório")
  private BigDecimal valorDoPedido;

  @ApiModelProperty(required = true, value = "Código de identificação do drone.", example = "25")
  @NotNull(message = "Id do drone é obrigatório")
  private Long droneId;

  @ApiModelProperty(required = true, value = "Peso em Kg do pedido.", example = "30.50")
  @NotNull(message = "Peso do pedido é obrigatório")
  private Double pesoKg;

  @ApiModelProperty(required = true, value = "Volume em m3 do pedido.", example = "100.50")
  @NotNull(message = "Volume do pedido é obrigatório")
  private Double volumeM3;

  @ApiModelProperty(required = false,
      value = "Coodernada de latitude do drone durante o percurso. (Atualizada automaticamente pelo drone, não é necessário preencher)",
      example = "100")
  private Integer latitude;

  @ApiModelProperty(required = false,
      value = "Coodernada de longitude do drone durante o percurso. (Atualizada automaticamente pelo drone, não é necessário preencher)",
      example = "100")
  private Integer longitude;
}
