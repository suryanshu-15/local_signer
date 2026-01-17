package com.dspace.local_signer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class LocalSignerApplication {

    public static void main(String[] args) {

        ConfigurableApplicationContext context =
                SpringApplication.run(LocalSignerApplication.class, args);

        String port = context.getEnvironment().getProperty("server.port");

        // If server.port=0 (random port), use local.server.port
        if (port == null || port.equals("0")) {
            port = context.getEnvironment().getProperty("local.server.port");
        }

        System.out.println("✅ Local DSC Signer running on port: " + port);
    }
}


