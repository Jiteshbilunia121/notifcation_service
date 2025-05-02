package org.example.notifcation_service.Service;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class NotificationService {

    private static final Map<String, String> userTokens = new HashMap<>(); // Store FCM tokens

    @Autowired
    private final EmailService emailService;


    public NotificationService(EmailService emailService) {
        this.emailService = emailService;

    }


    public void saveToken(String userId, String token) {
        userTokens.put(userId, token);
        // :Print the current HashMap
        System.out.println("Hashmap of tokens - ");
        System.out.println(userTokens);
    }

    public void sendPushNotification(String userId, String title, String message, String topic) {
        System.out.println("Sending web push notification, current Map");

        System.out.println(userTokens);
        try {
            String token = userTokens.get(userId);
            if (token == null) {
                System.out.println("User token not found!");
                return;
            }

//            Notification notification = Notification.builder()
//                    .setTitle(title)
//                    .setBody(message)
//                    .build();

//            Message firebaseMessage = Message.builder()
//                    .setNotification(notification)
//                    .setTopic(topic)
//                    .putData("title", title)
//                    .putData("message", message)
//                    .build();

            Message firebaseMessage = Message.builder()
                    .setToken(token) // ✅ send to specific user token
                    .putData("title", title)
                    .putData("message", message)
                    .build();

            FirebaseMessaging.getInstance().send(firebaseMessage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendEmailNotification(String to, String subject, String body) {
//        String to = userTokens.get(userId);
//          String to

        // TODO: Implement Email Sending (SMTP or Mail API)
        System.out.println("Sending email to " + to + " - " + subject);
        emailService.sendEmail(to, subject, body );
    }
}

