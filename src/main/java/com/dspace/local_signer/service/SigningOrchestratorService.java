package com.dspace.local_signer.service;

import java.security.Provider;
import java.util.Base64;

import org.springframework.stereotype.Service;

import com.dspace.local_signer.model.SignRequest;
import com.dspace.local_signer.model.SignResponse;
import com.dspace.local_signer.model.TokenInfo;

@Service
public class SigningOrchestratorService {

    private final UsbTokenKeyService usbTokenKeyService;
    private final PdfSigningService pdfSigningService;
    private final Pkcs11ProviderService pkcs11ProviderService;

    public SigningOrchestratorService(
        UsbTokenKeyService usb,
        PdfSigningService pdf,
        Pkcs11ProviderService pkcs
    ) {
        this.usbTokenKeyService = usb;
        this.pdfSigningService = pdf;
        this.pkcs11ProviderService = pkcs;
    }

    public SignResponse signPdf(SignRequest request) {

        try {
            Provider provider = pkcs11ProviderService.loadProvider();

            TokenInfo token = usbTokenKeyService.loadToken(provider, request.getPin());

            byte[] pdfBytes = Base64.getDecoder().decode(request.getPdfBytes());

            byte[] signed = pdfSigningService.sign(pdfBytes, token, request.getClientName());

            return SignResponse.success(signed);

        } catch (Exception e) {
            return SignResponse.error(mapError(e));
        }
    }

    private String mapError(Exception e) {
        String m = e.getMessage();

        if (m == null) return "Signing failed";

        if (m.contains("PIN")) return "Invalid PIN";
        if (m.contains("token")) return "USB token not detected";
        if (m.contains("provider")) return "Signing provider error";
        if (m.contains("certificate")) return "Certificate not found in token";

        return "Signing failed: " + m;
    }
}
