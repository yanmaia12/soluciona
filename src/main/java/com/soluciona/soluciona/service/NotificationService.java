package com.soluciona.soluciona.service;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {

    public void enviarNotificacao(String tokenDestino, String titulo, String corpo) {
        if (tokenDestino == null || tokenDestino.isEmpty()) {
            System.out.println("Erro: Tentativa de enviar notificação sem token.");
            return;
        }

        try {
            Message message = Message.builder()
                    .setToken(tokenDestino)
                    .setNotification(Notification.builder()
                            .setTitle(titulo)
                            .setBody(corpo)
                            .build())
                    .build();

            String response = FirebaseMessaging.getInstance().send(message);
            System.out.println("Notificação enviada com sucesso: " + response);

        } catch (Exception e) {
            System.err.println("Erro ao enviar notificação FCM: " + e.getMessage());
        }
    }
}