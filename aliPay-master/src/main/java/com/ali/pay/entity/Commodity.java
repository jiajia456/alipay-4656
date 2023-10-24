package com.ali.pay.entity;

import lombok.Data;

// 商品名称
@Data
public class Commodity {
    private String password;//提取码
    private String name;
    private double money;
    private String baidu_url;//百度网盘
    private String baidu_password;//提取码
    private int id;
}
