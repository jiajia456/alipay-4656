package com.ali.pay.dto;

import lombok.Data;

@Data
public class PayReturnDTO {
    private String subject;
    private String invoiceAmount;
    private String notifyId;
    private String cardkey;
}
