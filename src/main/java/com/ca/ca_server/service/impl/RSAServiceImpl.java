package com.ca.ca_server.service.impl;

import com.ca.ca_server.domain.entity.Certificate;
import com.ca.ca_server.domain.repository.CertificateRepository;
import com.ca.ca_server.dto.response.CertificateResponseDTO;
import com.ca.ca_server.enums.CertificateStatus;
import com.ca.ca_server.service.IRSAService;
import com.ca.ca_server.service.ICryptoEngine;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
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
    public boolean isKeyRevoked(String owner) {
        return certRepository.findByOwner(owner).stream()
                .allMatch(cert -> CertificateStatus.REVOKED.equals(cert.getStatus()));
    }

    @Override
    @Transactional
    public CertificateResponseDTO registerPublicKey(String owner, String publicKey) {
        boolean hasActiveKey = certRepository.existsByOwnerAndStatus(owner, CertificateStatus.GOOD);

        if (hasActiveKey) {
            throw new IllegalStateException("Owner đã có khóa đang hoạt động, vui lòng thu hồi khóa cũ trước!");
        }

        String serial = UUID.randomUUID().toString();
        Certificate cert = Certificate.builder()
                .serialNumber(serial)
                .owner(owner)
                .publicKey(publicKey)
                .status(CertificateStatus.GOOD)
                .createdAt(LocalDateTime.now())
                .build();

        certRepository.save(cert);
        log.info("Đăng ký khóa công khai thành công cho owner: {}. Serial: {}", owner, serial);

        return CertificateResponseDTO.builder()
                .serialNumber(serial)
                .owner(owner)
                .build();
    }


    @Override
    @Transactional
    public CertificateResponseDTO signAndIssue(String data, String owner, String padding, String publicKey) throws Exception {
        String usedPadding = (padding == null || padding.isBlank()) ? "PKCS1" : padding;
        Certificate cert = certRepository.findByOwnerAndStatus(owner, CertificateStatus.GOOD)
                .orElseGet(() -> {
                    if (publicKey == null || publicKey.isBlank()) {
                        throw new RuntimeException("Đây là lần đầu bạn ký, vui lòng cung cấp publicKey!");
                    }
                    return registerPublicKeyInternal(owner, publicKey);
                });

        if (publicKey != null && !publicKey.equals(cert.getPublicKey())) {
            throw new SecurityException("Public Key bạn gửi không khớp với khóa đã đăng ký!");
        }
        String signature = cryptoEngine.sign(data, usedPadding);
        return CertificateResponseDTO.builder()
                .signature(signature)
                .serialNumber(cert.getSerialNumber())
                .owner(owner)
                .build();
    }

    private Certificate registerPublicKeyInternal(String owner, String publicKey) {
        String serial = UUID.randomUUID().toString();
        Certificate cert = Certificate.builder()
                .serialNumber(serial)
                .owner(owner)
                .publicKey(publicKey)
                .status(CertificateStatus.GOOD)
                .createdAt(LocalDateTime.now())
                .build();
        log.info("đăng ký public key cho owner: {}. Serial: {}", owner, serial);
        return certRepository.save(cert);
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

    // Trong IRSAService.java và RSAServiceImpl.java
    @Override
    public boolean verifySignature(String data, String signature, String owner, String padding) throws Exception {
        Certificate cert = certRepository.findByOwnerAndStatus(owner, CertificateStatus.GOOD)
                .orElseThrow(() -> new Exception("Owner không tồn tại!"));
        if (cert.getStatus() == CertificateStatus.REVOKED) {
            log.warn("Lưu ý: chứng chỉ {} đã bị thu hồi. Từ chối xác thực!", cert.getSerialNumber());
            return false;
        }
        return cryptoEngine.verify(data, signature, cert.getPublicKey(), padding);
    }
}