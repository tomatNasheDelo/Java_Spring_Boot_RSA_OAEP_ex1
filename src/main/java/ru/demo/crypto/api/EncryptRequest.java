package ru.demo.crypto.api;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record EncryptRequest(
        @NotBlank @Size(max = 245) String plaintext
) {
}
