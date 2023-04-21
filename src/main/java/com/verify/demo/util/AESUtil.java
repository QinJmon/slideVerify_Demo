package com.verify.demo.util;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Base64;

public class AESUtil {
    private static final String AES_TRANSFORMATION = "AES/ECB/PKCS5Padding";
    private static final String AES_ALGORITHM = "AES";
    private static final String CHARSET = StandardCharsets.UTF_8.name();

    public static String encrypt(String data, String key) throws Exception {
        Cipher cipher = Cipher.getInstance(AES_TRANSFORMATION);
        cipher.init(Cipher.ENCRYPT_MODE, generateSecretKey(key));
        byte[] encryptedData = cipher.doFinal(data.getBytes(CHARSET));
        return Base64.getEncoder().encodeToString(encryptedData);
    }

    public static String decrypt(String encryptedData, String key) throws Exception {
        byte[] decodedData = Base64.getDecoder().decode(encryptedData);
        Cipher cipher = Cipher.getInstance(AES_TRANSFORMATION);
        cipher.init(Cipher.DECRYPT_MODE, generateSecretKey(key));
        byte[] decryptedData = cipher.doFinal(decodedData);
        return new String(decryptedData, CHARSET);
    }

    private static SecretKey generateSecretKey(String key) throws Exception {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        byte[] keyBytes = md.digest(key.getBytes(CHARSET));
        return new SecretKeySpec(keyBytes, AES_ALGORITHM);
    }
}
