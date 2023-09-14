package com.ali.pay.dto;

import lombok.Data;
// 用来返回微信小程序的百度网盘的参数
@Data
public class CommodityDTO {
    private String baidu_url;
    private String baidu_password;
}
