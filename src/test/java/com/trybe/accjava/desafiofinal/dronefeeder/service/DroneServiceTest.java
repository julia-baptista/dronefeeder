package com.trybe.accjava.desafiofinal.dronefeeder.service;

import static org.mockito.Mockito.times;
import com.trybe.accjava.desafiofinal.dronefeeder.dtos.DroneDto;
import com.trybe.accjava.desafiofinal.dronefeeder.enums.StatusDroneEnum;
import com.trybe.accjava.desafiofinal.dronefeeder.exception.DroneExistenteException;
import com.trybe.accjava.desafiofinal.dronefeeder.exception.ErroInesperadoException;
import com.trybe.accjava.desafiofinal.dronefeeder.model.Drone;
import com.trybe.accjava.desafiofinal.dronefeeder.repository.DroneRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest
public class DroneServiceTest {

  @MockBean
  private DroneRepository repository;

  @InjectMocks
  private DroneService service;

  DroneDto dto = new DroneDto();

  Drone drone = new Drone();

  @BeforeEach
  public void setup() {
    dto = DroneDto.builder().id(1L).nome("Drone A").fabricante("Drone Technology").build();

    drone.setId(1L);
    drone.setNome("Drone A");
    drone.setFabricante("Drone Technology");
    drone.setStatus(StatusDroneEnum.ATIVO);
  }

  @Test()
  public void shouldCadastrarNovoDrone() {


    Mockito.when(repository.existsByNome(Mockito.anyString())).thenReturn(false);
    Mockito.when(repository.save(Mockito.any())).thenReturn(drone);

    DroneDto result = service.cadastrar(dto);

    Assertions.assertNotNull(result);
    Assertions.assertEquals("Drone A", result.getNome());
  }

  @Test
  public void shouldFailCadastrarNovoDroneWhenDroneAlreadyExists() {
    Mockito.when(repository.existsByNome(Mockito.anyString())).thenReturn(true);

    // https://stackoverflow.com/questions/40268446/junit-5-how-to-assert-an-exception-is-thrown
    DroneExistenteException exception =
        Assertions.assertThrows(DroneExistenteException.class, () -> service.cadastrar(dto));

    Mockito.verify(repository, times(0)).save(Mockito.any());
    Assertions.assertNotNull(exception);
    Assertions.assertEquals("Drone Existente", exception.getMessage());

  }

  @Test
  public void shouldFailCadastrarNovoDroneWhenSomeUnexpectedErrorOccurs() {
    Mockito.when(repository.existsByNome(Mockito.anyString())).thenReturn(false);
    Mockito.when(repository.save(Mockito.any())).thenThrow(new IllegalArgumentException());

    ErroInesperadoException exception =
        Assertions.assertThrows(ErroInesperadoException.class, () -> service.cadastrar(dto));

    Mockito.verify(repository, times(1)).existsByNome(Mockito.anyString());
    Mockito.verify(repository, times(1)).save(Mockito.any());
    Assertions.assertNotNull(exception);
    Assertions.assertEquals("Erro inesperado", exception.getMessage());
  }
}
