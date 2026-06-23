package com.movimento.jade.back.converters;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.util.Base64;

@Converter
public class CryptoConverter implements AttributeConverter<String, String> {

    private static final String ALGORITHM = "AES";

    // Puxa a chave de forma dinâmica do ambiente seguro
    private static final String ENV_KEY = System.getenv("JADE_ENCRYPTION_KEY");

    // Método auxiliar para garantir que a chave existe e tem o tamanho certo
    private Key getKey() {
        if (ENV_KEY == null || (ENV_KEY.length() != 16 && ENV_KEY.length() != 24 && ENV_KEY.length() != 32)) {
            throw new IllegalStateException("Erro Crítico: Variável de ambiente JADE_ENCRYPTION_KEY não configurada ou com tamanho inválido. (Use 16, 24 ou 32 caracteres).");
        }
        return new SecretKeySpec(ENV_KEY.getBytes(), ALGORITHM);
    }

    @Override
    public String convertToDatabaseColumn(String dadoSensivel) {
        if (dadoSensivel == null) {
            return null;
        }
        try {
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, getKey());

            byte[] dadoCriptografado = cipher.doFinal(dadoSensivel.getBytes());
            return Base64.getEncoder().encodeToString(dadoCriptografado);

        } catch (Exception e) {
            throw new RuntimeException("Falha ao criptografar o dado sensível para o banco de dados.", e);
        }
    }

    @Override
    public String convertToEntityAttribute(String dadoDoBanco) {
        if (dadoDoBanco == null) {
            return null;
        }
        try {
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, getKey());

            byte[] dadoDescriptografado = cipher.doFinal(Base64.getDecoder().decode(dadoDoBanco));
            return new String(dadoDescriptografado);

        } catch (Exception e) {
            throw new RuntimeException("Falha ao descriptografar o dado sensível vindo do banco de dados.", e);
        }
    }
}