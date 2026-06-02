package com.ca.ca_server.controller;

import com.ca.ca_server.service.RSA;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/ca")
public class CAController {

    @Autowired
    @Qualifier("caRsaEngine")
    private RSA rsaEngine;
}
