package com.ca.ca_server.service;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.MessageDigest;
import java.security.PublicKey;
import java.security.Signature;
import java.security.spec.MGF1ParameterSpec;
import java.security.spec.PSSParameterSpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

public class CryptoEngineImpl implements ICryptoEngine {
	private KeyPair keyPair;

	@Override
	public void generateKeyPair(int keySize) throws Exception {
		// TODO Auto-generated method stub
		KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
		keyGen.initialize(keySize);
		keyPair = keyGen.generateKeyPair();
	}

	@Override
	public String getPublicKeyString() {
		// TODO Auto-generated method stub
		if (keyPair == null) {
			return null;
		}

		return Base64.getEncoder().encodeToString(keyPair.getPublic().getEncoded());
	}

	@Override
	public String getPrivateKeyString() {
		// TODO Auto-generated method stub
		if (keyPair == null) {
			return null;
		}

		return Base64.getEncoder().encodeToString(keyPair.getPrivate().getEncoded());
	}

	@Override
	public String hash(String data) throws Exception {
		// TODO Auto-generated method stub
		try {
			MessageDigest md = MessageDigest.getInstance("SHA-256");
			byte[] hashBytes = md.digest(data.getBytes(StandardCharsets.UTF_8));
			BigInteger number = new BigInteger(1, hashBytes);
			return number.toString(16);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public String sign(String data, String padding) throws Exception {
		// TODO Auto-generated method stub
		try {
			if (keyPair == null) {
				throw new Exception("Chưa tạo KeyPair");
	        }

	        Signature signature = createSignature(padding);

	        signature.initSign(keyPair.getPrivate());

	        signature.update(data.getBytes(StandardCharsets.UTF_8));

	        byte[] signBytes = signature.sign();

	        return Base64.getEncoder().encodeToString(signBytes);

	    } catch(Exception e) {
	    	e.printStackTrace();
	    	throw e;
	    }
	}

	@Override
	public boolean verify(String data, String signature, String publicKeyStr, String padding) throws Exception {
		// TODO Auto-generated method stub
		 try {

		        byte[] keyBytes = Base64.getDecoder().decode(publicKeyStr);

		        X509EncodedKeySpec spec = new X509EncodedKeySpec(keyBytes);

		        KeyFactory keyFactory = KeyFactory.getInstance("RSA");

		        PublicKey publicKey =keyFactory.generatePublic(spec);

		        Signature sig = createSignature(padding);

		        sig.initVerify(publicKey);

		        sig.update(data.getBytes(StandardCharsets.UTF_8));

		        byte[] signBytes =Base64.getDecoder().decode(signature);

		        return sig.verify(signBytes);

		    }catch(Exception e) {
		    	e.printStackTrace();
		    	throw e;
		    }
		
	}

	// tạo signature theo padding
	private Signature createSignature(String padding) throws Exception {
		if ("PSS".equalsIgnoreCase(padding)) {
			Signature signature = Signature.getInstance("RSASSA-PSS");
			signature.setParameter(new PSSParameterSpec("SHA-256", "MGF1", MGF1ParameterSpec.SHA256, 32, 1));

			return signature;
		}
		return Signature.getInstance("SHA256withRSA");
	}
	//test thử
	public static void main(String[] args) {
		try {
			CryptoEngineImpl crypto = new CryptoEngineImpl();

	        // Sinh khóa RSA
	        crypto.generateKeyPair(2048);

	        System.out.println("===== PUBLIC KEY =====");
	        System.out.println(crypto.getPublicKeyString());

	        System.out.println();

	        System.out.println("===== PRIVATE KEY =====");
	        System.out.println(crypto.getPrivateKeyString());

	        System.out.println();

	        String data ="DH001|KH001|SALE10|2";

	        System.out.println("===== DATA =====");
	        System.out.println(data);

	        System.out.println();

	        String hash = crypto.hash(data);

	        System.out.println("===== HASH =====");
	        System.out.println(hash);

	        System.out.println();

	        String signature =crypto.sign(data,"PKCS1");

	        System.out.println("===== SIGNATURE =====");

	        System.out.println(signature);

	        System.out.println();

	        boolean verifyResult =crypto.verify(data,signature,crypto.getPublicKeyString(),"PKCS1");

	        System.out.println("===== VERIFY =====");

	        System.out.println(verifyResult);

	        System.out.println();

	        // Test sửa dữ liệu

	        String fakeData ="DH001|KH001|SALE10|3";

	        boolean fakeVerify = crypto.verify(fakeData,signature,crypto.getPublicKeyString(),"PKCS1");

	        System.out.println("===== VERIFY AFTER MODIFY DATA =====");

	        System.out.println(fakeVerify);

	    } catch (Exception e) {

	        e.printStackTrace();
	    }
	}
}
