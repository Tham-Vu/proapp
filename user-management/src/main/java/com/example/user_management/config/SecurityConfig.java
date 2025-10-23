package com.example.user_management.config;

import com.example.user_management.service.serviceimpl.UserDetailsServiceImpl;
import com.example.user_management.service.serviceimpl.UserServiceImpl;
import com.example.user_management.utils.JwtFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableWebSecurity
@Configuration
@EnableMethodSecurity
public class SecurityConfig {
    private final JwtFilter filter;

    public SecurityConfig(JwtFilter filter) {
        this.filter = filter;
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
    @Bean
    protected UserDetailsService userDetailsService(){
        return new UserDetailsServiceImpl();
    }
    @Bean
    public AuthenticationManager authenticationManagerBean(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder.userDetailsService(userDetailsService()).passwordEncoder(passwordEncoder());
        return authenticationManagerBuilder.build();
    }
    @Bean
    protected SecurityFilterChain configuration(HttpSecurity http) throws Exception{
        http.cors(cors -> cors.disable()).csrf(csrf-> csrf.disable()).authorizeHttpRequests(request ->
                    request.requestMatchers("/api/login").permitAll()
                            .requestMatchers("/api/refresh-toke").permitAll()
                            .requestMatchers("/api/logout").permitAll()
//                            users api
//                            .requestMatchers(HttpMethod.GET, "/api/users").hasRole("GET_LIST_USERS")
//                            .requestMatchers(HttpMethod.POST, "/api/users").hasRole("CREATE_USER")
//                            .requestMatchers(HttpMethod.PUT,"/api/users").hasRole("UPDATE_USER")
//                            .requestMatchers(HttpMethod.DELETE, "/api/users").hasRole("DELETE_USER")
////                          groups api
//                            .requestMatchers(HttpMethod.GET, "/api/groups").hasRole("GET_LIST_GROUPS")
//                            .requestMatchers(HttpMethod.POST, "/api/groups").hasRole("CREATE_GROUP")
//                            .requestMatchers(HttpMethod.PUT,"/api/groups").hasRole("UPDATE_GROUP")
//                            .requestMatchers(HttpMethod.DELETE, "/api/groups").hasRole("DELETE_GROUP")
////                          permissions
//                            .requestMatchers(HttpMethod.GET, "/api/permissions").hasRole("GET_LIST_PERMISSIONS")
//                            .requestMatchers(HttpMethod.POST, "/api/permissions").hasRole("CREATE_PERMISSION")
//                            .requestMatchers(HttpMethod.PUT,"/api/permissions").hasRole("UPDATE_PERMISSION")
//                            .requestMatchers(HttpMethod.DELETE, "/api/permissions").hasRole("DELETE_PERMISSION")
                            .anyRequest().permitAll()

        );
        http.addFilterBefore(filter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

}
