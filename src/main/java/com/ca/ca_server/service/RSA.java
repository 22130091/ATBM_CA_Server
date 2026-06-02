package com.ca.ca_server.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.security.*;
import java.security.interfaces.RSAKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import javax.crypto.Cipher;
import javax.crypto.CipherOutputStream;

public class RSA {
    private PublicKey publicKey;
    private PrivateKey privateKey;

    public void generateKeyPair(int keySize) throws Exception {
        KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
        keyGen.initialize(keySize);
        KeyPair pair = keyGen.generateKeyPair();
        this.publicKey = pair.getPublic();
        this.privateKey = pair.getPrivate();
    }

    public String getPublicStr() {
        return Base64.getEncoder().encodeToString(publicKey.getEncoded());
    }

    public String getPrivateStr() {
        return Base64.getEncoder().encodeToString(privateKey.getEncoded());
    }

    public PublicKey rebuildPublicKey(String keyStr) throws Exception {
        byte[] keyBytes = Base64.getDecoder().decode(keyStr);
        X509EncodedKeySpec spec = new X509EncodedKeySpec(keyBytes);
        KeyFactory kf = KeyFactory.getInstance("RSA");
        return kf.generatePublic(spec);
    }

    public PrivateKey rebuildPrivateKey(String keyStr) throws Exception {
        byte[] keyBytes = Base64.getDecoder().decode(keyStr);
        PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory kf = KeyFactory.getInstance("RSA");
        return kf.generatePrivate(spec);
    }

    public String encrypt(String plainText, PublicKey key) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, key);
        return Base64.getEncoder().encodeToString(cipher.doFinal(plainText.getBytes("UTF-8")));
    }

    /**
     * Với PKCS1Padding, kích thước tối đa là (keySize/8) - 11 bytes
     */
    public void processFile(int mode, File in, File out, Key key) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        cipher.init(mode, key);
        int keyBitLength = ((RSAKey) key).getModulus().bitLength();
        int keyByteLength = keyBitLength / 8;
        int inputBlockSize = (mode == Cipher.ENCRYPT_MODE) ? (keyByteLength - 11) : keyByteLength;

        try (FileInputStream fis = new FileInputStream(in); FileOutputStream fos = new FileOutputStream(out)) {

            byte[] buffer = new byte[inputBlockSize];
            int bytesRead;

            while ((bytesRead = fis.read(buffer)) != -1) {
                byte[] outputBlock = cipher.doFinal(buffer, 0, bytesRead);
                fos.write(outputBlock);
            }
        }
    }

    public String decrypt(String cipherText, PrivateKey key) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, key);
        return new String(cipher.doFinal(Base64.getDecoder().decode(cipherText)), "UTF-8");
    }

}