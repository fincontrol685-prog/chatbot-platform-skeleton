package com.br.chatbotplatformskeleton.util;

import com.google.common.io.BaseEncoding;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

/**
 * Utilitário para TOTP (Time-based One-Time Password)
 * Implementação compatível com Google Authenticator, Authy, Microsoft Authenticator, etc.
 */
public class TotpUtil {

    private static final String ALGORITHM = "HmacSHA1";
    private static final int TIME_STEP = 30;  // 30 seconds
    private static final int CODE_LENGTH = 6;

    /**
     * Gera um novo secret key para TOTP
     */
    public static String generateSecretKey() {
        SecureRandom random = new SecureRandom();
        byte[] bytes = new byte[20];
        random.nextBytes(bytes);
        return BaseEncoding.base32().encode(bytes);
    }

    /**
     * Valida um código TOTP contra um secret key
     */
    public static boolean validate(String secretKey, String code) {
        return validate(secretKey, code, 1);
    }

    /**
     * Valida um código TOTP com tolerância de janelas de tempo
     * @param secretKey Secret key em base32
     * @param code Código de 6 dígitos
     * @param window Número de períodos anteriores/posteriores a tolerar (default 1)
     */
    public static boolean validate(String secretKey, String code, int window) {
        try {
            long timeWindow = System.currentTimeMillis() / 1000 / TIME_STEP;
            for (int i = -window; i <= window; i++) {
                String expectedCode = generateCode(secretKey, timeWindow + i);
                if (expectedCode.equals(code)) {
                    return true;
                }
            }
            return false;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Gera um código TOTP para o momento atual
     */
    public static String generateCode(String secretKey) throws NoSuchAlgorithmException, InvalidKeyException {
        long timeWindow = System.currentTimeMillis() / 1000 / TIME_STEP;
        return generateCode(secretKey, timeWindow);
    }

    /**
     * Gera um código TOTP para uma janela de tempo específica
     */
    private static String generateCode(String secretKey, long timeWindow) throws NoSuchAlgorithmException, InvalidKeyException {
        byte[] decodedKey = BaseEncoding.base32().decode(secretKey);
        byte[] data = new byte[8];
        long value = timeWindow;
        for (int i = 8; i-- > 0; value >>>= 8) {
            data[i] = (byte) value;
        }

        SecretKeySpec signKey = new SecretKeySpec(decodedKey, 0, decodedKey.length, ALGORITHM);
        Mac mac = Mac.getInstance(ALGORITHM);
        mac.init(signKey);
        byte[] hash = mac.doFinal(data);

        int offset = hash[20 - 1] & 0xf;
        long truncatedHash = 0;
        for (int i = 0; i < 4; ++i) {
            truncatedHash <<= 8;
            truncatedHash |= (hash[offset + i] & 0xff);
        }

        truncatedHash &= 0x7fffffff;
        truncatedHash %= 1000000;

        return String.format("%0" + CODE_LENGTH + "d", truncatedHash);
    }

    /**
     * Gera a URL de QR Code para provisionamento em autenticadores
     */
    public static String getQrCodeUrl(String secretKey, String accountName, String issuer) {
        String format = "otpauth://totp/%s:%s?secret=%s&issuer=%s";
        return String.format(format, issuer, accountName, secretKey, issuer);
    }

    /**
     * Gera códigos de backup (para recuperação da conta)
     */
    public static String[] generateBackupCodes(int count) {
        String[] codes = new String[count];
        SecureRandom random = new SecureRandom();
        for (int i = 0; i < count; i++) {
            long code = 100000000L + random.nextLong() % 900000000L;
            codes[i] = String.valueOf(code);
        }
        return codes;
    }
}

