package com.diedari.jimdur.config;

import com.diedari.jimdur.model.Rol;
import com.diedari.jimdur.model.Vista;
import com.diedari.jimdur.service.VistaService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import java.util.List;

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
        public SecurityFilterChain securityFilterChain(HttpSecurity http, VistaService vistaService) throws Exception {
                http
                                .authorizeHttpRequests(authorize -> {
                                        // Permisos públicos
                                        authorize
                                                        .requestMatchers(
                                                                        "/uploads/**",
                                                                        "/api/verification/**", // API de verificación es pública
                                                                        "/user/registro",
                                                                        "/css/**",
                                                                        "/js/**",
                                                                        "/image/**",
                                                                        "/img/**",
                                                                        "/",
                                                                        "/productos/**", // Permitir ver productos a todos
                                                                        "/user/contacto",
                                                                        "/user/nosotros")
                                                        .permitAll();

                                        // Reglas dinámicas desde la BD
                                        List<Vista> vistas = vistaService.findAllWithRoles();
                                        for (Vista vista : vistas) {
                                                if (vista.getPath() != null && !vista.getRoles().isEmpty()) {
                                                        String[] roles = vista.getRoles().stream().map(Rol::getNombre)
                                                                        .toArray(String[]::new);
                                                        authorize.requestMatchers(vista.getPath()).hasAnyRole(roles);
                                                }
                                        }

                                        // Cualquier otra petición requiere autenticación
                                        authorize.anyRequest().authenticated();
                                })
                                .formLogin(form -> form
                                                .loginPage("/user/login") // tu login web personalizado
                                                .loginProcessingUrl("/user/login") // POST del formulario
                                                .defaultSuccessUrl("/", true)
                                                .failureUrl("/user/login?error=true")
                                                .permitAll())
                                .logout(logout -> logout
                                                .logoutUrl("/user/logout")
                                                .logoutSuccessUrl("/")
                                                .permitAll())
                                .csrf(csrf -> csrf
                                                .ignoringRequestMatchers("/api/**",
                                                                "/admin/**"));

                return http.build();
        }
}
