package com.dspace.local_signer.model;

import java.security.PrivateKey;
import java.security.cert.X509Certificate;

public class TokenInfo {

    private final String alias;
    private final PrivateKey privateKey;
    private final X509Certificate certificate;

    public TokenInfo(String alias, PrivateKey privateKey, X509Certificate certificate) {
        this.alias = alias;
        this.privateKey = privateKey;
        this.certificate = certificate;
    }

    public String getAlias() {
        return alias;
    }

    public PrivateKey getPrivateKey() {
        return privateKey;
    }

    public X509Certificate getCertificate() {
        return certificate;
    }
}
    