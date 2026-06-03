package com.ca.ca_server.service.impl;

import com.ca.ca_server.service.ICryptoEngine;
import org.springframework.stereotype.Service;
public class MockCryptoEngine implements ICryptoEngine {
    @Override
    public void generateKeyPair(int keySize) { }
    @Override
    public String getPublicKeyString() { return "MOCK_v45a8b9c2d1e0f3g6h7i8j9k0l1m2n3o4"; }
    @Override
    public String getPrivateKeyString() { return "MOCK_a1b2c3d4e5f6g7h8i9j0k1l2m3n4o5p6"; }
    @Override
    public String sign(String data) { return "SIG_MOCK_" + Integer.toHexString(data.hashCode()).toUpperCase(); }
    @Override
    public boolean verify(String data, String signature, String publicKeyStr) { return signature.startsWith("SIG_MOCK_");
    }
}