package com.dspace.local_signer.controller;

import java.util.Map;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.dspace.local_signer.model.SignRequest;
import com.dspace.local_signer.model.SignResponse;
import com.dspace.local_signer.service.CertificateService;
import com.dspace.local_signer.service.SigningOrchestratorService;
import com.dspace.local_signer.service.UsbTokenDetectorService;

@RestController
@RequestMapping("/signer")
@CrossOrigin(origins = "http://localhost:4000", allowCredentials = "true")
public class SignerController {

    private final SigningOrchestratorService orchestrator;
    private final UsbTokenDetectorService tokenDetector = new UsbTokenDetectorService();
    private final CertificateService certificateService;

    public SignerController(SigningOrchestratorService orchestrator, CertificateService certificateService) {
        this.orchestrator = orchestrator;
        this.certificateService = certificateService;
    }

    @GetMapping("/health")
    public String health() {
        return "OK";
    }

    @GetMapping("/token/status")
    public Map<String, Object> tokenStatus() {
        boolean present = tokenDetector.isTokenPresent();
        return Map.of(
                "tokenPresent", present,
                "vendor", "Watchdata",
                "type", "PKCS11");
    }

    @PostMapping("/token/verify-pin")
    public Map<String, Object> verifyPin(@RequestParam String pin) throws Exception {
        System.out.println("Verifying PIN: " + pin);
        return certificateService.readCertificate(pin);
    }

    @PostMapping("/sign")
    public ResponseEntity<?> sign(@RequestBody SignRequest request) {

        SignResponse response = orchestrator.signPdf(request);

        if (!response.isSuccess()) {
            return ResponseEntity
                    .badRequest()
                    .body(Map.of("success", false, "message", response.getMessage()));
        }

        byte[] signedPdf = response.getPdf();

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_TYPE, "application/pdf")
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "inline; filename=\"" + request.getFileName() + "\"")
                .body(signedPdf);
    }

}
