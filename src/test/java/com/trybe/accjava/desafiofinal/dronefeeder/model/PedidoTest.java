package com.trybe.accjava.desafiofinal.dronefeeder.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import com.trybe.accjava.desafiofinal.dronefeeder.enums.StatusPedidoEnum;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Model - Pedido Teste")
class PedidoTest {
  ByteArrayOutputStream output = new ByteArrayOutputStream();
  BigDecimal valorDoPedido = new BigDecimal("0.1");
  @Enumerated(EnumType.STRING)
  StatusPedidoEnum status = StatusPedidoEnum.AB;
  Video video;

  @BeforeEach
  public void setUp() throws UnsupportedEncodingException {
    PrintStream newOutput = new PrintStream(output, true, "UTF-8");
    System.setOut(newOutput);
  }

  @AfterEach
  public void cleanUp() {
    output.reset();
  }

  @Test
  @DisplayName("01 - Testa a inicialização do Pedido vazia.")
  void pedidoNullTest() {
    Pedido pedido = new Pedido();

    assertEquals(null, pedido.getId());
  }

  @Test
  @DisplayName("02 - Testa os setters e getters do Pedido.")
  void settersTest() {
    Drone drone = new Drone();
    Pedido pedido = new Pedido();

    pedido.setId(10l);
    assertEquals(10l, pedido.getId());

    pedido.setDataEntregaProgramada(LocalDateTime.parse("2023-01-30T12:00:00"));
    assertEquals(LocalDateTime.parse("2023-01-30T12:00:00"), pedido.getDataEntregaProgramada());

    pedido.setDuracaoDoPercurso(1);
    assertEquals(1, pedido.getDuracaoDoPercurso());

    pedido.setDataProgramadaDaSaida(LocalDateTime.parse("2023-01-25T12:00:00"));
    assertEquals(LocalDateTime.parse("2023-01-25T12:00:00"), pedido.getDataProgramadaDaSaida());

    pedido.setDataConfirmacaoEntrega(LocalDateTime.parse("2023-01-20T12:00:00"));
    assertEquals(LocalDateTime.parse("2023-01-20T12:00:00"), pedido.getDataConfirmacaoEntrega());

    pedido.setDescricaoPedido("Pedido Descrição Teste");
    assertEquals("Pedido Descrição Teste", pedido.getDescricaoPedido());

    pedido.setValorDoPedido(valorDoPedido);
    assertEquals(new BigDecimal("0.1"), pedido.getValorDoPedido());

    pedido.setDrone(drone);
    assertEquals(drone, pedido.getDrone());

    pedido.setPesoKg(1.00);
    assertEquals(1.00, pedido.getPesoKg());

    pedido.setVolumeM3(1.00);
    assertEquals(1.00, pedido.getVolumeM3());

    pedido.setLatitudeDestino(100);
    assertEquals(100, pedido.getLatitudeDestino());

    pedido.setLatitudeAtual(200);
    assertEquals(200, pedido.getLatitudeAtual());

    pedido.setLongitudeDestino(300);
    assertEquals(300, pedido.getLongitudeDestino());

    pedido.setLongitudeAtual(400);
    assertEquals(400, pedido.getLongitudeAtual());

    pedido.setVideo(video);
    assertEquals(video, pedido.getVideo());    

    pedido.setStatus(StatusPedidoEnum.AB);
    assertEquals(StatusPedidoEnum.AB, pedido.getStatus());

    pedido.setStatus(StatusPedidoEnum.EN);
    assertEquals(StatusPedidoEnum.EN, pedido.getStatus());
  }
}