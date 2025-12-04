package com.soluciona.soluciona.config;

import com.soluciona.soluciona.config.JwtAuthFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter; // 2. IMPORTA ESTE FILTRO

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private JwtAuthFilter jwtAuthFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                .csrf(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)

                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                .authorizeHttpRequests(authorize -> authorize

                        // --- ENDPOINTS PÚBLICOS (Não precisam de token) ---
                        .requestMatchers(HttpMethod.POST, "/api/profiles").permitAll()         // Registar Perfil
                        .requestMatchers(HttpMethod.GET, "/api/profiles/**").permitAll()      // Ver Perfis
                        .requestMatchers(HttpMethod.GET, "/api/categories/**").permitAll()    // Ver Categorias
                        .requestMatchers(HttpMethod.GET, "/api/services/**").permitAll()      // Ver Anúncios (Serviços)

                        // --- ENDPOINTS PRIVADOS (Precisam de token JWT) ---
                        .requestMatchers(HttpMethod.PUT, "/api/profiles/**").authenticated()    // Atualizar Perfil
                        .requestMatchers(HttpMethod.DELETE, "/api/profiles/**").authenticated() // Deletar Perfil

                        .requestMatchers(HttpMethod.POST, "/api/services").authenticated()      // <-- Criar Anúncio
                        .requestMatchers(HttpMethod.PUT, "/api/services/**").authenticated()    // <-- Atualizar Anúncio
                        .requestMatchers(HttpMethod.DELETE, "/api/services/**").authenticated() // <-- Deletar Anúncio

                        .requestMatchers("/api/requests/**").authenticated()                  // Tudo de Solicitações
                        .requestMatchers(HttpMethod.DELETE, "/api/categories/**").authenticated() // Deletar Categorias

                        // Qualquer outra coisa, tem de estar autenticado
                        .anyRequest().authenticated()
                )

                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}