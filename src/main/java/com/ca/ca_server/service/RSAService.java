package com.ca.ca_server.service;

import org.springframework.stereotype.Service;
import java.security.*;
import java.util.Base64;

@Service
public class RSAService {
    private final PrivateKey privateKey;
    private final PublicKey publicKey;

    public RSAService() throws NoSuchAlgorithmException {
        KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
        generator.initialize(2048);
        KeyPair pair = generator.generateKeyPair();
        this.privateKey = pair.getPrivate();
        this.publicKey = pair.getPublic();
    }

    public String sign(String data) throws Exception {
        Signature privateSignature = Signature.getInstance("SHA256withRSA");
        privateSignature.initSign(privateKey);
        privateSignature.update(data.getBytes());
        byte[] signature = privateSignature.sign();
        return Base64.getEncoder().encodeToString(signature);
    }

    public PublicKey getPublicKey() {
        return this.publicKey;
    }
}