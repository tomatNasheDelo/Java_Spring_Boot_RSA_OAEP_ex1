package ru.demo.crypto.service;

import org.springframework.stereotype.Service;

import javax.crypto.Cipher;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Base64;

@Service
public class RsaOaepService {
    private static final String TRANSFORMATION = "RSA/ECB/OAEPWithSHA-256AndMGF1Padding";
    private final PublicKey publicKey;
    private final PrivateKey privateKey;

    public RsaOaepService(KeyPair keyPair) {
        this.publicKey = keyPair.getPublic();
        this.privateKey = keyPair.getPrivate();
    }

    public String encrypt(String plaintext) {
        try {
            Cipher cipher = Cipher.getInstance(TRANSFORMATION);
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);
            byte[] encrypted = cipher.doFinal(plaintext.getBytes(java.nio.charset.StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(encrypted);
        } catch (Exception ex) {
            throw new CryptoOperationException("RSA encryption failed", ex);
        }
    }

    public String decrypt(String ciphertextBase64) {
        try {
            byte[] encryptedBytes = Base64.getDecoder().decode(ciphertextBase64);
            Cipher cipher = Cipher.getInstance(TRANSFORMATION);
            cipher.init(Cipher.DECRYPT_MODE, privateKey);
            byte[] decrypted = cipher.doFinal(encryptedBytes);
            return new String(decrypted, java.nio.charset.StandardCharsets.UTF_8);
        } catch (IllegalArgumentException ex) {
            throw new CryptoOperationException("Ciphertext is not valid Base64", ex);
        } catch (Exception ex) {
            throw new CryptoOperationException("RSA decryption failed", ex);
        }
    }
}
