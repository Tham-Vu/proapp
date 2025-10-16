package com.example.user_management.config;

import com.example.user_management.utils.JwtFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfiguration;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.server.SecurityWebFilterChain;

@EnableWebSecurity
@Configuration
@EnableMethodSecurity
public class SecurityConfig {
    @Autowired
    private JwtFilter filter;
    @Bean
    protected SecurityFilterChain configuration(HttpSecurity http) throws Exception{
        http.cors(cors -> cors.disable()).csrf(csrf-> csrf.disable()).authorizeHttpRequests(request -> request.anyRequest().permitAll());
        http.addFilterBefore(filter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

}
