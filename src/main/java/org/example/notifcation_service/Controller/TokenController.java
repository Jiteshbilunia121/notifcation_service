package org.example.notifcation_service.Controller;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import org.example.notifcation_service.Service.NotificationService;
import org.example.notifcation_service.dto.RequestToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/api/notification")
public class TokenController {


    @Autowired
    private final NotificationService notificationService;
//    private final StringHttpMessageConverter stringHttpMessageConverter;

    public TokenController(NotificationService notificationService) {
        this.notificationService = notificationService;

    }



    @PostMapping("/register-token")
    public ResponseEntity<String> registerToken(@RequestBody RequestToken requesttoken) {
        String userId = requesttoken.getUserId();
        String token = requesttoken.getToken();

        if (token == null || token.isEmpty()) {
            return ResponseEntity.badRequest().body("Token is missing");
        }

        notificationService.saveToken(userId, token);

        try {
            FirebaseMessaging.getInstance().subscribeToTopic(List.of(token), "parking.slot.occupied");
            FirebaseMessaging.getInstance().subscribeToTopic(List.of(token), "parking.slot.vacant");
            FirebaseMessaging.getInstance().subscribeToTopic(List.of(token), "parking.payment");
        } catch (FirebaseMessagingException e) {
            throw new RuntimeException(e);
        }


        // Optional: Subscribe this token to a topic
//        try {
//            FirebaseMessaging.getInstance().subscribeToTopic(
//                    List.of(token), "parking.slot.vacant"
//            );
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

        return ResponseEntity.ok("Token registered and subscribed successfully.");
    }
}
