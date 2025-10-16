package com.example.proapp_gateway.config;

import com.example.proapp_gateway.utils.jwt.JwtFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {
    @Autowired
    private JwtFilter filter;
    @Bean
    public SecurityWebFilterChain configuration (ServerHttpSecurity http){
        return http.csrf(csrf -> csrf.disable())
                .authorizeExchange(exchange -> exchange
                        .pathMatchers("/user/api/login").permitAll()
                        .pathMatchers("/user/api/refresh-token").permitAll()
                        .pathMatchers("/user/**").permitAll()
//                        config permission for user
//                        .pathMatchers("/personal/**").hasRole("ADMIN")

                        .anyExchange().permitAll())
                .addFilterBefore(filter, SecurityWebFiltersOrder.AUTHENTICATION)
//                        .oauth2ResourceServer(oauth2 -> oauth2.jwt(Customizer.withDefaults()))
                .build();
    }
}
