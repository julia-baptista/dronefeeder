package com.trybe.accjava.desafiofinal.dronefeeder.integration;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import java.math.BigDecimal;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.trybe.accjava.desafiofinal.dronefeeder.dtos.DroneDto;
import com.trybe.accjava.desafiofinal.dronefeeder.dtos.PedidoDto;
import com.trybe.accjava.desafiofinal.dronefeeder.enums.StatusDroneEnum;
import com.trybe.accjava.desafiofinal.dronefeeder.enums.StatusPedidoEnum;
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

  @WithMockUser(username = "dronefeeder")
  @Test
  @Order(7)
  @DisplayName("7 - Deve listar todos os pedidos com sucesso.")
  void shouldListarTodosPedidos() throws Exception {

    DroneDto result = droneService.cadastrar(newDroneDto);
    newPedidoDto = PedidoDto.builder().dataEntregaProgramada("10/11/2022 10:00")
        .duracaoDoPercurso((long) 60).enderecoDeEntrega("Avenida Rui Barbosa 506")
        .descricaoPedido("Nintendo Switch 32gb").valorDoPedido(new BigDecimal(2299.00))
        .droneId(result.getId()).pesoKg(10.00).volumeM3(1.00).build();
    PedidoDto newPedidoDto2 = PedidoDto.builder().dataEntregaProgramada("11/11/2022 09:30")
        .duracaoDoPercurso((long) 30).enderecoDeEntrega("Avenida Rui Barbosa 506")
        .descricaoPedido("PlayStation 5").valorDoPedido(new BigDecimal(4499.00))
        .droneId(result.getId()).pesoKg(4.00).volumeM3(1.00).build();

    pedidoService.cadastrar(newPedidoDto);
    pedidoService.cadastrar(newPedidoDto2);

    mockMvc
        .perform(get("/v1/pedido").contentType(MediaType.APPLICATION_JSON)
            .content(new ObjectMapper().writeValueAsString(newPedidoDto)))
        .andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$[0].descricaoPedido").value(newPedidoDto.getDescricaoPedido()))
        .andExpect(jsonPath("$[1].descricaoPedido").value(newPedidoDto2.getDescricaoPedido()));
  }

  @WithMockUser(username = "dronefeeder")
  @Test
  @Order(8)
  @DisplayName("8 - Deve listar todos os pedidos de um Drone específico.")
  void shouldListarTodosPedidosDeUmDrone() throws Exception {

    DroneDto result = droneService.cadastrar(newDroneDto);
    newPedidoDto = PedidoDto.builder().dataEntregaProgramada("10/11/2022 10:00")
        .duracaoDoPercurso((long) 60).enderecoDeEntrega("Avenida Rui Barbosa 506")
        .descricaoPedido("Nintendo Switch 32gb").valorDoPedido(new BigDecimal(2299.00))
        .droneId(result.getId()).pesoKg(10.00).volumeM3(1.00).build();
    PedidoDto newPedidoDto2 = PedidoDto.builder().dataEntregaProgramada("11/11/2022 09:30")
        .duracaoDoPercurso((long) 30).enderecoDeEntrega("Avenida Rui Barbosa 506")
        .descricaoPedido("PlayStation 5").valorDoPedido(new BigDecimal(4499.00))
        .droneId(result.getId()).pesoKg(4.00).volumeM3(1.00).build();

    pedidoService.cadastrar(newPedidoDto);
    pedidoService.cadastrar(newPedidoDto2);

    mockMvc
        .perform(get("/v1/pedido/drone/" + result.getId()).contentType(MediaType.APPLICATION_JSON)
            .content(new ObjectMapper().writeValueAsString(newPedidoDto)))
        .andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$[0].descricaoPedido").value(newPedidoDto.getDescricaoPedido()))
        .andExpect(jsonPath("$[1].descricaoPedido").value(newPedidoDto2.getDescricaoPedido()));
  }

  @WithMockUser(username = "dronefeeder")
  @Test
  @Order(9)
  @DisplayName("9 - Deve falhar ao tentar excluir um pedido não encontrado no banco de dados.")
  void shouldFailDeletarPedidoNaoEncontradoNoBanco() throws Exception {

    DroneDto result = droneService.cadastrar(newDroneDto);
    newPedidoDto = PedidoDto.builder().dataEntregaProgramada("10/11/2022 10:00")
        .duracaoDoPercurso((long) 60).enderecoDeEntrega("Avenida Rui Barbosa 506")
        .descricaoPedido("Nintendo Switch 32gb").valorDoPedido(new BigDecimal(2299.00))
        .droneId(result.getId()).pesoKg(10.00).volumeM3(1.00).build();

    PedidoDto pedidoDto = pedidoService.cadastrar(newPedidoDto);

    mockMvc.perform(delete("/v1/pedido/" + pedidoDto.getId() + 1)).andExpect(status().isNotFound())
        .andExpect(jsonPath("$.error").value("Pedido não encontrado"));
  }

  @WithMockUser(username = "dronefeeder")
  @Test
  @Order(10)
  @DisplayName("10 - Deve excluir um pedido com sucesso.")
  void shouldDeletarPedido() throws Exception {

    DroneDto result = droneService.cadastrar(newDroneDto);
    newPedidoDto = PedidoDto.builder().dataEntregaProgramada("10/11/2022 10:00")
        .duracaoDoPercurso((long) 60).enderecoDeEntrega("Avenida Rui Barbosa 506")
        .descricaoPedido("Nintendo Switch 32gb").valorDoPedido(new BigDecimal(2299.00))
        .droneId(result.getId()).pesoKg(10.00).volumeM3(1.00).build();

    PedidoDto pedidoDto = pedidoService.cadastrar(newPedidoDto);

    mockMvc.perform(delete("/v1/pedido/" + pedidoDto.getId())).andExpect(status().isAccepted());
  }

  @WithMockUser(username = "dronefeeder")
  @Test
  @Order(11)
  @DisplayName("11 - Deve falhar ao alterar um pedido nao encontrado no banco de dados.")
  void shoulfFaitAlterarPedidoNaoEncontradoNoBanco() throws Exception {

    DroneDto result = droneService.cadastrar(newDroneDto);
    newPedidoDto = PedidoDto.builder().dataEntregaProgramada("10/11/2022 10:00")
        .duracaoDoPercurso((long) 60).enderecoDeEntrega("Avenida Rui Barbosa 506")
        .descricaoPedido("Nintendo Switch 32gb").valorDoPedido(new BigDecimal(2299.00))
        .droneId(result.getId()).pesoKg(10.00).volumeM3(1.00).build();

    PedidoDto pedidoDto = pedidoService.cadastrar(newPedidoDto);

    PedidoDto newPedidoDto2 = PedidoDto.builder().dataEntregaProgramada("10/11/2022 10:00")
        .duracaoDoPercurso((long) 60).enderecoDeEntrega("Avenida Rui Barbosa 364")
        .descricaoPedido("Nintendo Switch Oled 32gb").valorDoPedido(new BigDecimal(2599.00))
        .droneId(result.getId()).pesoKg(4.00).volumeM3(1.00).build();

    mockMvc
        .perform(put("/v1/pedido/" + pedidoDto.getId() + 1).contentType(MediaType.APPLICATION_JSON)
            .content(new ObjectMapper().writeValueAsString(newPedidoDto2)))
        .andExpect(status().isNotFound())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.error").value("Pedido não encontrado"));
  }

  @WithMockUser(username = "dronefeeder")
  @Test
  @Order(12)
  @DisplayName("12 - Deve falhar ao alterar um pedido que está com status cancelado.")
  void shoulfFailAlterarPedidoStatusCancelado() throws Exception {

    DroneDto result = droneService.cadastrar(newDroneDto);
    newPedidoDto = PedidoDto.builder().dataEntregaProgramada("10/11/2022 10:00")
        .duracaoDoPercurso((long) 60).enderecoDeEntrega("Avenida Rui Barbosa 506")
        .descricaoPedido("Nintendo Switch 32gb").valorDoPedido(new BigDecimal(2299.00))
        .droneId(result.getId()).pesoKg(10.00).volumeM3(1.00).build();

    PedidoDto pedidoDto = pedidoService.cadastrar(newPedidoDto);

    pedidoService.alterarStatus(pedidoDto.getId(), StatusPedidoEnum.CA);

    PedidoDto newPedidoDto2 = PedidoDto.builder().dataEntregaProgramada("10/11/2022 10:00")
        .duracaoDoPercurso((long) 60).enderecoDeEntrega("Avenida Rui Barbosa 364")
        .descricaoPedido("Nintendo Switch Oled 32gb").valorDoPedido(new BigDecimal(2599.00))
        .droneId(result.getId()).pesoKg(4.00).volumeM3(1.00).build();

    mockMvc
        .perform(put("/v1/pedido/" + pedidoDto.getId()).contentType(MediaType.APPLICATION_JSON)
            .content(new ObjectMapper().writeValueAsString(newPedidoDto2)))
        .andExpect(status().isConflict())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.error").value("Pedido cancelado"));
  }

  @WithMockUser(username = "dronefeeder")
  @Test
  @Order(13)
  @DisplayName("13 - Deve falhar ao alterar um pedido que está com status em andamento.")
  void shoulfFailAlterarPedidoStatusEmAndamento() throws Exception {

    DroneDto result = droneService.cadastrar(newDroneDto);
    newPedidoDto = PedidoDto.builder().dataEntregaProgramada("10/11/2022 10:00")
        .duracaoDoPercurso((long) 60).enderecoDeEntrega("Avenida Rui Barbosa 506")
        .descricaoPedido("Nintendo Switch 32gb").valorDoPedido(new BigDecimal(2299.00))
        .droneId(result.getId()).pesoKg(10.00).volumeM3(1.00).build();

    PedidoDto pedidoDto = pedidoService.cadastrar(newPedidoDto);

    pedidoService.alterarStatus(pedidoDto.getId(), StatusPedidoEnum.EA);

    PedidoDto newPedidoDto2 = PedidoDto.builder().dataEntregaProgramada("10/11/2022 10:00")
        .duracaoDoPercurso((long) 60).enderecoDeEntrega("Avenida Rui Barbosa 364")
        .descricaoPedido("Nintendo Switch Oled 32gb").valorDoPedido(new BigDecimal(2599.00))
        .droneId(result.getId()).pesoKg(4.00).volumeM3(1.00).build();

    mockMvc
        .perform(put("/v1/pedido/" + pedidoDto.getId()).contentType(MediaType.APPLICATION_JSON)
            .content(new ObjectMapper().writeValueAsString(newPedidoDto2)))
        .andExpect(status().isConflict())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.error").value("Pedido em andamento"));
  }

  @WithMockUser(username = "dronefeeder")
  @Test
  @Order(14)
  @DisplayName("14 - Deve falhar ao alterar um pedido que está com status entregue.")
  void shoulfFailAlterarPedidoStatusEntregue() throws Exception {

    DroneDto result = droneService.cadastrar(newDroneDto);
    newPedidoDto = PedidoDto.builder().dataEntregaProgramada("10/11/2022 10:00")
        .duracaoDoPercurso((long) 60).enderecoDeEntrega("Avenida Rui Barbosa 506")
        .descricaoPedido("Nintendo Switch 32gb").valorDoPedido(new BigDecimal(2299.00))
        .droneId(result.getId()).pesoKg(10.00).volumeM3(1.00).build();

    PedidoDto pedidoDto = pedidoService.cadastrar(newPedidoDto);

    pedidoService.alterarStatus(pedidoDto.getId(), StatusPedidoEnum.EN);

    PedidoDto newPedidoDto2 = PedidoDto.builder().dataEntregaProgramada("10/11/2022 10:00")
        .duracaoDoPercurso((long) 60).enderecoDeEntrega("Avenida Rui Barbosa 364")
        .descricaoPedido("Nintendo Switch Oled 32gb").valorDoPedido(new BigDecimal(2599.00))
        .droneId(result.getId()).pesoKg(4.00).volumeM3(1.00).build();

    mockMvc
        .perform(put("/v1/pedido/" + pedidoDto.getId()).contentType(MediaType.APPLICATION_JSON)
            .content(new ObjectMapper().writeValueAsString(newPedidoDto2)))
        .andExpect(status().isConflict())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.error").value("Pedido entregue"));
  }

  @WithMockUser(username = "dronefeeder")
  @Test
  @Order(15)
  @DisplayName("15 - Deve falhar ao alterar um pedido colocando um drone que está inativo.")
  void shouldFailAlterarPedidoParaDroneInativo() throws Exception {

    DroneDto newDroneDto2 = DroneDto.builder().nome("Drone 02").marca("Drone&Cia")
        .fabricante("Drone&Cia").altitudeMax(1000.00).duracaoBateria(24).capacidadeKg(20.00)
        .capacidadeM3(10.00).build();
    DroneDto result = droneService.cadastrar(newDroneDto);
    DroneDto result2 = droneService.cadastrar(newDroneDto2);

    newPedidoDto = PedidoDto.builder().dataEntregaProgramada("10/11/2022 10:00")
        .duracaoDoPercurso((long) 60).enderecoDeEntrega("Avenida Rui Barbosa 506")
        .descricaoPedido("Nintendo Switch 32gb").valorDoPedido(new BigDecimal(2299.00))
        .droneId(result.getId()).pesoKg(10.00).volumeM3(1.00).build();

    PedidoDto pedidoDto = pedidoService.cadastrar(newPedidoDto);

    droneService.alterarStatus(result2.getId(), StatusDroneEnum.INATIVO);

    PedidoDto newPedidoDto2 = PedidoDto.builder().dataEntregaProgramada("11/11/2022 10:00")
        .duracaoDoPercurso((long) 60).enderecoDeEntrega("Avenida Rui Barbosa 364")
        .descricaoPedido("Nintendo Switch Oled 32gb").valorDoPedido(new BigDecimal(2599.00))
        .droneId(result2.getId()).pesoKg(4.00).volumeM3(1.00).build();

    mockMvc
        .perform(put("/v1/pedido/" + pedidoDto.getId()).contentType(MediaType.APPLICATION_JSON)
            .content(new ObjectMapper().writeValueAsString(newPedidoDto2)))
        .andExpect(status().isConflict())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.error").value("Drone inativo"));
  }

  @WithMockUser(username = "dronefeeder")
  @Test
  @Order(16)
  @DisplayName("16 - Deve falhar ao alterar o peso de um pedido para mais que o drone pode transportar.")
  void shouldFailAlterarPesoPedidoMaiorDoQuePesoDroneTransporta() throws Exception {

    DroneDto result = droneService.cadastrar(newDroneDto);
    newPedidoDto = PedidoDto.builder().dataEntregaProgramada("10/11/2022 10:00")
        .duracaoDoPercurso((long) 60).enderecoDeEntrega("Avenida Rui Barbosa 506")
        .descricaoPedido("Nintendo Switch 32gb").valorDoPedido(new BigDecimal(2299.00))
        .droneId(result.getId()).pesoKg(10.00).volumeM3(1.00).build();

    PedidoDto pedidoDto = pedidoService.cadastrar(newPedidoDto);

    PedidoDto newPedidoDto2 = PedidoDto.builder().dataEntregaProgramada("11/11/2022 10:00")
        .duracaoDoPercurso((long) 60).enderecoDeEntrega("Avenida Rui Barbosa 364")
        .descricaoPedido("Televisão Samsung").valorDoPedido(new BigDecimal(2599.00))
        .droneId(result.getId()).pesoKg(22.00).volumeM3(1.00).build();

    mockMvc
        .perform(put("/v1/pedido/" + pedidoDto.getId()).contentType(MediaType.APPLICATION_JSON)
            .content(new ObjectMapper().writeValueAsString(newPedidoDto2)))
        .andExpect(status().isConflict())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.error").value("Peso Excedido"));
  }

  @WithMockUser(username = "dronefeeder")
  @Test
  @Order(17)
  @DisplayName("17 - Deve falhar ao alterar o volume cúbico de um pedido para mais que o drone pode transportar.")
  void shouldFailAlterarVolumeCubicoMaiorDoQuePesoDroneTransporta() throws Exception {

    DroneDto result = droneService.cadastrar(newDroneDto);
    newPedidoDto = PedidoDto.builder().dataEntregaProgramada("10/11/2022 10:00")
        .duracaoDoPercurso((long) 60).enderecoDeEntrega("Avenida Rui Barbosa 506")
        .descricaoPedido("Nintendo Switch 32gb").valorDoPedido(new BigDecimal(2299.00))
        .droneId(result.getId()).pesoKg(10.00).volumeM3(1.00).build();

    PedidoDto pedidoDto = pedidoService.cadastrar(newPedidoDto);

    PedidoDto newPedidoDto2 = PedidoDto.builder().dataEntregaProgramada("11/11/2022 10:00")
        .duracaoDoPercurso((long) 60).enderecoDeEntrega("Avenida Rui Barbosa 364")
        .descricaoPedido("Televisão Samsung").valorDoPedido(new BigDecimal(2599.00))
        .droneId(result.getId()).pesoKg(19.00).volumeM3(11.00).build();

    mockMvc
        .perform(put("/v1/pedido/" + pedidoDto.getId()).contentType(MediaType.APPLICATION_JSON)
            .content(new ObjectMapper().writeValueAsString(newPedidoDto2)))
        .andExpect(status().isConflict())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.error").value("Volume Excedido"));
  }

}
