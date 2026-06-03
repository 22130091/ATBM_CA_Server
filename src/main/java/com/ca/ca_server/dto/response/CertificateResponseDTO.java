package com.ca.ca_server.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CertificateResponseDTO {
    private String signature;
    private String serialNumber;
    private String owner;
}