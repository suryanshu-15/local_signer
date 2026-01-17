package com.dspace.local_signer.service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;

import org.springframework.stereotype.Service;

import com.dspace.local_signer.model.TokenInfo;
import com.itextpdf.io.font.constants.StandardFonts;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.StampingProperties;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.signatures.BouncyCastleDigest;
import com.itextpdf.signatures.DigestAlgorithms;
import com.itextpdf.signatures.IExternalDigest;
import com.itextpdf.signatures.IExternalSignature;
import com.itextpdf.signatures.PdfSigner;
import com.itextpdf.signatures.PrivateKeySignature;

@Service
public class PdfSigningService {

    public byte[] sign(byte[] pdf, TokenInfo token, String clientName) throws Exception {

        ByteArrayOutputStream out = new ByteArrayOutputStream();

        PdfReader reader = new PdfReader(new ByteArrayInputStream(pdf));
        PdfSigner signer = new PdfSigner(reader, out, new StampingProperties());

        X509Certificate cert = token.getCertificate();

        PdfDocument pdfDoc = signer.getDocument();

        String signerName = extractCN(cert);

        String stampText =
                "Digitally Signed\n" +
                "By: " + signerName + "\n" +
                "Date: " + new java.util.Date();

        PdfFont font = PdfFontFactory.createFont(StandardFonts.HELVETICA);

        // ---- Stamp text on every page ----
        for (int i = 1; i <= pdfDoc.getNumberOfPages(); i++) {
            PdfPage page = pdfDoc.getPage(i);
            PdfCanvas canvas = new PdfCanvas(page);

            canvas.beginText();
            canvas.setFontAndSize(font, 8);
            canvas.moveText(150, 20);   // bottom area
            canvas.showText(stampText);
            canvas.endText();
        }

        // ---- Digital signature ----
        IExternalSignature signature = new PrivateKeySignature(
                token.getPrivateKey(),
                DigestAlgorithms.SHA256,
                "SunPKCS11-Watchdata");

        IExternalDigest digest = new BouncyCastleDigest();

        signer.setFieldName("Signature1");

        signer.signDetached(
                digest,
                signature,
                new Certificate[]{cert},
                null,
                null,
                null,
                0,
                PdfSigner.CryptoStandard.CADES);

        return out.toByteArray();
    }

    // ---- Clean CN extractor ----
    private String extractCN(X509Certificate cert) {
        String dn = cert.getSubjectX500Principal().getName();

        for (String part : dn.split(",")) {
            part = part.trim();
            if (part.startsWith("CN=")) {
                return part.substring(3);
            }
        }
        return "Unknown";
    }
}
