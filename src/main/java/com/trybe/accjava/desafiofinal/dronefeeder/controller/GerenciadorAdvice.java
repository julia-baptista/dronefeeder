package com.trybe.accjava.desafiofinal.dronefeeder.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import com.trybe.accjava.desafiofinal.dronefeeder.exception.DataError;
import com.trybe.accjava.desafiofinal.dronefeeder.exception.DroneExistenteException;
import com.trybe.accjava.desafiofinal.dronefeeder.exception.ErroInesperadoException;

/**
 * Classe GerenciadorAdvice.
 **/
@ControllerAdvice
public class GerenciadorAdvice {


  /**
   * Metodo handlerConflict.
   **/
  @ExceptionHandler(DroneExistenteException.class)
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

}
