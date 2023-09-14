package com.ali.pay.dto;

import lombok.Data;

@Data
public class UrlandCode {
    private String code; // 手机链接
    private String url; // 二维码地址
    private String orderNumber; // 订单号
}
