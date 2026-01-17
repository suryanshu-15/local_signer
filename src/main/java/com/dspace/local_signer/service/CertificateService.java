package com.dspace.local_signer.service;

import java.security.KeyStore;
import java.security.cert.X509Certificate;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

@Service
public class CertificateService {

    private final WindowsKspProviderService windowsKspProviderService;

    public CertificateService(WindowsKspProviderService windowsKspProviderService) {
        this.windowsKspProviderService = windowsKspProviderService;
    }

    /**
     * This method VERIFIES PIN and reads certificate
     */
    public Map<String, Object> readCertificate(String pin) throws Exception {

        // Provider provider = windowsKspProviderService.loadProvider();

         KeyStore keyStore = KeyStore.getInstance("Windows-MY", "SunMSCAPI");

        // 🔐 PIN IS VERIFIED HERE
        keyStore.load(null, pin.toCharArray());

        Enumeration<String> aliases = keyStore.aliases();
        if (!aliases.hasMoreElements()) {
            throw new IllegalStateException("No certificate found in token");
        }

        String alias = aliases.nextElement();

        try {
    // PrivateKey privateKey = (PrivateKey) keyStore.getKey(alias, null);
} catch (Exception e) {
    throw new RuntimeException("Invalid PIN or token access denied");
}

        X509Certificate cert =
                (X509Certificate) keyStore.getCertificate(alias);

        Map<String, Object> info = new HashMap<>();
        System.out.print(cert);
        info.put("subject", cert.getSubjectX500Principal().toString());
        info.put("issuer", cert.getIssuerX500Principal().toString());
        info.put("serialNumber", cert.getSerialNumber().toString());
        info.put("validFrom", cert.getNotBefore());
        info.put("validTo", cert.getNotAfter());
        info.put("algorithm", cert.getSigAlgName());

        return info;
    }
}
