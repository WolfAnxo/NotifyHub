package com.notifyhub.notification_service.kafka;

import com.notifyhub.notification_service.model.Notification;
import com.notifyhub.notification_service.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NotificationConsumer {

    private final NotificationRepository notificationRepository;

    @KafkaListener(topics = "notifications", groupId = "notifyhub-group")
    public void consume(String message) {
        System.out.println("Mensaje recibido de Kafka: " + message);

        Notification notification = new Notification();
        notification.setTitle("Nueva notificación");
        notification.setMessage(message);
        notification.setStatus("RECEIVED");

        notificationRepository.save(notification);
        System.out.println("Notificación guardada en BD: " + message);
    }
}