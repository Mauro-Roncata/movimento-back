package com.movimento.jade.back.controllers;


import com.movimento.jade.back.dtos.AssinaturaDTO;
import com.movimento.jade.back.services.AssinaturaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/assinaturas")
@CrossOrigin(origins = "*") //Limitar apenas para o IP do front
public class AssinaturaController {
    @Autowired
    private AssinaturaService assinaturaService;

    @PostMapping
    public ResponseEntity<String> registrarAssinatura(@Valid @RequestBody AssinaturaDTO assinaturaDTO) {
        try {
            assinaturaService.processarNovaAssinatura(assinaturaDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body("Assinatura registrada com sucesso!");
        } catch (RuntimeException e) {
            // Captura os erros de duplicidade que lançamos no Service
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }
}
