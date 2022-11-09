package com.trybe.accjava.desafiofinal.dronefeeder.integration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.trybe.accjava.desafiofinal.dronefeeder.dtos.DroneDtoSaida;
import com.trybe.accjava.desafiofinal.dronefeeder.enums.StatusDroneEnum;
import com.trybe.accjava.desafiofinal.dronefeeder.model.Drone;
import com.trybe.accjava.desafiofinal.dronefeeder.repository.DroneRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class IntegrationDroneTests {

  @Autowired
  private MockMvc mockMvc;

  @SpyBean
  private DroneRepository droneRepository;

  @Captor
  private ArgumentCaptor<Drone> droneCaptor;

  @BeforeEach
  public void setup() {
    droneRepository.deleteAll();
  }

  @WithMockUser(username = "dronefeeder")
  @Test
  @Order(1)
  @DisplayName("1 - Deve cadastrar um novo Drone na base de dados.")
  void cadastrarDroneTest() throws Exception {

    DroneDtoSaida newDroneDto = DroneDtoSaida.builder().nome("Drone 01").marca("Drone&Cia")
        .fabricante("Drone&Cia").altitudeMax(1000.00).duracaoBateria(24).capacidadeKg(20.00)
        .capacidadeM3(10.00).build();

    mockMvc
        .perform(post("/v1/drone").contentType(MediaType.APPLICATION_JSON)
            .content(new ObjectMapper().writeValueAsString(newDroneDto)))
        .andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.nome").value(newDroneDto.getNome()));

    verify(droneRepository, atLeast(1)).save(droneCaptor.capture());

    assertThat(droneCaptor.getValue()).isNotNull();
    assertThat(droneCaptor.getValue().getId()).isNotNull();
    assertThat(droneCaptor.getValue().getMarca()).isNotNull();
    assertThat(droneCaptor.getValue().getFabricante()).isNotNull();
    assertThat(droneCaptor.getValue().getAltitudeMax()).isNotNull();
    assertThat(droneCaptor.getValue().getDuracaoBateria()).isNotNull();
    assertThat(droneCaptor.getValue().getCapacidadeKg()).isNotNull();
    assertThat(droneCaptor.getValue().getCapacidadeM3()).isNotNull();
    assertThat(droneCaptor.getValue().getStatus()).isNotNull();

    // DroneDto newDroneDto2 = DroneDto.builder().nome(null).marca(null).fabricante(null)
    // .altitudeMax(null).duracaoBateria(null).capacidadeKg(null).capacidadeM3(null).build();
    //
    // mockMvc
    // .perform(post("/v1/drone").contentType(MediaType.APPLICATION_JSON)
    // .content(new ObjectMapper().writeValueAsString(newDroneDto2)))
    // .andExpect(content().contentType(MediaType.APPLICATION_JSON))
    // .andExpect(status().isInternalServerError())
    // .andExpect(jsonPath("$.error").value("Erro inesperado"));

  }

  @WithMockUser(username = "dronefeeder")
  @Test
  @Order(2)
  @DisplayName("2 - Deve utilizar @ControllerAdvice para lançar erro quando já houver drone cadastrado com determinado nome.")
  void cadastraDroneJaExistenteTest() throws Exception {

    Drone newDrone = new Drone("Drone 02", "Drone&Cia", "Drone&Cia", 1000.00, 24, 20.00, 10.00,
        StatusDroneEnum.ATIVO);
    droneRepository.save(newDrone);

    mockMvc
        .perform(post("/v1/drone").contentType(MediaType.APPLICATION_JSON)
            .content(new ObjectMapper().writeValueAsString(newDrone)))
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isConflict()).andExpect(jsonPath("$.error").value("Drone Existente"));

  }

  @WithMockUser(username = "dronefeeder")
  @Test
  @Order(3)
  @DisplayName("3 - Deve retornar todas os Drones existentes da base de dados.")
  void retornaTodosOsDronesDoBancoTest() throws Exception {

    Drone newDrone = new Drone("Drone 01", "Drone&Cia", "Drone&Cia", 1000.00, 24, 20.00, 10.00,
        StatusDroneEnum.ATIVO);
    Drone newDrone2 = new Drone("Drone 02", "Drone&Cia", "Drone&Cia", 1000.00, 24, 20.00, 10.00,
        StatusDroneEnum.ATIVO);
    droneRepository.save(newDrone);
    droneRepository.save(newDrone2);

    mockMvc.perform(get("/v1/drone").contentType(MediaType.APPLICATION_JSON))
        .andExpect(content().contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
        .andExpect(jsonPath("$[0].nome").value(newDrone.getNome()))
        .andExpect(jsonPath("$[1].nome").value(newDrone2.getNome()));

  }

  @WithMockUser(username = "dronefeeder")
  @Test
  @Order(4)
  @DisplayName("4 - Deve retornar lista vazia quando não existir séries na base de dados.")
  void retornaListaVaziaQuandoNaoExistemDronesNoBancoTest() throws Exception {

    mockMvc.perform(get("/v1/drone").contentType(MediaType.APPLICATION_JSON))
        .andExpect(content().contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
        .andExpect(content().string(containsString("[]")));

  }

  @WithMockUser(username = "dronefeeder")
  @Test
  @Order(5)
  @DisplayName("5 - Deve remover Drone, por um id existente informado.")
  void removeDronePorIdTest() throws Exception {

    Drone newDrone = new Drone("Drone 01", "Drone&Cia", "Drone&Cia", 1000.00, 24, 20.00, 10.00,
        StatusDroneEnum.ATIVO);
    droneRepository.save(newDrone);

    mockMvc.perform(delete("/v1/drone/" + newDrone.getId())).andExpect(status().isAccepted());

  }

  @WithMockUser(username = "dronefeeder")
  @Test
  @Order(6)
  @DisplayName("6 - Deve utilizar @ControllerAdvice em controller para informar que o Drone comdeterminado idnão existena base de dados.")
  void removeDroneComIdNaoExistenteNoBancoTest() throws Exception {

    Drone newDrone = new Drone("Drone 01", "Drone&Cia", "Drone&Cia", 1000.00, 24, 20.00, 10.00,
        StatusDroneEnum.ATIVO);
    droneRepository.save(newDrone);

    mockMvc
        .perform(delete("/v1/drone/2").contentType(MediaType.APPLICATION_JSON)
            .content(new ObjectMapper().writeValueAsString(newDrone)))
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.error").value("Drone não encontrado"));

  }

  @WithMockUser(username = "dronefeeder")
  @Test
  @Order(7)
  @DisplayName("7 - Deve atualizar um Drone.")
  void atualizaDroneTest() throws Exception {

    Drone newDrone1 = new Drone("Drone 01", "Drone&Cia", "Drone&Cia", 1000.00, 24, 20.00, 10.00,
        StatusDroneEnum.ATIVO);
    Drone newDrone2 = new Drone("Drone 02", "Drone&Cia", "Drone&Cia", 1000.00, 24, 20.00, 10.00,
        StatusDroneEnum.ATIVO);

    droneRepository.save(newDrone1);
    droneRepository.save(newDrone2);

    DroneDtoSaida droneUpdatedDto1 = DroneDtoSaida.builder().nome("Drone 02").marca("Drones&Drones")
        .fabricante("Drones&Drones").altitudeMax(1000.00).duracaoBateria(48).capacidadeKg(20.00)
        .capacidadeM3(10.00).build();
    DroneDtoSaida droneUpdatedDto2 = DroneDtoSaida.builder().nome("Drone 01").marca("Drones&Drones")
        .fabricante("Drones&Drones").altitudeMax(1000.00).duracaoBateria(48).capacidadeKg(20.00)
        .capacidadeM3(10.00).build();
    // Drone newDroneUpdated = new Drone("Drone 01", "Drones&Drones", "Drones&Drones", 1000.00, 48,
    // 20.00, 10.00, StatusDroneEnum.ATIVO);

    mockMvc
        .perform(put("/v1/drone/" + (newDrone1.getId() + 2)).contentType(MediaType.APPLICATION_JSON)
            .content(new ObjectMapper().writeValueAsString(droneUpdatedDto1)))
        .andExpect(status().isNotFound())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.error").value("Drone não encontrado"));

    mockMvc
        .perform(put("/v1/drone/" + newDrone1.getId()).contentType(MediaType.APPLICATION_JSON)
            .content(new ObjectMapper().writeValueAsString(droneUpdatedDto1)))
        .andExpect(status().isConflict())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.error").value("Drone Existente"));

    mockMvc
        .perform(put("/v1/drone/" + newDrone1.getId()).contentType(MediaType.APPLICATION_JSON)
            .content(new ObjectMapper().writeValueAsString(droneUpdatedDto2)))
        .andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.marca").value(droneUpdatedDto2.getMarca()));

  }

  @WithMockUser(username = "dronefeeder")
  @Test
  @Order(8)
  @DisplayName("8 - Deve alterar o status de um Drone.")
  void alteraStatusDeUmDrone() throws Exception {

    Drone newDrone = new Drone("Drone 01", "Drone&Cia", "Drone&Cia", 1000.00, 24, 20.00, 10.00,
        StatusDroneEnum.ATIVO);
    droneRepository.save(newDrone);

    mockMvc
        .perform(put("/v1/drone/inativar/" + (newDrone.getId() + 1))
            .contentType(MediaType.APPLICATION_JSON)
            .content(new ObjectMapper().writeValueAsString(newDrone)))
        .andExpect(status().isNotFound())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.error").value("Drone não encontrado"));

    mockMvc
        .perform(put("/v1/drone/ativar/" + (newDrone.getId() + 1))
            .contentType(MediaType.APPLICATION_JSON)
            .content(new ObjectMapper().writeValueAsString(newDrone)))
        .andExpect(status().isNotFound())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.error").value("Drone não encontrado"));

    mockMvc
        .perform(
            put("/v1/drone/inativar/" + newDrone.getId()).contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(newDrone)))
        .andExpect(status().isAccepted())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.status").value("Inativo"));

    mockMvc
        .perform(put("/v1/drone/ativar/" + newDrone.getId()).contentType(MediaType.APPLICATION_JSON)
            .content(new ObjectMapper().writeValueAsString(newDrone)))
        .andExpect(status().isAccepted())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.status").value("Ativo"));
  }


}
