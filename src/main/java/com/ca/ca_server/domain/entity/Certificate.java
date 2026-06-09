package com.ca.ca_server.domain.entity;

import com.ca.ca_server.enums.CertificateStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "certificates")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Certificate {

    @Id
    @Column(name = "serial_number", nullable = false)
    private String serialNumber;

    @Column(nullable = false)
    private String owner;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String publicKey;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CertificateStatus status;

    private LocalDateTime revocationDate;

    @Column(nullable = false, updatable = false)
    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();
}