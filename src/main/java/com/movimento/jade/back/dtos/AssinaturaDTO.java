package com.movimento.jade.back.dtos;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.hibernate.validator.constraints.br.CPF;

@Data
public class AssinaturaDTO {
    @NotBlank(message = "O nome completo é obrigatório.")
    @Size(min = 2, max = 150, message = "O nome deve ter entre 2 e 150 caracteres.")
    private String nomeCompleto;

    @NotBlank(message = "O CPF é obrigatório.")
    @CPF(message = "CPF inválido.")
    private String cpf;

    @NotBlank(message = "O Título de Eleitor é obrigatório.")
    @Pattern(regexp = "\\d{12}", message = "O Título de Eleitor deve conter exatamente 12 números.")
    private String tituloEleitor;

    @NotBlank(message = "O CEP é obrigatório.")
    @Pattern(regexp = "\\d{5}-\\d{3}", message = "Formato de CEP inválido (use 00000-000).")
    private String cep;

    @NotBlank(message = "O logradouro é obrigatório.")
    private String logradouro;

    @NotBlank(message = "O bairro é obrigatório.")
    private String bairro;
}

