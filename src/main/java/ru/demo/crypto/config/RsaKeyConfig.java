package ru.demo.crypto.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

@Configuration
public class RsaKeyConfig {
    @Bean
    public KeyPair rsaKeyPair(
            @Value("${app.crypto.public-key-pem:}") String publicKeyPem,
            @Value("${app.crypto.private-key-pem:}") String privateKeyPem
    ) {
        if (publicKeyPem == null || publicKeyPem.isBlank() || privateKeyPem == null || privateKeyPem.isBlank()) {
            throw new IllegalStateException("RSA keys are required. Set RSA_PUBLIC_KEY_PEM and RSA_PRIVATE_KEY_PEM environment variables.");
        }

        try {
            PublicKey publicKey = loadPublicKey(publicKeyPem);
            PrivateKey privateKey = loadPrivateKey(privateKeyPem);
            return new KeyPair(publicKey, privateKey);
        } catch (Exception ex) {
            throw new IllegalStateException("RSA keys are invalid. Provide PEM-encoded PKCS#8 private key and X.509 public key.", ex);
        }
    }

    private PublicKey loadPublicKey(String pem) throws Exception {
        String normalized = normalizePem(pem);
        byte[] decoded = Base64.getDecoder().decode(normalized);
        return KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(decoded));
    }

    private PrivateKey loadPrivateKey(String pem) throws Exception {
        String normalized = normalizePem(pem);
        byte[] decoded = Base64.getDecoder().decode(normalized);
        return KeyFactory.getInstance("RSA").generatePrivate(new PKCS8EncodedKeySpec(decoded));
    }

    private String normalizePem(String pem) {
        String cleaned = pem
                .replace("-----BEGIN PUBLIC KEY-----", "")
                .replace("-----END PUBLIC KEY-----", "")
                .replace("-----BEGIN PRIVATE KEY-----", "")
                .replace("-----END PRIVATE KEY-----", "")
                .replace("-----BEGIN RSA PRIVATE KEY-----", "")
                .replace("-----END RSA PRIVATE KEY-----", "")
                .replace("-----BEGIN RSA PUBLIC KEY-----", "")
                .replace("-----END RSA PUBLIC KEY-----", "")
                .replaceAll("\\s+", "");
        if (cleaned.isBlank()) {
            throw new IllegalStateException("PEM is empty");
        }
        return cleaned;
    }
}
