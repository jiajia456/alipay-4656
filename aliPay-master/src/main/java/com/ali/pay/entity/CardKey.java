package com.ali.pay.entity;

import lombok.Data;
// 卡密
@Data
public class CardKey {
    private int id;
    private String key_password;//压缩包解码

}
