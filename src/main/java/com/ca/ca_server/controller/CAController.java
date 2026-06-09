package com.ca.ca_server.controller;

import com.ca.ca_server.dto.request.RegisterRequest;
import com.ca.ca_server.dto.request.VerifyRequest;
import com.ca.ca_server.dto.response.CertificateResponseDTO;
import com.ca.ca_server.enums.CertificateStatus;
import com.ca.ca_server.service.ICryptoEngine;
import com.ca.ca_server.service.IRSAService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/v1/ca")
public class CAController {
    @Autowired
    private IRSAService rsaService;
    @Autowired
    private ICryptoEngine cryptoEngine;

    @PostMapping("/generate-key")
    public ResponseEntity<Map<String, String>> generateKeyPair() throws Exception {
        cryptoEngine.generateKeyPair(2048);

        Map<String, String> keys = new HashMap<>();
        keys.put("publicKey", cryptoEngine.getPublicKeyString());
        keys.put("privateKey", cryptoEngine.getPrivateKeyString());
        return ResponseEntity.ok(keys);
    }

    @PostMapping("/register")
    public ResponseEntity<CertificateResponseDTO> register(@RequestBody RegisterRequest request) {
        CertificateResponseDTO response = rsaService.registerPublicKey(request.getOwner(), request.getPublicKey());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/simulate-sign")
    public ResponseEntity<Map<String, String>> simulateSign(@RequestBody Map<String, String> request) throws Exception {
        String data = request.get("data");
        String privateKeyStr = request.get("privateKey");
        String padding = request.getOrDefault("padding", "PKCS1");
        String signature = cryptoEngine.signWithPrivateKey(data, privateKeyStr, padding);

        Map<String, String> response = new HashMap<>();
        response.put("signature", signature);
        response.put("message", "Chữ ký đã được tạo thành công!");
        return ResponseEntity.ok(response);
    }

    @PostMapping("/verify")
    public ResponseEntity<Boolean> verifyData(@Valid @RequestBody VerifyRequest request) throws Exception {
        log.info("Nhận yêu cầu xác thực cho owner: {}", request.getOwner());

        boolean isValid = rsaService.verifySignature(
                request.getData(),
                request.getSignature(),
                request.getOwner(),
                request.getPadding()
        );

        return ResponseEntity.ok(isValid);
    }

    @PatchMapping("/revoke/{serialNumber}")
    public ResponseEntity<String> revokeCertificate(@PathVariable String serialNumber) throws Exception {
        rsaService.revokeCertificate(serialNumber);
        return ResponseEntity.ok("Certificate " + serialNumber + " đã được thu hồi.");
    }

    @GetMapping("/status/{serialNumber}")
    public ResponseEntity<CertificateStatus> getCertificateStatus(@PathVariable String serialNumber) throws Exception {
        CertificateStatus status = rsaService.getStatus(serialNumber);
        return ResponseEntity.ok(status);
    }
}