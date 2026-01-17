package com.dspace.local_signer.service;

import java.security.Provider;
import java.security.Security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class WindowsKspProviderService {

    private static final Logger log = LoggerFactory.getLogger(WindowsKspProviderService.class);

    private Provider provider;

    public synchronized Provider loadProvider() {

        log.info("🔐 Windows KSP Provider initialization started");

        if (provider != null) {
            log.info("✅ Windows KSP provider already loaded: {}", provider.getName());
            return provider;
        }

        try {
            provider = Security.getProvider("SunMSCAPI");

            if (provider == null) {
                throw new RuntimeException("SunMSCAPI provider not available in JVM");
            }

            log.info("✅ Windows KSP provider loaded successfully: {}", provider.getName());

            return provider;

        } catch (Exception e) {
            log.error("❌ Failed to initialize Windows KSP provider", e);
            throw new RuntimeException("Failed to initialize Windows KSP provider", e);
        }
    }

    public synchronized void initialize() {
        loadProvider();
    }
}
