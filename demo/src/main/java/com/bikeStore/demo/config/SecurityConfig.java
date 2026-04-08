package com.bikeStore.demo.config;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.security.config.Customizer;
import org.springframework.http.HttpMethod;

import java.util.Arrays;
import java.util.List;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .cors(Customizer.withDefaults())
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth

                        // ── Zona Pública ──────────────────────────────────────
                        .requestMatchers("/api/auth/**").permitAll()
                        .requestMatchers(
                                "/v3/api-docs", "/v3/api-docs/**",
                                "/api-docs", "/api-docs/**",
                                "/swagger-ui/**", "/swagger-ui.html",
                                "/docs", "/error"
                        ).permitAll()

                        // ── ADMIN: Gestión de Datos Maestros ──────────────────
                        // Usuarios (CRUD completo - solo admin crea cuentas)
                        .requestMatchers("/api/usuarios/**").hasRole("ADMIN")
                        // Roles (CRUD completo)
                        .requestMatchers("/api/roles/**").hasRole("ADMIN")
                        // Bicicletas: crear, modificar, eliminar → ADMIN
                        .requestMatchers(HttpMethod.POST, "/api/bicicletas").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/bicicletas/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/bicicletas/**").hasRole("ADMIN")
                        // Proveedores: crear, modificar, activar/desactivar → ADMIN
                        .requestMatchers(HttpMethod.POST, "/api/proveedores").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/proveedores/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PATCH, "/api/proveedores/**").hasRole("ADMIN")
                        // Reportes → solo ADMIN
                        .requestMatchers("/api/reportes/**").hasRole("ADMIN")

                        // ── EMPLEADO + ADMIN: Operativa y Lectura ─────────────
                        // Bicicletas: lectura → cualquier autenticado
                        .requestMatchers(HttpMethod.GET, "/api/bicicletas", "/api/bicicletas/**").authenticated()
                        // Proveedores: lectura → cualquier autenticado
                        .requestMatchers(HttpMethod.GET, "/api/proveedores", "/api/proveedores/**").authenticated()
                        // Entradas (compras a proveedores) → cualquier autenticado
                        .requestMatchers("/api/entradas/**").authenticated()
                        // Ventas → cualquier autenticado
                        .requestMatchers("/api/ventas/**").authenticated()
                        // Salidas → cualquier autenticado
                        .requestMatchers("/api/salidas/**").authenticated()
                        // Detalles de venta → cualquier autenticado
                        .requestMatchers("/api/detalles-venta/**").authenticated()

                        .anyRequest().authenticated()
                )
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint((request, response, authException) -> {
                            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                            response.setContentType("application/json");
                            response.getWriter().write("{\"error\": \"No autenticado\", \"message\": \""
                                    + authException.getMessage() + "\"}");
                        })
                        .accessDeniedHandler((request, response, accessDeniedException) -> {
                            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                            response.setContentType("application/json");
                            response.getWriter().write("{\"error\": \"Acceso denegado\", \"message\": \""
                                    + accessDeniedException.getMessage() + "\"}");
                        }))
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return httpSecurity.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOriginPatterns(List.of("*"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"));
        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Cache-Control", "Content-Type"));
        configuration.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
