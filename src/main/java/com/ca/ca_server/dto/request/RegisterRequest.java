package com.ca.ca_server.dto.request;

import lombok.Data;
@Data
public class RegisterRequest {
    private String owner;
    private String publicKey;
}