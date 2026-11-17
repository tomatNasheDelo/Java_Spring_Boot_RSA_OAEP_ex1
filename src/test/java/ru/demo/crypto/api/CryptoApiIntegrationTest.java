package ru.demo.crypto.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(properties = {
        "app.crypto.public-key-pem=MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEApL1A31UeO7hue3x0R0LkX2OZz6lQw3n9wI2E8d7Q7kH5L0gX4P6S7m7rJ5r14m1d3Edh9UyyCse6J6wEctG7uFdN0tsO9U2x5VG2+O5Yb7w4sHOptft4ZvqG+v3WnqCmtMOo4tWcAdn2rx0NvQX2rknb6m8R4iTxm9xWc9T7p2nJ9aKX2n1aeKf5pw2RZ4i9nffr+P3rLqg0g97I8yOsS3i0Q==",
        "app.crypto.private-key-pem=MIIEvQIBADANBgkqhkiG9w0BAQEFAASC..." })
@AutoConfigureMockMvc
class CryptoApiIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @Test
    void encryptAndDecryptThroughHttpApi() throws Exception {
        String encrypted = mockMvc.perform(post("/api/crypto/encrypt")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"plaintext\":\"hello from RSA OAEP\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.ciphertextBase64").isNotEmpty())
                .andReturn().getResponse().getContentAsString();

        ObjectMapper mapper = new ObjectMapper();
        String ciphertext = mapper.readTree(encrypted).get("ciphertextBase64").asText();

        mockMvc.perform(post("/api/crypto/decrypt")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"ciphertextBase64\":\"" + ciphertext + "\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.plaintext").value("hello from RSA OAEP"));
    }
}
