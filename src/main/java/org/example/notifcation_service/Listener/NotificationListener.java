package org.example.notifcation_service.Listener;

import org.example.notifcation_service.Service.NotificationService;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.example.notifcation_service.dto.CheckoutEvent;
import org.example.notifcation_service.dto.PaymentInfoDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import java.sql.SQLOutput;
import java.util.Optional;

@Service
public class NotificationListener {

    @Autowired
    private final NotificationService notificationService;

    public NotificationListener(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @KafkaListener(topics = "parking.slot.occupied", groupId = "notification-group", properties = {
            "spring.deserializer.value.delegate.class=org.apache.kafka.common.serialization.StringDeserializer"
    })
    public void handleSlotOccupied(String eventMessage) {
        String[] data = eventMessage.split(" ");
        String userId = data[1];
        String slotNumber = data[5];
        System.out.println("Received values for parking slot occupied event");
        System.out.println("userId = " + userId);
        System.out.println("slotNumber = " + slotNumber);
        // Send push notification
        String title = "Parking Slot Reserved!";
        String message = "You have reserved Slot " + slotNumber + ".";
        notificationService.sendPushNotification(userId, title, message, "parking.slot.occupied");
    }

    @KafkaListener(topics = "parking.slot.vacant", groupId = "notification-group")
    public void handleSlotVacant(@Payload CheckoutEvent checkoutEvent) {
//        String[] data = record.value().split(":");
//        String userId = data[0];
//        String slotId = data[1];
//        String amountDue = data[2];
//
//        // Send push & email notification
//        String title = "Parking Checkout Completed!";
//        String message = "Your slot " + slotId + " is now vacant. Amount Due: $" + amountDue;
        String title = "Parking Slot vacant!";
        String message = "You are checking out from the parking space";
        String userId = String.valueOf(checkoutEvent.getUserId());
        String vehicleNumber = String.valueOf(checkoutEvent.getVehicleNumber());
        System.out.println(message);
        System.out.println("userId = " + userId);
        System.out.println("vehicleNumber = " + vehicleNumber);

        notificationService.sendPushNotification(String.valueOf(userId), title, message, "parking.slot.vacant");
        notificationService.sendEmailNotification(String.valueOf(userId), title, message);
    }


    @KafkaListener(topics = "parking.payment")
    public void listen(@Payload PaymentInfoDTO dto, @Header(KafkaHeaders.GROUP_ID) String groupId) {
        String userId = String.valueOf(dto.getUserId());
        String amount = String.valueOf(dto.getAmount());
        System.out.println("Received Payment Info");
        System.out.println("userId = " + userId);
        System.out.println("amount = " + amount);

        String subject = "Parking Payment Successful";
        String body = "Your payment of " +(dto.getAmount() / 100.0) + " " + dto.getCurrency().toUpperCase() +
                " was successful. Transaction ID: " + dto.getTransactionId();


        notificationService.sendPushNotification(String.valueOf(dto.getUserId()), subject, body, "parking.payment");

        notificationService.sendEmailNotification(dto.getEmail(), subject, body);
//

    }

}
