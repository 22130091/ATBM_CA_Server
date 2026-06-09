package com.ca.ca_server.domain.repository;

import com.ca.ca_server.domain.entity.Certificate;
import com.ca.ca_server.enums.CertificateStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public interface CertificateRepository extends JpaRepository<Certificate, String> {
    Optional<Certificate> findBySerialNumber(String serialNumber);

    List<Certificate> findByOwner(String owner);

    boolean existsByOwnerAndStatus(String owner, CertificateStatus certificateStatus);

    Optional<Certificate> findByOwnerAndStatus(String owner, CertificateStatus status);
}