package com.ca.ca_server.service;

import org.springframework.stereotype.Service;

public interface ICryptoEngine {
    void generateKeyPair(int keySize) throws Exception;

    String getPublicKeyString();
    String getPrivateKeyString();
    
    String hash(String data) throws Exception;


    String sign(String data, String padding) throws Exception;

    boolean verify(String data, String signature, String publicKeyStr, String padding) throws Exception;
}
