package com.trybe.accjava.desafiofinal.dronefeeder.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class DataUtil {

  // start1 = p1.dataProgramadaDeSaida
  // end1 = p1.dataEntregaProgramada
  // start2 = p2.dataProgramadaDeSaida
  // end2 = p2.dataEntregaProgramada
  public static boolean isDataHoraPedidoSobreposta(LocalDateTime startp1, LocalDateTime endp1,
      LocalDateTime startp2, LocalDateTime endp2) {
    return startp1.isBefore(endp2) && startp2.isBefore(endp1);
  }

  public static LocalDateTime geraDataSaidaProgramada(Long duracaoPercurso,
      LocalDateTime dataEntregaProgramada) {
    return dataEntregaProgramada.minusMinutes(duracaoPercurso);
  }

  // https://www.java67.com/2016/04/how-to-convert-string-to-localdatetime-in-java8-example.html
  public static LocalDateTime converteStringParaData(String data) {
    // String data = "04/03/2022 11:30";
    LocalDateTime dataHora = null;
    if (Objects.nonNull(data) && !data.isEmpty()) {
      DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
      dataHora = LocalDateTime.parse(data, formatter);
    }
    return dataHora;
  }

  // https://attacomsian.com/blog/java-format-localdatetime
  public static String converteDataParaString(LocalDateTime data) {
    String dataString = "";
    if (Objects.nonNull(data)) {
      dataString = data.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
    }
    return dataString;
  }



}
