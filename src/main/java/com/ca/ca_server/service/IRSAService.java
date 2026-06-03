package com.ca.ca_server.service;

import com.ca.ca_server.dto.response.CertificateResponseDTO;
import com.ca.ca_server.enums.CertificateStatus;

public interface IRSAService {
    CertificateResponseDTO signAndIssue(String data, String owner) throws Exception;
    void revokeCertificate(String serialNumber) throws Exception;
    CertificateStatus getStatus(String serialNumber) throws Exception;
}