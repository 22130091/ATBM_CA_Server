package com.ca.ca_server.service.impl;

import com.ca.ca_server.domain.entity.Certificate;
import com.ca.ca_server.domain.repository.CertificateRepository;
import com.ca.ca_server.dto.response.CertificateResponseDTO;
import com.ca.ca_server.enums.CertificateStatus;
import com.ca.ca_server.service.IRSAService;
import com.ca.ca_server.service.ICryptoEngine;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Slf4j
@Service("rsaService")
public class RSAServiceImpl implements IRSAService {

    @Autowired
    private CertificateRepository certRepository;

    @Autowired
    private ICryptoEngine cryptoEngine;

    @Override
    @Transactional
    public CertificateResponseDTO signAndIssue(String data, String owner) throws Exception {
        String signature = cryptoEngine.sign(data);
        String serial = UUID.randomUUID().toString();
        Certificate cert = Certificate.builder()
                .serialNumber(serial)
                .status(CertificateStatus.GOOD)
                .owner(owner)
                .revocationDate(null)
                .createdAt(LocalDateTime.now())
                .build();
        Certificate savedCert = certRepository.save(cert);
        log.info(" id certificate saved: {}", savedCert.getId());
        return CertificateResponseDTO.builder()
                .signature(signature)
                .serialNumber(serial)
                .owner(owner)
                .build();
    }

    @Override
    @Transactional
    public void revokeCertificate(String serialNumber) throws Exception {
        Certificate cert = certRepository.findBySerialNumber(serialNumber)
                .orElseThrow(() -> new Exception("Không tìm thấy chứng chỉ với mã serial: " + serialNumber));
        if (CertificateStatus.REVOKED.equals(cert.getStatus())) {
            log.warn("Chứng chỉ đã được thu hồi trước đó: {}", serialNumber);
            return;
        }
        cert.setStatus(CertificateStatus.REVOKED);
        cert.setRevocationDate(LocalDateTime.now());
        certRepository.save(cert);
        log.info("Đã thu hồi Certificate - mã Serial: {}", serialNumber);
    }

    @Override
    public CertificateStatus getStatus(String serialNumber) throws Exception {
        return certRepository.findBySerialNumber(serialNumber)
                .map(Certificate::getStatus)
                .orElseThrow(() -> new Exception("Không tìm thấy chứng chỉ: " + serialNumber));
    }
}