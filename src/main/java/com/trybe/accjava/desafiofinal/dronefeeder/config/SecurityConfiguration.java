package com.trybe.accjava.desafiofinal.dronefeeder.config;

import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true, jsr250Enabled = true)
public class SecurityConfiguration { 

  //Como desabilitar as urls do actuator da autenticação do spring security
  //https://www.baeldung.com/spring-deprecated-websecurityconfigureradapter
  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

    http.csrf()
    .disable()
    .authorizeRequests()
    .antMatchers("/actuator/**")
    .anonymous()
    .anyRequest()
    .authenticated()
    .and()
    .httpBasic()
    .and()
    .sessionManagement()
    .sessionCreationPolicy(SessionCreationPolicy.STATELESS);

  return http.build();

  }
}
