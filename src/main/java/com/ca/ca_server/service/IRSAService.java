package com.ca.ca_server.service;

import com.ca.ca_server.dto.response.CertificateResponseDTO;
import com.ca.ca_server.enums.CertificateStatus;
import jakarta.validation.constraints.NotBlank;

public interface IRSAService {
    CertificateResponseDTO signAndIssue(String data, String owner, String padding, String publicKey) throws Exception;
    void revokeCertificate(String serialNumber) throws Exception;
    CertificateStatus getStatus(String serialNumber) throws Exception;

    boolean isKeyRevoked(@NotBlank(message = "Thông tin người dùng không được để trống") String owner);
    CertificateResponseDTO registerPublicKey(String owner, String publicKey);

    boolean verifySignature(String data, String signature, String owner, String padding) throws Exception;
}