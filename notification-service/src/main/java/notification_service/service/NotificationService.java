package com.notifyhub.notification_service.service;

import com.notifyhub.notification_service.kafka.NotificationProducer;
import com.notifyhub.notification_service.model.Notification;
import com.notifyhub.notification_service.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationService{

    private final NotificationProducer notificationProducer;
    private final NotificationRepository notificationRepository;

    public void sendNotification(String message){
        notificationProducer.sendNotification(message);
    }

    public List<Notification> getAllNotifications(){
        return notificationRepository.findAll();
    }
}