package com.ca.ca_server.config;

import com.ca.ca_server.service.ICryptoEngine;
import com.ca.ca_server.service.impl.MockCryptoEngine;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CASignatureConfig {
    @Bean(name = "caRsaEngine")
    public ICryptoEngine cryptoEngine() {
        return new MockCryptoEngine();
    }
}
