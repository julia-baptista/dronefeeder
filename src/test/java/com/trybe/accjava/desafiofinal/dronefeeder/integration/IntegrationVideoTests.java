

package com.trybe.accjava.desafiofinal.dronefeeder.integration;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import java.math.BigDecimal;
import com.trybe.accjava.desafiofinal.dronefeeder.dtos.DroneDto;
import com.trybe.accjava.desafiofinal.dronefeeder.dtos.PedidoDto;
import com.trybe.accjava.desafiofinal.dronefeeder.repository.DroneRepository;
import com.trybe.accjava.desafiofinal.dronefeeder.repository.PedidoRepository;
import com.trybe.accjava.desafiofinal.dronefeeder.repository.VideoRepository;
import com.trybe.accjava.desafiofinal.dronefeeder.service.DroneService;
import com.trybe.accjava.desafiofinal.dronefeeder.service.PedidoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class IntegrationVideoTests {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private DroneService droneService;

  @Autowired
  private PedidoService pedidoService;

  @SpyBean
  private VideoRepository videoRepository;

  @SpyBean
  private PedidoRepository pedidoRepository;

  @SpyBean
  private DroneRepository droneRepository;

  DroneDto newDroneDto = new DroneDto();

  PedidoDto newPedidoDto = new PedidoDto();

  @BeforeEach
  public void setup() {
    droneRepository.deleteAll();
    pedidoRepository.deleteAll();
    videoRepository.deleteAll();
    newDroneDto = new DroneDto();
    newPedidoDto = new PedidoDto();
    newDroneDto = DroneDto.builder().nome("Drone 01").marca("Drone&Cia").fabricante("Drone&Cia")
        .altitudeMax(1000.00).duracaoBateria(24).capacidadeKg(20.00).capacidadeM3(10.00).build();
    DroneDto droneDtoRetorno = droneService.cadastrar(newDroneDto);
    newPedidoDto = PedidoDto.builder().dataEntregaProgramada("10/11/2022 10:00")
        .duracaoDoPercurso((long) 60).enderecoDeEntrega("Avenida Rui Barbosa 506")
        .descricaoPedido("Nintendo Switch 32gb").valorDoPedido(new BigDecimal(2299.00))
        .droneId(droneDtoRetorno.getId()).pesoKg(4.00).volumeM3(1.00).build();
  }

  @WithMockUser(username = "dronefeeder")
  @Test
  @Order(1)
  @DisplayName("1 - Deve falhar ao salvar um vídeo para um pedido inexistente.")
  void shouldFailSalvarVideoParaPedidoInexistente() throws Exception {

    PedidoDto pedidoDtoRetorno = pedidoService.cadastrar(newPedidoDto);
    Long pedidoIdErrado = pedidoDtoRetorno.getId() + 1;
    String pedidoIdErradoString = pedidoIdErrado.toString();

    MockMultipartFile fileTest = new MockMultipartFile("file", "test-file.txt", "text/plain",
        "Teste Integração Upload Vídeo".getBytes());

    mockMvc
        .perform(MockMvcRequestBuilders.multipart("/v1/video/upload").file(fileTest)
            .param("pedidoId", pedidoIdErradoString).param("latitude", "200")
            .param("longitude", "200"))
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.error").value("Pedido não encontrado"));
  }


}
