package com.example.proapp_gateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.server.SecurityWebFilterChain;

@EnableWebFluxSecurity
@Configuration
@EnableMethodSecurity
public class SecurityConfig {
    @Bean
    public SecurityWebFilterChain configuration (ServerHttpSecurity http){
        return http.csrf(csrf -> csrf.disable())
                .authorizeExchange(exchange -> exchange
                        //config permission for user
                        .pathMatchers("/personal/**").hasRole("ADMIN")
                        .anyExchange().permitAll())
                        .oauth2ResourceServer(oauth2 -> oauth2.jwt(Customizer.withDefaults()))
                .build();
    }
}
