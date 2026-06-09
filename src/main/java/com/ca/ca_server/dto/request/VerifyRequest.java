package com.ca.ca_server.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VerifyRequest {
    @NotBlank(message = "Dữ liệu gốc không được để trống")
    private String data;

    @NotBlank(message = "Chữ ký không được để trống")
    private String signature;

    @NotBlank(message = "Owner không được để trống")
    private String owner;

    private String padding;
}