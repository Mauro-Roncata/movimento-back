package com.movimento.jade.back.repositories;

import com.movimento.jade.back.entities.Assinatura;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface AssinaturaRepository extends JpaRepository <Assinatura, UUID> {

    boolean existsByCpf(String cpf);

    boolean existsByTituloEleitor (String tituloEleitor);
}
