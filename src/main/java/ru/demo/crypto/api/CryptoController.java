package ru.demo.crypto.api;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.demo.crypto.service.RsaOaepService;

@RestController
@RequestMapping("/api/crypto")
public class CryptoController {
    private final RsaOaepService cryptoService;

    public CryptoController(RsaOaepService cryptoService) {
        this.cryptoService = cryptoService;
    }

    @PostMapping("/encrypt")
    public EncryptResponse encrypt(@Valid @RequestBody EncryptRequest request) {
        return new EncryptResponse(cryptoService.encrypt(request.plaintext()));
    }

    @PostMapping("/decrypt")
    public DecryptResponse decrypt(@Valid @RequestBody DecryptRequest request) {
        return new DecryptResponse(cryptoService.decrypt(request.ciphertextBase64()));
    }
}
