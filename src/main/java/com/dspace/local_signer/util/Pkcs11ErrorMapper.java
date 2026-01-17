package com.dspace.local_signer.util;

public class Pkcs11ErrorMapper {

    public static String map(Exception e) {
        String msg = e.getMessage();

        if (msg == null) return "Signing failed";

        if (msg.contains("CKR_PIN_INCORRECT")) return "Invalid PIN. Please try again.";
        if (msg.contains("CKR_PIN_LOCKED")) return "Token locked due to multiple wrong PIN attempts.";
        if (msg.contains("CKR_TOKEN_NOT_PRESENT")) return "USB token not detected.";
        if (msg.contains("CKR_TOKEN_NOT_RECOGNIZED")) return "Token not recognized. Please reinstall driver.";
        if (msg.contains("No such provider")) return "Signing provider not initialized.";
        if (msg.contains("certificate")) return "Signing certificate not found in token.";

        return "Signing failed. Please try again.";
    }
}
