package com.movimento.jade.back.services;

import com.movimento.jade.back.dtos.AssinaturaDTO;
import com.movimento.jade.back.entities.Assinatura;
import com.movimento.jade.back.repositories.AssinaturaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AssinaturaService {

    @Autowired
    private AssinaturaRepository assinaturaRepository;

    public Assinatura processarNovaAssinatura(AssinaturaDTO dto) {

        // 1. Regra de Negócio: Evitar duplicidade
        if (assinaturaRepository.existsByCpf(dto.getCpf())) {
            throw new RuntimeException("Este CPF já assinou o manifesto.");
        }
        if (assinaturaRepository.existsByTituloEleitor(dto.getTituloEleitor())) {
            throw new RuntimeException("Este Título de Eleitor já assinou o manifesto.");
        }

        // 2. Mapeamento: Transforma o DTO em Entidade
        Assinatura novaAssinatura = new Assinatura();
        novaAssinatura.setNomeCompleto(dto.getNomeCompleto());
        novaAssinatura.setCpf(dto.getCpf()); // O texto limpo entra aqui...
        novaAssinatura.setTituloEleitor(dto.getTituloEleitor());
        novaAssinatura.setCep(dto.getCep());
        novaAssinatura.setLogradouro(dto.getLogradouro());
        novaAssinatura.setBairro(dto.getBairro());

        // 3. Persistência: O Hibernate vai acionar o CryptoConverter automaticamente aqui!
        return assinaturaRepository.save(novaAssinatura);
    }
}
