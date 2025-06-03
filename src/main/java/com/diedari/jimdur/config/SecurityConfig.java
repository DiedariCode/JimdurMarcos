package com.diedari.jimdur.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.Customizer;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/admin/productos/**",
                                "/uploads/**",
                                "/admin/productos/api/**"
                                )
                        .permitAll()
                        .anyRequest().authenticated())
                .formLogin(Customizer.withDefaults()) // Usa el login por defecto
                .logout(logout -> logout
                        .permitAll());

        return http.build();
    }
}
