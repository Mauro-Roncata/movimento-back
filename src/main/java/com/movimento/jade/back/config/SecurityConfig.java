package com.movimento.jade.back.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // 1. Habilita o CORS no nível de segurança (Necessário para a anotação @CrossOrigin do Controller funcionar)
                .cors(Customizer.withDefaults())

                // 2. Desabilita a proteção CSRF. Isso é padrão e seguro para APIs REST
                .csrf(csrf -> csrf.disable())

                // 3. Configura as regras de acesso às URLs
                .authorizeHttpRequests(auth -> auth
                        // LIBERA o acesso público para a requisição POST na rota de assinaturas
                        .requestMatchers(HttpMethod.POST, "/api/assinaturas").permitAll()

                        // Libera a rota interna de erros do Spring Boot.
                        .requestMatchers("/error").permitAll()

                        // BLOQUEIA qualquer outra rota no futuro
                        .anyRequest().authenticated()
                )

                // 4. Define a API como "Stateless" (sem estado)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        return http.build();
    }
}