package com.ca.ca_server.service.impl;

import com.ca.ca_server.service.ICryptoEngine;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.spec.MGF1ParameterSpec;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.PSSParameterSpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
@Slf4j
@Service
public class CryptoEngineImpl implements ICryptoEngine {

	@Value("${ca.keystore.password}")
	private String keystorePassword;
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
	public String signWithPrivateKey(String data, String privateKeyStr, String padding) throws Exception {
		byte[] keyBytes = Base64.getDecoder().decode(privateKeyStr);
		PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(keyBytes);
		KeyFactory kf = KeyFactory.getInstance("RSA");
		PrivateKey privateKey = kf.generatePrivate(spec);

		Signature signature = createSignature(padding);
		signature.initSign(privateKey);
		signature.update(data.getBytes(StandardCharsets.UTF_8));

		return Base64.getEncoder().encodeToString(signature.sign());
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
	try {
		log.info(" Padding : {}", padding);
		byte[] keyBytes = Base64.getDecoder().decode(publicKeyStr);
		byte[] signBytes = Base64.getDecoder().decode(signature);
		byte[] dataBytes = data.getBytes(StandardCharsets.UTF_8);

		log.info(" Data length: {}, Signature length: {}", dataBytes.length, signBytes.length);

		X509EncodedKeySpec spec = new X509EncodedKeySpec(keyBytes);
		KeyFactory keyFactory = KeyFactory.getInstance("RSA");
		PublicKey publicKey = keyFactory.generatePublic(spec);

		Signature sig = createSignature(padding);
		sig.initVerify(publicKey);
		sig.update(dataBytes);

		boolean result = sig.verify(signBytes);
		log.info(" Kết quả : {}", result);
		return result;
	} catch (Exception e) {
		log.error(" Lỗi trong quá trình verify: ", e);
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
}
