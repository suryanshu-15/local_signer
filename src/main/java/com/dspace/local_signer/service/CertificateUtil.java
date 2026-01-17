package com.dspace.local_signer.service;

import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.Map;

import javax.security.auth.x500.X500Principal;

public class CertificateUtil {

    public static Map<String, String> parseDN(X509Certificate cert) {
        String dn = cert.getSubjectX500Principal()
                .getName(X500Principal.RFC2253);

        Map<String, String> map = new HashMap<>();

        String[] parts = dn.split(",");
        for (String part : parts) {
            String[] kv = part.split("=", 2);
            if (kv.length == 2) {
                map.put(kv[0].trim(), kv[1].trim());
            }
        }
        return map;
    }

    public static String getCN(X509Certificate cert) {
        String cn = parseDN(cert).getOrDefault("SERIALNUMBER", "");
        System.out.println("CN: "+cn);
        System.out.println("Cert: "+cert);
        return parseDN(cert).getOrDefault("CN", "");
    }

    public static String getO(X509Certificate cert) {
        return parseDN(cert).getOrDefault("O", "");
    }

    public static String getIssuerCN(X509Certificate cert) {
        String dn = cert.getIssuerX500Principal()
                .getName(X500Principal.RFC2253);
        for (String part : dn.split(",")) {
            if (part.startsWith("CN=")) {
                return part.substring(3);
            }
        }
        return dn;
    }
}
