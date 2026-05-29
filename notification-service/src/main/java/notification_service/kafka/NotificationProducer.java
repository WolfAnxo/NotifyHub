package com.notifyhub.notification_service.kafka;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class NotificationProducer{

    private final KafkaTemplate<String, String> KafkaTemplate;
    private static final String TOPIC = "notifications";

    public void sendNotification(String message){
        KafkaTemplate.send(TOPIC, message);
        System.out.println("Mensaje enviado: " + message);
    }
}