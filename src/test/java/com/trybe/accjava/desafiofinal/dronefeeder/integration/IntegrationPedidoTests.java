package com.trybe.accjava.desafiofinal.dronefeeder.integration;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import java.math.BigDecimal;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.trybe.accjava.desafiofinal.dronefeeder.dtos.DroneDto;
import com.trybe.accjava.desafiofinal.dronefeeder.dtos.PedidoDto;
import com.trybe.accjava.desafiofinal.dronefeeder.enums.StatusDroneEnum;
import com.trybe.accjava.desafiofinal.dronefeeder.model.Pedido;
import com.trybe.accjava.desafiofinal.dronefeeder.repository.DroneRepository;
import com.trybe.accjava.desafiofinal.dronefeeder.repository.PedidoRepository;
import com.trybe.accjava.desafiofinal.dronefeeder.service.DroneService;
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
public class IntegrationPedidoTests {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private DroneService droneService;

  @SpyBean
  private PedidoRepository pedidoRepository;

  @SpyBean
  private DroneRepository droneRepository;

  @Captor
  private ArgumentCaptor<Pedido> pedidoCaptor;

  DroneDto newDroneDto = new DroneDto();

  PedidoDto newPedidoDto = new PedidoDto();

  @BeforeEach
  public void setup() {
    droneRepository.deleteAll();
    pedidoRepository.deleteAll();
    newDroneDto = new DroneDto();
    newPedidoDto = new PedidoDto();
    newDroneDto = DroneDto.builder().nome("Drone 01").marca("Drone&Cia").fabricante("Drone&Cia")
        .altitudeMax(1000.00).duracaoBateria(24).capacidadeKg(20.00).capacidadeM3(10.00).build();
  }

  @WithMockUser(username = "dronefeeder")
  @Test
  @Order(1)
  @DisplayName("1 - Deve falhar ao cadastrar um pedido com um drone inexistente.")
  void cadastraPedidoDroneInexistente() throws Exception {

    DroneDto result = droneService.cadastrar(newDroneDto);
    newPedidoDto = PedidoDto.builder().dataEntregaProgramada("10/11/2022 10:00")
        .duracaoDoPercurso((long) 60).enderecoDeEntrega("Avenida Rui Barbosa 506")
        .descricaoPedido("Nintendo Switch 32gb").valorDoPedido(new BigDecimal(2299.00))
        .droneId(result.getId() + 1).pesoKg(4.00).volumeM3(1.00).build();

    mockMvc
        .perform(post("/v1/pedido").contentType(MediaType.APPLICATION_JSON)
            .content(new ObjectMapper().writeValueAsString(newPedidoDto)))
        .andExpect(status().isNotFound())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.error").value("Drone não encontrado"));
  }

  @WithMockUser(username = "dronefeeder")
  @Test
  @Order(2)
  @DisplayName("2 - Deve falhar ao cadastrar um pedido com um drone inativo.")
  void cadastraPedidoDroneInativo() throws Exception {

    DroneDto result = droneService.cadastrar(newDroneDto);
    droneService.alterarStatus(result.getId(), StatusDroneEnum.INATIVO);
    newPedidoDto = PedidoDto.builder().dataEntregaProgramada("10/11/2022 10:00")
        .duracaoDoPercurso((long) 60).enderecoDeEntrega("Avenida Rui Barbosa 506")
        .descricaoPedido("Nintendo Switch 32gb").valorDoPedido(new BigDecimal(2299.00))
        .droneId(result.getId()).pesoKg(4.00).volumeM3(1.00).build();

    mockMvc
        .perform(post("/v1/pedido").contentType(MediaType.APPLICATION_JSON)
            .content(new ObjectMapper().writeValueAsString(newPedidoDto)))
        .andExpect(status().isConflict())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.error").value("Drone inativo"));
  }

}
