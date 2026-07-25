package com.movimento.jade.back.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
public class CaptchaService {

    // O Spring Boot vai injetar a chave real que está no application.properties aqui!
    @Value("${TURNSTILE_SITE_KEY}")
    private String secretKey;

    private static final String VERIFY_URL = "https://challenges.cloudflare.com/turnstile/v0/siteverify";

    private final RestTemplate restTemplate;

    public CaptchaService() {
        this.restTemplate = new RestTemplate();
    }

    public boolean isCaptchaValido(String captchaToken) {
        if (captchaToken == null || captchaToken.isEmpty()) {
            return false;
        }

        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

            MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
            // Usando a chave que veio do application.properties
            map.add("secret", secretKey);
            map.add("response", captchaToken);

            HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);

            // O Java faz uma chamada POST escondida para o Cloudflare
            ResponseEntity<Map> response = restTemplate.postForEntity(VERIFY_URL, request, Map.class);
            Map<String, Object> body = response.getBody();

            // O Cloudflare devolve um JSON com um campo booleano "success"
            if (body != null && Boolean.TRUE.equals(body.get("success"))) {
                return true;
            }
        } catch (Exception e) {
            System.err.println("Erro ao validar o Captcha com o Cloudflare: " + e.getMessage());
        }

        return false; // Se a requisição falhar ou o success for false, consideramos o bot bloqueado
    }
}