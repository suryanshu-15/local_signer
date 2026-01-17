package com.dspace.local_signer.service;

import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.Provider;
import java.security.cert.X509Certificate;
import java.util.Enumeration;

import org.springframework.stereotype.Service;

import com.dspace.local_signer.model.TokenInfo;

@Service
public class UsbTokenKeyService {
    private TokenInfo currentToken;

    public TokenInfo loadToken(Provider provider, String pin) throws Exception {

        KeyStore keyStore = KeyStore.getInstance("PKCS11", provider);
        keyStore.load(null, pin.toCharArray());

        Enumeration<String> aliases = keyStore.aliases();
        if (!aliases.hasMoreElements()) {
            throw new IllegalStateException("No certificate found in token");
        }

        String alias = aliases.nextElement();

        PrivateKey privateKey = (PrivateKey) keyStore.getKey(alias, null);

        if (privateKey == null) {
            throw new IllegalStateException("Private key not accessible");
        }

        X509Certificate certificate = (X509Certificate) keyStore.getCertificate(alias);

        return new TokenInfo(alias, privateKey, certificate);
    }

    public synchronized TokenInfo getCurrentToken() {
        if (currentToken == null) {
            throw new RuntimeException("Token not logged in");
        }
        return currentToken;
    }

    public synchronized void reset() {
        currentToken = null;
    }
}
