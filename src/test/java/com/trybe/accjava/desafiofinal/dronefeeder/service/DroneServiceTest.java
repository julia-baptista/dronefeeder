package com.trybe.accjava.desafiofinal.dronefeeder.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import com.trybe.accjava.desafiofinal.dronefeeder.exception.DroneExistenteException;
import com.trybe.accjava.desafiofinal.dronefeeder.model.Drone;
import com.trybe.accjava.desafiofinal.dronefeeder.repository.DroneRepository;

@SpringBootTest
public class DroneServiceTest {

  @Mock
  private DroneRepository repository;

  @InjectMocks
  private DroneService service;

  Drone drone = new Drone();

  @BeforeEach
  public void setup() {
    drone = new Drone();
    drone.setId(1L);
    drone.setNome("Drone A");
    drone.setFabricante("Natives drone");
  }

  @Test()
  public void shouldCadastrarNovoDrone() {
    Mockito.when(repository.existsByNome(Mockito.anyString())).thenReturn(false);
    Mockito.when(repository.save(Mockito.any())).thenReturn(drone);

    Drone result = service.cadastrar(drone);

    Assertions.assertNotNull(result);
    Assertions.assertEquals("Drone A", result.getNome());
  }

  @Test
  public void shouldFailCadastrarNovoDroneWhenDroneAlreadyExists() {
    Mockito.when(repository.existsByNome(Mockito.anyString())).thenReturn(true);

    // https://stackoverflow.com/questions/40268446/junit-5-how-to-assert-an-exception-is-thrown
    DroneExistenteException exception =
        Assertions.assertThrows(DroneExistenteException.class, () -> service.cadastrar(drone));

    Assertions.assertNotNull(exception);
    Assertions.assertEquals("Drone Existente", exception.getMessage());

  }
}
