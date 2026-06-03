package com.ca.ca_server.service.adapter;

import com.ca.ca_server.service.ICryptoEngine;

public class CryptoAdapter implements ICryptoEngine {
    private final ICryptoEngine engine;
    public CryptoAdapter(ICryptoEngine engine) {
        this.engine = engine;
    }

    @Override
    public void generateKeyPair(int keySize) throws Exception {
        engine.generateKeyPair(keySize);
    }

    @Override
    public String getPublicKeyString() {
        return engine.getPublicKeyString();
    }

    @Override
    public String getPrivateKeyString() {
        return engine.getPrivateKeyString();
    }

    @Override
    public String sign(String data) throws Exception {
        return engine.sign(data);
    }

    @Override
    public boolean verify(String data, String signature, String publicKeyStr) throws Exception {
        return engine.verify(data, signature, publicKeyStr);
    }
}