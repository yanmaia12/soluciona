package com.soluciona.soluciona.service;

import com.google.firebase.FirebaseApp;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {
    @Autowired
    private FirebaseApp firebaseApp;
    public void enviarNotificacao(String fcmToken, String titulo, String corpo) {

        if (fcmToken == null || fcmToken.isEmpty()) {
            return;
        }

        try {
            Message message = Message.builder()
                    .setToken(fcmToken)
                    .setNotification(Notification.builder()
                            .setTitle(titulo)
                            .setBody(corpo)
                            .build())
                    .build();

            FirebaseMessaging.getInstance().send(message);
            System.out.println("Notificação enviada: " + titulo);

        } catch (Exception e) {
            System.err.println("Erro ao enviar notificação: " + e.getMessage());
        }
    }
}
