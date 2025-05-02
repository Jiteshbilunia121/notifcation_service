package org.example.notifcation_service.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CheckoutEvent implements Serializable {
    private String vehicleNumber;
    private String slotNumber;
    private Long userId;
    private double amount;

}
