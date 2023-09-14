package com.ali.pay.mapper;

import com.ali.pay.entity.PayNotification;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface PayNotificationMapper {

    @Insert("INSERT INTO pay_notification (sellerEmail, subject, invoiceAmount, notifyId, time, cardkey) " +
            "VALUES (#{sellerEmail}, #{subject}, #{invoiceAmount}, #{notifyId}, #{time}, #{cardkey})")
    int insertPayNotification(PayNotification payNotification);

    // 还可以添加其他数据库操作方法，如查询、更新、删除等，根据需求自行添加
}
