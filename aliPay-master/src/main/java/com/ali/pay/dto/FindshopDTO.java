package com.ali.pay.dto;

import lombok.Data;
// 用来返回网页的商品界面的参数
@Data
public class FindshopDTO {
    private String password;
    private String name;
    private double money;
}
