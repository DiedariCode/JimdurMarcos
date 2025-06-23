package com.diedari.jimdur.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

        @Bean
        public PasswordEncoder passwordEncoder() {
                return new BCryptPasswordEncoder();
        }

        @Bean
        public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
                return config.getAuthenticationManager();
        }

        @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
                http
                                .authorizeHttpRequests(auth -> auth
                                                .requestMatchers(
                                                                "/uploads/**",
                                                                "/api/**",
                                                                "/user/**",
                                                                "/productos/**",
                                                                "/user/registro",
                                                                "/css/**",
                                                                "/js/**",
                                                                "/image/**",
                                                                "/img/**",
                                                                "/").permitAll()
                                                .requestMatchers("/admin/**").hasRole("ADMIN")
                                                .anyRequest().authenticated())
                                .formLogin(form -> form
                                                .loginPage("/user/login") // tu login web personalizado
                                                .loginProcessingUrl("/user/login") // POST del formulario
                                                .defaultSuccessUrl("/", true)
                                                .failureUrl("/user/login?error=true")
                                                .permitAll())
                                .logout(logout -> logout
                                                .logoutUrl("/user/logout")
                                                // .logoutSuccessUrl("/auth/login?logout=true")
                                                .logoutSuccessUrl("/")
                                                .permitAll())
                                .csrf(csrf -> csrf
                                                .ignoringRequestMatchers("/api/verification/**", 
                                                                       "/admin/productos/api/**",
                                                                       "/admin/productos/crear",
                                                                       "/admin/productos/editar/**")); // Ignorar CSRF para API

                return http.build();
        }
}
