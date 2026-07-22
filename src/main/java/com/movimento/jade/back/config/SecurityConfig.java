package com.movimento.jade.back.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

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

    // 5. BLINDAGEM DO CORS (Aceita apenas o meu front)
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        // Dominios com permissão para fazer a req para API
        configuration.setAllowedOrigins(List.of(
                "http://localhost:5173",
                "https://movimentojade.com.br" // URL de prod
        ));

        // Métodos HTTP permitidos
        configuration.setAllowedMethods(List.of("POST", "OPTIONS"));

        // Libera o envio de cabeçalhos (como o Content-Type: application/json)
        configuration.setAllowedHeaders(List.of("*"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        // Aplica essa regra para todas as rotas da API
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
