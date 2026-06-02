package com.ca.ca_server.service.impl;

import com.ca.ca_server.domain.entity.Certificate;
import com.ca.ca_server.domain.repository.CertificateRepository;
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
    public String signAndIssue(String data, String owner) throws Exception {
        String signature = cryptoEngine.sign(data);
        Certificate cert = Certificate.builder()
                .serialNumber(UUID.randomUUID().toString())
                .status(CertificateStatus.GOOD)
                .owner(owner)
                .revocationDate(null)
                .createdAt(LocalDateTime.now())
                .build();
        Certificate savedCert = certRepository.save(cert);
        log.info(" id certificate : {}", savedCert.getId());
        return signature;
    }
}