# Spring Boot RSA-OAEP demo

Демопроект показывает асимметричное шифрование через Java Cryptography Architecture (JCA) и RSA-OAEP. Время шифрования и шифротекст, возвращаемый API, демонстрируют, как работает ключевая пара: public key для шифрования, private key для расшифровки.

## Быстрый старт

Требуются JDK 17+ и Gradle 8+.

Нужно заранее сгенерировать ключевую пару и передать её через переменные окружения:

```bash
# Generate PEM-encoded RSA 2048-bit key pair
openssl genrsa -out private.pem 2048
openssl rsa -in private.pem -pubout -out public.pem
```

Затем экспортируйте содержимое в env:

```bash
export RSA_PUBLIC_KEY_PEM="$(cat public.pem)"
export RSA_PRIVATE_KEY_PEM="$(cat private.pem)"
gradle bootRun
```

> Никогда не коммитьте приватный ключ. Используйте secret manager, env vars или KMS.

## API

Зашифровать:

```bash
curl -sS -X POST http://localhost:8080/api/crypto/encrypt \
  -H 'Content-Type: application/json' \
  -d '{"plaintext":"Hello RSA OAEP"}'
```

Расшифровать:

```bash
curl -sS -X POST http://localhost:8080/api/crypto/decrypt \
  -H 'Content-Type: application/json' \
  -d '{"ciphertextBase64":"<base64 ciphertext>"}'
```

## Что изучаем

- `RSA/ECB/OAEPWithSHA-256AndMGF1Padding`
- public/private key pair
- чёткий раздел обязанностей: шифровать — public key, расшифровывать — private key
- ограничения OAEP: подходит для небольших данных; для больших сообщений лучше комбинировать с симметричным ключом
- важность безопасного хранения приватного ключа

Следующий уровень после этого:
- RSA + AES hybrid flow
- ECDSA / RSA signature validation
- Certificate management and PKI basics
