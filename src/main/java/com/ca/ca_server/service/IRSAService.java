package com.ca.ca_server.service;

public interface IRSAService {
    String signAndIssue(String data, String owner) throws Exception;
}