package com.trybe.accjava.desafiofinal.dronefeeder.util;

import java.time.LocalDateTime;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class DataUtilTest {

  @Test
  public void cenarioDataEntregaPedido1DentroDoIntervaloDoPedido2() {
    LocalDateTime p1DataHoraEntrega = LocalDateTime.now();
    LocalDateTime p1DataHoraSaida = p1DataHoraEntrega.minusHours(1);

    LocalDateTime p2DataHoraEntrega = p1DataHoraEntrega.plusMinutes(30);
    LocalDateTime p2DataHoraSaida = p1DataHoraSaida.plusMinutes(30);

    boolean result = DataUtil.isDataHoraPedidoSobreposta(p1DataHoraSaida, p1DataHoraEntrega,
        p2DataHoraSaida, p2DataHoraEntrega);

    Assertions.assertEquals(true, result);
  }


  @Test
  public void cenarioDataEntregaPedido2DentroDoIntervaloDoPedido1() {
    LocalDateTime p1DataHoraEntrega = LocalDateTime.now();
    LocalDateTime p1DataHoraSaida = p1DataHoraEntrega.minusHours(1);

    LocalDateTime p2DataHoraEntrega = p1DataHoraEntrega.minusMinutes(30);
    LocalDateTime p2DataHoraSaida = p1DataHoraSaida.minusMinutes(30);

    boolean result = DataUtil.isDataHoraPedidoSobreposta(p1DataHoraSaida, p1DataHoraEntrega,
        p2DataHoraSaida, p2DataHoraEntrega);

    Assertions.assertEquals(true, result);
  }

  @Test
  public void cenarioPedido2DentroDoIntervaloDoPedido1() {
    LocalDateTime p1DataHoraEntrega = LocalDateTime.now();
    LocalDateTime p1DataHoraSaida = p1DataHoraEntrega.minusHours(2);

    LocalDateTime p2DataHoraEntrega = p1DataHoraEntrega.minusMinutes(30);
    LocalDateTime p2DataHoraSaida = p1DataHoraSaida.plusMinutes(30);

    boolean result = DataUtil.isDataHoraPedidoSobreposta(p1DataHoraSaida, p1DataHoraEntrega,
        p2DataHoraSaida, p2DataHoraEntrega);

    Assertions.assertEquals(true, result);

  }

  @Test
  public void cenarioPedido1DentroDoIntervaloDoPedido2() {
    LocalDateTime p1DataHoraEntrega = LocalDateTime.now();
    LocalDateTime p1DataHoraSaida = p1DataHoraEntrega.minusHours(2);

    LocalDateTime p2DataHoraEntrega = p1DataHoraEntrega.plusMinutes(30);
    LocalDateTime p2DataHoraSaida = p1DataHoraSaida.minusMinutes(30);

    boolean result = DataUtil.isDataHoraPedidoSobreposta(p1DataHoraSaida, p1DataHoraEntrega,
        p2DataHoraSaida, p2DataHoraEntrega);

    Assertions.assertEquals(true, result);

  }

  @Test
  public void cenarioPedido1AntesDoPedido2() {
    LocalDateTime p1DataHoraEntrega = LocalDateTime.now();
    LocalDateTime p1DataHoraSaida = p1DataHoraEntrega.minusHours(1);

    LocalDateTime p2DataHoraSaida = p1DataHoraEntrega.plusMinutes(30);
    LocalDateTime p2DataHoraEntrega = p2DataHoraSaida.plusHours(1);

    boolean result = DataUtil.isDataHoraPedidoSobreposta(p1DataHoraSaida, p1DataHoraEntrega,
        p2DataHoraSaida, p2DataHoraEntrega);

    Assertions.assertEquals(false, result);
  }

  @Test
  public void cenarioPedido2AntesDoPedido1() {
    LocalDateTime p1DataHoraEntrega = LocalDateTime.now();
    LocalDateTime p1DataHoraSaida = p1DataHoraEntrega.minusHours(1);

    LocalDateTime p2DataHoraEntrega = p1DataHoraSaida.minusMinutes(30);
    LocalDateTime p2DataHoraSaida = p2DataHoraEntrega.minusHours(1);

    boolean result = DataUtil.isDataHoraPedidoSobreposta(p1DataHoraSaida, p1DataHoraEntrega,
        p2DataHoraSaida, p2DataHoraEntrega);

    Assertions.assertEquals(false, result);
  }
}
