package com.notifyhub.notification_service.kafka;

import com.notifyhub.notification_service.model.Notification;
import com.notifyhub.notification_service.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class NotificationConsumer {

    private final NotificationRepository notificationRepository;
    private final JavaMailSender mailSender; 

    @Value("${MAIL_TO:anxopenablanco@gmail.com}")
    private String mailTo;

    @Transactional
    @KafkaListener(topics = "notifications", groupId = "notifyhub-group")
    public void consume(String messageId) {
        System.out.println("📥 ID recibido desde Kafka para procesar email: " + messageId);

        try {
            Long id = Long.parseLong(messageId);
            Optional<Notification> optionalNotification = notificationRepository.findById(id);
            
            if (optionalNotification.isPresent()) {
                Notification notification = optionalNotification.get();
                
                //  Aseguramos las credenciales SMTP directamente en el motor de envío
                if (mailSender instanceof JavaMailSenderImpl) {
                    JavaMailSenderImpl impl = (JavaMailSenderImpl) mailSender;
                    impl.setUsername("anxopenablanco@gmail.com");
                    impl.setPassword("xlitdgzhxbpxbnic"); 
                }

                // Configuración y envío del correo real
                SimpleMailMessage mailMessage = new SimpleMailMessage();
                mailMessage.setTo(this.mailTo); 
                mailMessage.setSubject("🔔 NotifyHub: " + notification.getTitle());
                mailMessage.setText(notification.getMessage());
                
                System.out.println("📬 Enviando correo electrónico real a: " + this.mailTo + "...");
                mailSender.send(mailMessage);
                System.out.println("✅ ¡Correo enviado con éxito!");
                
                // Si el correo sale bien, actualizamos el estado en base de datos
                notification.setStatus("SENT");
                notificationRepository.save(notification);
                
                System.out.println("💾 Notificación ID " + id + " guardada en Postgres como SENT");
            }
        } catch (Exception e) {
            System.err.println("❌ Error crítico al procesar el envío de correo: " + e.getMessage());
            e.printStackTrace();
        }
    }
}