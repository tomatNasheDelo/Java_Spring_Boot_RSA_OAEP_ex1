package ru.demo.crypto.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.security.KeyPair;
import java.security.KeyPairGenerator;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class RsaOaepServiceTest {
    private RsaOaepService service;

    @BeforeEach
    void setUp() throws Exception {
        KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
        generator.initialize(2048);
        KeyPair keyPair = generator.generateKeyPair();
        service = new RsaOaepService(keyPair);
    }

    @Test
    void encryptThenDecryptRestoresText() {
        String plaintext = "Hello RSA OAEP with JCA";
        String ciphertext = service.encrypt(plaintext);
        assertNotEquals(plaintext, ciphertext);
        assertEquals(plaintext, service.decrypt(ciphertext));
    }

    @Test
    void decryptRejectsInvalidBase64() {
        assertThrows(CryptoOperationException.class, () -> service.decrypt("not-base64"));
    }
}
