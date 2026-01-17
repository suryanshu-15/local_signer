package com.dspace.local_signer.model;

public class SignResponse {

    private boolean success;
    private String message;
    private byte[] pdf;

    public static SignResponse success(byte[] pdf) {
        SignResponse r = new SignResponse();
        r.success = true;
        r.pdf = pdf;
        r.message = "Document signed successfully";
        return r;
    }

    public static SignResponse error(String msg) {
        SignResponse r = new SignResponse();
        r.success = false;
        r.message = msg;
        return r;
    }

    public boolean isSuccess() { return success; }
    public String getMessage() { return message; }
    public byte[] getPdf() { return pdf; }
}
