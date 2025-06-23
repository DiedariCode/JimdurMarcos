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
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.diedari.jimdur.service.VistaService;

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
        public SecurityFilterChain securityFilterChain(HttpSecurity http,
                        VistaService vistaService,
                        FiltroAutorizacionDinamica filtroAutorizacionDinamica) throws Exception {

                http
                                .authorizeHttpRequests(authorize -> {
                                        // Rutas públicas
                                        authorize
                                                        .requestMatchers(
                                                                        "/uploads/**",
                                                                        "/api/verification/**",
                                                                        "/user/registro",
                                                                        "/css/**",
                                                                        "/js/**",
                                                                        "/image/**",
                                                                        "/img/**",
                                                                        "/",
                                                                        "/productos/**",
                                                                        "/user/contacto",
                                                                        "/user/nosotros")
                                                        .permitAll();

                                        // Todo lo demás requiere autenticación
                                        authorize.anyRequest().authenticated();
                                })
                                // 🔐 Agregar filtro dinámico después del filtro de login
                                .addFilterAfter(filtroAutorizacionDinamica, UsernamePasswordAuthenticationFilter.class)

                                // Configuración de login
                                .formLogin(form -> form
                                                .loginPage("/user/login")
                                                .loginProcessingUrl("/user/login")
                                                .defaultSuccessUrl("/", true)
                                                .failureUrl("/user/login?error=true")
                                                .permitAll())
                                .logout(logout -> logout
                                                .logoutUrl("/user/logout")
                                                .logoutSuccessUrl("/")
                                                .permitAll())
                                .csrf(csrf -> csrf
                                                .ignoringRequestMatchers("/api/**", "/admin/**"));

                return http.build();
        }
}
