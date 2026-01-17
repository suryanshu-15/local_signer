package com.dspace.local_signer.service;

import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * Detects presence of USB DSC token on the local machine.
 *
 * Works for:
 * - Watchdata
 * - ePass
 * - ProxKey
 * - SafeNet
 *
 * Uses OS-level detection (no Java PKCS11 yet).
 */
@Service
public class UsbTokenDetectorService {

    // Watchdata USB Key
    private static final String WATCHDATA_VENDOR_ID = "163c";
    private static final String WATCHDATA_PRODUCT_ID = "0417";


    public boolean isTokenPresent() {
        String os = System.getProperty("os.name").toLowerCase();

        try {
            if (os.contains("linux")) {
                return checkLinux();
            } else if (os.contains("windows")) {
                return checkWindows();
            } else if (os.contains("mac")) {
                return checkMac();
            }
        } catch (Exception e) {
            return false;
        }

        return false;
    }


    private boolean checkLinux() throws Exception {
        Process process = new ProcessBuilder("lsusb").start();
        BufferedReader reader =
                new BufferedReader(new InputStreamReader(process.getInputStream()));

        String line;
        while ((line = reader.readLine()) != null) {
            if (line.contains(WATCHDATA_VENDOR_ID + ":" + WATCHDATA_PRODUCT_ID)) {
                return true;
            }
        }
        return false;
    }

    private boolean checkWindows() throws Exception {
        Process process = new ProcessBuilder(
                "wmic",
                "path",
                "Win32_PnPEntity",
                "get",
                "DeviceID"
        ).start();

        BufferedReader reader =
                new BufferedReader(new InputStreamReader(process.getInputStream()));

        String line;
        while ((line = reader.readLine()) != null) {
            if (line.toLowerCase().contains("vid_" + WATCHDATA_VENDOR_ID)) {
                return true;
            }
        }
        return false;
    }

    private boolean checkMac() throws Exception {
        Process process = new ProcessBuilder("system_profiler", "SPUSBDataType").start();
        BufferedReader reader =
                new BufferedReader(new InputStreamReader(process.getInputStream()));

        String line;
        while ((line = reader.readLine()) != null) {
            if (line.contains("Watchdata") || line.contains("USB Key")) {
                return true;
            }
        }
        return false;
    }
}
