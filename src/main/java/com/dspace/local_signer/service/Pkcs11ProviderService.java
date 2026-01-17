package com.dspace.local_signer.service;

import java.nio.file.Path;
import java.security.Provider;
import java.security.Security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class Pkcs11ProviderService {

    private static final Logger log = LoggerFactory.getLogger(Pkcs11ProviderService.class);

    private static final String CFG_PATH = "C:/pkcs11/pkcs11-watchdata.cfg";
    private static final String PROVIDER_NAME = "SunPKCS11-Watchdata";

    public synchronized Provider loadProvider() {

        try {
            Path cfgPath = Path.of(CFG_PATH);

            if (!cfgPath.toFile().exists()) {
                throw new RuntimeException("PKCS11 config file not found");
            }

            Provider old = Security.getProvider(PROVIDER_NAME);
            if (old != null) {
                Security.removeProvider(PROVIDER_NAME);
                System.gc();
            }

            Provider base = Security.getProvider("SunPKCS11");
            if (base == null) {
                throw new RuntimeException("SunPKCS11 not available in JVM");
            }

            Provider provider = base.configure(cfgPath.toAbsolutePath().toString());
            Security.addProvider(provider);

            return provider;

        } catch (Exception e) {
            throw new RuntimeException("PKCS11 provider initialization failed");
        }
    }

    public synchronized void reset() {
        Provider old = Security.getProvider(PROVIDER_NAME);
        if (old != null) {
            Security.removeProvider(PROVIDER_NAME);
            System.gc();
        }
    }
}
