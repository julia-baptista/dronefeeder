package com.trybe.accjava.desafiofinal.dronefeeder.schedule;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import com.trybe.accjava.desafiofinal.dronefeeder.enums.StatusPedidoEnum;
import com.trybe.accjava.desafiofinal.dronefeeder.model.Pedido;
import com.trybe.accjava.desafiofinal.dronefeeder.service.PedidoService;
import lombok.extern.log4j.Log4j2;

@Component
@Log4j2
public class AgendadorPedido {

  private PedidoService service;

  public AgendadorPedido(PedidoService service) {
    this.service = service;
  }

  // https://medium.com/@nagireddygajjela19/spring-boot-cron-job-example-every-5-minutes-b2437e6ac068
  // https://crontab.guru/every-5-minutes
  // https://spring.io/blog/2020/11/10/new-in-spring-5-3-improved-cron-expressions
  // https://www.baeldung.com/cron-expressions
  // https://crontab.guru/examples.html
  // https://www.baeldung.com/spring-scheduled-tasks

  @Scheduled(cron = "0 */5 * ? * *")
  public void atualizarStatus() {
    log.info(String.format("Iniciando o agendador para atualização de status de pedido %s",
        LocalDateTime.now()));
    List<Pedido> pedidos =
        this.service.listaPedidosPorStatus(Arrays.asList(StatusPedidoEnum.AB, StatusPedidoEnum.EA));

    List<Pedido> pedidosAbertos =
        pedidos.stream().filter(pedido -> pedido.getStatus().equals(StatusPedidoEnum.AB))
            .collect(Collectors.toList());

    pedidosAbertos.stream().forEach(pedido -> {
      if (pedido.getDataProgramadaDaSaida().isBefore(LocalDateTime.now())) {
        service.alterarStatus(pedido.getId(), StatusPedidoEnum.EA);
      }
    });

    List<Pedido> pedidosEmAndamento =
        pedidos.stream().filter(pedido -> pedido.getStatus().equals(StatusPedidoEnum.EA))
            .collect(Collectors.toList());

    pedidosEmAndamento.stream().forEach(pedido -> {
      if (pedido.getDataEntregaProgramada().isBefore(LocalDateTime.now())
          && Objects.isNull(pedido.getDataConfirmacaoEntrega())) {
        service.alterarStatus(pedido.getId(), StatusPedidoEnum.AT);
      }
    });

    log.info(String.format("Finalizado o agendador para atualização de status de pedido %s",
        LocalDateTime.now()));
  }
}
