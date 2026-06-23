package com.movimento.jade.back.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;
import com.movimento.jade.back.converters.CryptoConverter;

@Data
@Entity
@Table(name = "assinaturas")
public class Assinatura {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, length = 150)
    private String nomeCompleto;

    // O Converter aplica o algoritmo AES-256 antes de salvar no banco
    @Convert(converter = CryptoConverter.class)
    @Column(nullable = false, unique = true)
    private String cpf;

    @Convert(converter = CryptoConverter.class)
    @Column(nullable = false, unique = true)
    private String tituloEleitor;

    @Column(nullable = false, length = 9)
    private String cep;

    @Column(nullable = false)
    private String logradouro;

    @Column(nullable = false)
    private String bairro;

    // Fixo para Ijuí, mas bom ter para manter o formato de endereço completo
    @Column(nullable = false)
    private String cidade = "Ijuí";

    @Column(nullable = false)
    private String uf = "RS";

    @Column(nullable = false, updatable = false)
    private LocalDateTime dataAssinatura = LocalDateTime.now();
}
