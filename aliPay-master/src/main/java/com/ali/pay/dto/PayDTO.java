package com.ali.pay.dto;

import lombok.Data;

@Data
public class PayDTO {
    private String name; //商品名
    private double money; //价格
    private String password; // 密匙
    private String requestId; //唯一标识
}
