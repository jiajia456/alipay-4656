package com.ali.pay.entity;

import lombok.Data;

@Data
public class OrderStateEntity {
    private int code;
    private String msg;

    public OrderStateEntity(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

}
