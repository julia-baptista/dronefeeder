package com.trybe.accjava.desafiofinal.dronefeeder.controller;

import java.util.HashMap;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import com.trybe.accjava.desafiofinal.dronefeeder.exception.DataError;
import com.trybe.accjava.desafiofinal.dronefeeder.exception.DroneExistenteException;
import com.trybe.accjava.desafiofinal.dronefeeder.exception.DroneNaoEncontradoException;
import com.trybe.accjava.desafiofinal.dronefeeder.exception.ErroInesperadoException;
import com.trybe.accjava.desafiofinal.dronefeeder.exception.PedidoNaoEncontradoException;
import com.trybe.accjava.desafiofinal.dronefeeder.exception.PesoExcedidoException;
import com.trybe.accjava.desafiofinal.dronefeeder.exception.VolumeExcedidoException;
import com.trybe.accjava.desafiofinal.dronefeeder.exception.HorarioDoPedidoSobrepostoException;
import com.trybe.accjava.desafiofinal.dronefeeder.exception.DroneInativoException;
import com.trybe.accjava.desafiofinal.dronefeeder.exception.PedidoCanceladoException;
import com.trybe.accjava.desafiofinal.dronefeeder.exception.PedidoEmAndamentoException;
import com.trybe.accjava.desafiofinal.dronefeeder.exception.PedidoEntregueException;
import com.trybe.accjava.desafiofinal.dronefeeder.exception.PedidoEmAbertoException;
import com.trybe.accjava.desafiofinal.dronefeeder.exception.DronePossuiPedidosException;

/**
 * Classe GerenciadorAdvice.
 **/
@ControllerAdvice
public class GerenciadorAdvice {


  /**
   * Metodo handlerConflict.
   **/
  @ExceptionHandler({DroneExistenteException.class, PesoExcedidoException.class,
      VolumeExcedidoException.class, HorarioDoPedidoSobrepostoException.class,
      DroneInativoException.class, PedidoCanceladoException.class, PedidoEmAndamentoException.class,
      PedidoEntregueException.class, PedidoEmAbertoException.class,
      DronePossuiPedidosException.class})
  public ResponseEntity<DataError> handlerConflict(RuntimeException e) {
    // public ResponseEntity<Map<String,String>>

    DataError payload = new DataError(e.getMessage());
    // Map<String, String> payload = this.createErroResponse(e.getMessage());

    return ResponseEntity.status(HttpStatus.CONFLICT).body(payload);

  }

  /**
   * Metodo handlerInternalServerError.
   **/
  @ExceptionHandler(ErroInesperadoException.class)
  public ResponseEntity<DataError> handleInternalServer(RuntimeException e) {

    DataError payload = new DataError(e.getMessage());

    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(payload);

  }

  @ExceptionHandler({DroneNaoEncontradoException.class, PedidoNaoEncontradoException.class})
  public ResponseEntity<DataError> handle(RuntimeException e) {

    DataError payload = new DataError(e.getMessage());

    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(payload);
  }

  // @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<Map<String, String>> handleValidationExceptions(
      MethodArgumentNotValidException ex) {
    Map<String, String> errors = new HashMap<>();
    ex.getBindingResult().getAllErrors().forEach((error) -> {
      String fieldName = ((FieldError) error).getField();
      String errorMessage = error.getDefaultMessage();
      errors.put(fieldName, errorMessage);
    });
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
  }

}
