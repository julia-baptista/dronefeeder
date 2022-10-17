package com.trybe.accjava.desafiofinal.dronefeeder.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import com.trybe.accjava.desafiofinal.dronefeeder.enums.StatusDroneEnum;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Model - Drone Teste")
class DroneTest {
  private final ByteArrayOutputStream output = new ByteArrayOutputStream();
  String nome = "Nome Teste";
  String marca = "Marca Teste";
  String fabricante = "Fabricante Teste";
  Double altitudeMax = 100.00;
  Integer duracaoBateria = 1;
  Double capacidadeKg = 1.00;
  Double capacidadeM3 = 1.00;
  @Enumerated(EnumType.STRING)
  StatusDroneEnum status = StatusDroneEnum.ATIVO;

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
  @DisplayName("01 - Testa a inicialização do drone.")
  void droneConstructorTest() {
    Drone drone = new Drone(1l, nome, marca, fabricante,
    		altitudeMax, duracaoBateria, capacidadeKg, capacidadeM3, status);
    
    assertEquals(1l, drone.getId());
    assertEquals("Nome Teste", drone.getNome());
    assertEquals("Marca Teste", drone.getMarca());
    assertEquals("Fabricante Teste", drone.getFabricante());
    assertEquals(100.00, drone.getAltitudeMax());
    assertEquals(1, drone.getDuracaoBateria());
    assertEquals(1.00, drone.getCapacidadeKg());
    assertEquals(1.00, drone.getCapacidadeM3());
    assertEquals(status, drone.getStatus());
  }

  @Test
  @DisplayName("02 - Testa a inicialização do drone vazia.")
  void droneNullTest() {
    Drone drone = new Drone();

    assertEquals(null, drone.getId());
  }

  @Test
  @DisplayName("03 - Testa os setters do Drone.")
  void settersTest() {
    Drone drone = new Drone();

    drone.setId(2l);
    assertEquals(2l, drone.getId());

    drone.setNome("Novo Nome");
    assertEquals("Novo Nome", drone.getNome());

    drone.setMarca("Nova Marca");
    assertEquals("Nova Marca", drone.getMarca());

    drone.setFabricante("Novo Fabricante");
    assertEquals("Novo Fabricante", drone.getFabricante());

    drone.setAltitudeMax(200.00);
    assertEquals(200.00, drone.getAltitudeMax());

    drone.setDuracaoBateria(2);
    assertEquals(2, drone.getDuracaoBateria());

    drone.setCapacidadeKg(2.00);
    assertEquals(2.00, drone.getCapacidadeKg());

    drone.setCapacidadeM3(2.00);
    assertEquals(2.00, drone.getCapacidadeM3());

    drone.setStatus(StatusDroneEnum.INATIVO);
    assertEquals(StatusDroneEnum.INATIVO, drone.getStatus());
  }
}
