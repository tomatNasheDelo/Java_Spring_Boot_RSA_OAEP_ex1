package ru.demo.crypto.api;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record DecryptRequest(
        @NotBlank @Size(max = 5000) String ciphertextBase64
) {
}
