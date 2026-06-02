package com.ca.ca_server.controller;

import com.ca.ca_server.service.ICryptoEngine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/ca")
public class CAController {

    @Autowired
    @Qualifier("caRsaEngine")
    private ICryptoEngine cryptoEngine;

    @PostMapping("/sign")
    public String signData(@RequestBody String data) {
        try {
            return cryptoEngine.sign(data);
        } catch (Exception e) {
            return "Error signing data: " + e.getMessage();
        }
    }
}
