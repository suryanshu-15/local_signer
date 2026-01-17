package com.dspace.local_signer.model;

public class SignRequest {

    private String pin;
    private String pdfBytes;
    private String fileName;
    private String clientName;

    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }

    public String getPdfBytes() {
        return pdfBytes;
    }

    public void setPdfBytes(String pdfBytes) {
        this.pdfBytes = pdfBytes;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }
}
