package com.ca.ca_server.enums;

public enum CertificateStatus {
    GOOD,
    REVOKED,
    EXPIRED;

    public boolean isUsable() {
        return this == GOOD;
    }
}