CREATE TABLE certificates (
                              serial_number VARCHAR(255) PRIMARY KEY,
                              owner VARCHAR(255) NOT NULL,
                              public_key TEXT NOT NULL,
                              status VARCHAR(50) NOT NULL,
                              revocation_date TIMESTAMP,
                              created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
CREATE INDEX idx_certificate_owner ON certificates(owner);
CREATE INDEX idx_certificate_status ON certificates(status);