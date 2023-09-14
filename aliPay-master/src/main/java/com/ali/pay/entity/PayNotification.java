package com.ali.pay.entity;
import java.util.Date;
import lombok.Data;
// 支付成功保存信息
@Data
public class PayNotification {
    private String sellerEmail; // 电话或者邮箱
    private String subject;   // 商品名称
    private String invoiceAmount; //商品价格
    private String notifyId; // 商家订单号
    private String time;
    private String cardkey;
}
