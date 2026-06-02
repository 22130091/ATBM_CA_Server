package com.ca.ca_server.domain.entity;

import com.ca.ca_server.enums.CertificateStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Certificate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String serialNumber;

    @Enumerated(EnumType.STRING)
    private CertificateStatus status;

    private String owner;
    private LocalDateTime revocationDate;
    private LocalDateTime createdAt;


}
