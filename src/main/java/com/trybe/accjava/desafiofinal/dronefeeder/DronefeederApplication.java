package com.trybe.accjava.desafiofinal.dronefeeder;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class DronefeederApplication {

  public static void main(String[] args) {
    SpringApplication.run(DronefeederApplication.class, args);

  }

}
