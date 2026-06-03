package com.ca.ca_server.controller;

import com.ca.ca_server.dto.request.SignRequest;
import com.ca.ca_server.dto.response.CertificateResponseDTO;
import com.ca.ca_server.enums.CertificateStatus;
import com.ca.ca_server.service.IRSAService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/ca")
public class CAController {
    @Autowired
    private IRSAService rsaService;
    @PostMapping("/sign")
    public ResponseEntity<CertificateResponseDTO> signData(@Valid @RequestBody SignRequest request) throws Exception {
        CertificateResponseDTO response = rsaService.signAndIssue(request.getData(), request.getOwner());
        return ResponseEntity.ok(response);
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