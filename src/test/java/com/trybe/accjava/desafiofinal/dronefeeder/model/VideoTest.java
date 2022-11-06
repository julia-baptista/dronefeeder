package com.trybe.accjava.desafiofinal.dronefeeder.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Model - Video Teste")
class VideoTest {
  private final ByteArrayOutputStream output = new ByteArrayOutputStream();
  String localizacao = "entregapedido1.mp4";

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
  @DisplayName("01 - Testa a inicialização do video.")
  void videoConstructorTest() {
    Pedido pedido = new Pedido();
    Video video = new Video(1l, localizacao, pedido);

    assertEquals(1l, video.getId());
    assertEquals("entregapedido1.mp4", video.getNomeArquivo());
    assertEquals(pedido, video.getPedido());
  }

  @Test
  @DisplayName("02 - Testa a inicialização do video vazia.")
  void videoNullTest() {
    Video video = new Video();

    assertEquals(null, video.getId());
  }

  @Test
  @DisplayName("03 - Testa os setters do video.")
  void settersTest() {
    Pedido pedido = new Pedido();
    Video video = new Video();

    video.setId(2l);
    assertEquals(2l, video.getId());

    video.setNomeArquivo("entregapedido1.mp4");
    assertEquals("entregapedido1.mp4", video.getNomeArquivo());

    video.setPedido(pedido);
    assertEquals(pedido, video.getPedido());
  }
}
