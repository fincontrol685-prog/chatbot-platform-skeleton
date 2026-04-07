package com.br.chatbotplatformskeleton.util;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

/**
 * Utilitário para encriptação/decriptação de dados sensíveis
 * Utiliza AES-256 para proteção de campos críticos
 */
public class EncryptionUtil {

    private static final String ALGORITHM = "AES";
    private static final int KEY_SIZE = 256;
    private static final String CIPHER_ALGORITHM = "AES";

    /**
     * Gera uma chave AES-256 baseada em uma senha master
     * Em produção, isso deve usar a master key do ambiente
     */
    public static SecretKey generateKey() throws NoSuchAlgorithmException {
        KeyGenerator keyGenerator = KeyGenerator.getInstance(ALGORITHM);
        keyGenerator.init(KEY_SIZE, new SecureRandom());
        return keyGenerator.generateKey();
    }

    /**
     * Encripta um texto usando a chave fornecida
     */
    public static String encrypt(String plainText, SecretKey key) throws Exception {
        Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, key);
        byte[] encryptedBytes = cipher.doFinal(plainText.getBytes());
        return Base64.getEncoder().encodeToString(encryptedBytes);
    }

    /**
     * Decripta um texto usando a chave fornecida
     */
    public static String decrypt(String encryptedText, SecretKey key) throws Exception {
        Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, key);
        byte[] decodedBytes = Base64.getDecoder().decode(encryptedText);
        byte[] decryptedBytes = cipher.doFinal(decodedBytes);
        return new String(decryptedBytes);
    }

    /**
     * Reconstrói uma chave a partir de bytes codificados (para uso em runtime)
     */
    public static SecretKey reconstructKey(byte[] encodedKey) {
        return new SecretKeySpec(encodedKey, 0, encodedKey.length, ALGORITHM);
    }
}

