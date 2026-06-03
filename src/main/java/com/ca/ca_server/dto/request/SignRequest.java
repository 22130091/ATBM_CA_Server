package com.ca.ca_server.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SignRequest {
    @NotBlank(message = "Dữ liệu không được để trống")
    private String data;

    @NotBlank(message = "Thông tin người dùng không được để trống")
    private String owner;

    private String algorithm;
}