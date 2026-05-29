package com.notifyhub.notification_service.controller;

import com.notifyhub.notification_service.model.Notification;
import com.notifyhub.notification_service.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
@CrossOrigin(origins = "http://localhost:4200")
@RequiredArgsConstructor
public class NotificationController {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final NotificationRepository notificationRepository;

    @PostMapping("/send")
    public ResponseEntity<String> sendNotification(@RequestBody String message) {
        // 1. Creamos la notificación en estado PENDING
        Notification notification = new Notification();
        notification.setTitle("Nueva notificación");
        notification.setMessage(message);
        notification.setStatus("PENDING"); // Nace esperando a ser procesada

        // 2. Guardamos en la base de datos para obtener el ID generado
        Notification savedNotification = notificationRepository.save(notification);

        // 3. Enviamos el ID de la notificación a Kafka (convertido a texto)
        kafkaTemplate.send("notifications", String.valueOf(savedNotification.getId()));

        return ResponseEntity.ok("Notificación registrada y en cola de procesamiento.");
    }

    @GetMapping
    public ResponseEntity<List<Notification>> getAllNotifications() {
        return ResponseEntity.ok(notificationRepository.findAll());
    }

    @DeleteMapping("/clear")
    public ResponseEntity<String> clearNotifications() {
        notificationRepository.deleteAll();
        // Nota: Si usas IDs autoincrementales nativos de JPA, esto vacía la tabla por completo.
        return ResponseEntity.ok("Historial de notificaciones eliminado correctamente.");
    }
}