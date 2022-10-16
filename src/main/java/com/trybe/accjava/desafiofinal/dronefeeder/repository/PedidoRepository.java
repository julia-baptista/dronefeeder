package com.trybe.accjava.desafiofinal.dronefeeder.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.trybe.accjava.desafiofinal.dronefeeder.model.Pedido;

/**
 * Repository.
 */
@Repository
public interface PedidoRepository extends JpaRepository<Pedido, Long> {

}
