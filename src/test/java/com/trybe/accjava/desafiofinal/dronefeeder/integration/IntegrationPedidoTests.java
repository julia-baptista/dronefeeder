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
import com.trybe.accjava.desafiofinal.dronefeeder.service.PedidoService;
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

  @Autowired
  private PedidoService pedidoService;

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
  void shouldFailCadastrarPedidoDroneInexistente() throws Exception {

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
  void shouldFailCadastrarPedidoDroneInativo() throws Exception {

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

  @WithMockUser(username = "dronefeeder")
  @Test
  @Order(3)
  @DisplayName("3 - Deve falhar ao cadastrar um pedido que exceda a capacidade de carga em Kg do drone.")
  void shouldFailCadastrarPedidoKgMaiorDoQueCapacidadeDrone() throws Exception {

    DroneDto result = droneService.cadastrar(newDroneDto);
    newPedidoDto = PedidoDto.builder().dataEntregaProgramada("10/11/2022 10:00")
        .duracaoDoPercurso((long) 60).enderecoDeEntrega("Avenida Rui Barbosa 506")
        .descricaoPedido("Anilha 30kg").valorDoPedido(new BigDecimal(150.00))
        .droneId(result.getId()).pesoKg(30.00).volumeM3(1.00).build();

    mockMvc
        .perform(post("/v1/pedido").contentType(MediaType.APPLICATION_JSON)
            .content(new ObjectMapper().writeValueAsString(newPedidoDto)))
        .andExpect(status().isConflict())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.error").value("Peso Excedido"));
  }

  @WithMockUser(username = "dronefeeder")
  @Test
  @Order(4)
  @DisplayName("4 - Deve falhar ao cadastrar um pedido que exceda a capacidade de carga em m3 do drone.")
  void shouldFailCadastrarPedidoM3MaiorDoQueCapacidadeDrone() throws Exception {

    DroneDto result = droneService.cadastrar(newDroneDto);
    newPedidoDto = PedidoDto.builder().dataEntregaProgramada("10/11/2022 10:00")
        .duracaoDoPercurso((long) 60).enderecoDeEntrega("Avenida Rui Barbosa 506")
        .descricaoPedido("Cama King Size").valorDoPedido(new BigDecimal(3000.00))
        .droneId(result.getId()).pesoKg(19.00).volumeM3(15.00).build();

    mockMvc
        .perform(post("/v1/pedido").contentType(MediaType.APPLICATION_JSON)
            .content(new ObjectMapper().writeValueAsString(newPedidoDto)))
        .andExpect(status().isConflict())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.error").value("Volume Excedido"));
  }

  @WithMockUser(username = "dronefeeder")
  @Test
  @Order(5)
  @DisplayName("5 - Deve falhar ao cadastrar um pedido que sobreponha o horário de outro pedido do drone escolhido.")
  void shouldFailCadastrarPedidoComHorarioSobreposto() throws Exception {

    DroneDto result = droneService.cadastrar(newDroneDto);
    newPedidoDto = PedidoDto.builder().dataEntregaProgramada("10/11/2022 10:00")
        .duracaoDoPercurso((long) 60).enderecoDeEntrega("Avenida Rui Barbosa 506")
        .descricaoPedido("Nintendo Switch 32gb").valorDoPedido(new BigDecimal(2299.00))
        .droneId(result.getId()).pesoKg(4.00).volumeM3(1.00).build();

    pedidoService.cadastrar(newPedidoDto);

    PedidoDto newPedidoDto2 = PedidoDto.builder().dataEntregaProgramada("10/11/2022 09:30")
        .duracaoDoPercurso((long) 30).enderecoDeEntrega("Avenida Rui Barbosa 506")
        .descricaoPedido("PlayStation 5").valorDoPedido(new BigDecimal(4499.00))
        .droneId(result.getId()).pesoKg(4.00).volumeM3(1.00).build();

    mockMvc
        .perform(post("/v1/pedido").contentType(MediaType.APPLICATION_JSON)
            .content(new ObjectMapper().writeValueAsString(newPedidoDto2)))
        .andExpect(status().isConflict())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.error").value("DataHora do pedido inválida."));
  }

  @WithMockUser(username = "dronefeeder")
  @Test
  @Order(6)
  @DisplayName("6 - Deve cadastrar um pedido com sucesso.")
  void shouldCadastrarPedido() throws Exception {

    DroneDto result = droneService.cadastrar(newDroneDto);
    newPedidoDto = PedidoDto.builder().dataEntregaProgramada("10/11/2022 10:00")
        .duracaoDoPercurso((long) 60).enderecoDeEntrega("Avenida Rui Barbosa 506")
        .descricaoPedido("Nintendo Switch 32gb").valorDoPedido(new BigDecimal(2299.00))
        .droneId(result.getId()).pesoKg(1.00).volumeM3(1.00).build();

    mockMvc
        .perform(post("/v1/pedido").contentType(MediaType.APPLICATION_JSON)
            .content(new ObjectMapper().writeValueAsString(newPedidoDto)))
        .andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.descricaoPedido").value(newPedidoDto.getDescricaoPedido()));


  }



}
