package com.dspace.local_signer.service;
import org.springframework.stereotype.Service;
@Service
public class TokenSessionService {

    private boolean loggedIn = false;

    public synchronized void setLoggedIn(boolean status) {
        this.loggedIn = status;
    }

    public synchronized boolean isLoggedIn() {
        return loggedIn;
    }

    public synchronized void reset() {
        loggedIn = false;
    }
}
