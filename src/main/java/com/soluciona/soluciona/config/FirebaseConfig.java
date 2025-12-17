package com.soluciona.soluciona.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

@Configuration
public class FirebaseConfig {
    String jsonCredentials = System.getenv("FIREBASE_CREDENTIALS");

    InputStream serviceAccount = new ByteArrayInputStream(jsonCredentials.getBytes());

    FirebaseOptions options = FirebaseOptions.builder()
            .setCredentials(GoogleCredentials.fromStream(serviceAccount))
            .build();

    public FirebaseConfig() throws IOException {
    }
}
