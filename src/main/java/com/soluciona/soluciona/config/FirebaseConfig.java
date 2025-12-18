package com.soluciona.soluciona.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.io.InputStream;

@Configuration
public class FirebaseConfig {

    @Bean
    public FirebaseApp firebaseApp() {
        try {
            // Tenta achar o arquivo serviceAccountKey.json na pasta resources
            ClassPathResource resource = new ClassPathResource("serviceAccountKey.json");

            // Se o arquivo não existir (pra não derrubar o Render)
            if (!resource.exists()) {
                System.out.println("⚠️ AVISO: Arquivo do Firebase não encontrado. O App vai subir sem notificações.");
                return null;
            }

            InputStream serviceAccount = resource.getInputStream();

            FirebaseOptions options = FirebaseOptions.builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .build();

            if (FirebaseApp.getApps().isEmpty()) {
                return FirebaseApp.initializeApp(options);
            }
            return FirebaseApp.getInstance();

        } catch (Exception e) {
            // Se der qualquer erro no Firebase, a gente ignora pro servidor subir
            System.out.println("⚠️ ERRO FIREBASE: " + e.getMessage());
            return null;
        }
    }
}