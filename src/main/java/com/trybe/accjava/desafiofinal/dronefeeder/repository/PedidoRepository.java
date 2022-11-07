package com.trybe.accjava.desafiofinal.dronefeeder.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.trybe.accjava.desafiofinal.dronefeeder.enums.StatusPedidoEnum;
import com.trybe.accjava.desafiofinal.dronefeeder.model.Pedido;

/**
 * Repository.
 */
@Repository
public interface PedidoRepository extends JpaRepository<Pedido, Long> {

  public List<Pedido> findByStatusIn(List<StatusPedidoEnum> status);

}
