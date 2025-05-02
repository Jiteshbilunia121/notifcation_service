package org.example.notifcation_service.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PaymentInfoDTO {
    private Long userId;
    private String email;
    private Long amount;
    private String currency;
    private String transactionId;

}
