package com.ca.ca_server.config;

import com.ca.ca_server.service.RSA;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CASignatureConfig {
    @Bean(name = "caRsaEngine")
    public RSA rsaEngine() {
        return new RSA();
    }
}
