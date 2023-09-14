package com.ali.pay.service.Impl;

import com.ali.pay.entity.PayNotification;
import org.springframework.stereotype.Service;

@Service
public interface PayNotificationServiceImpl {
    int insertPayNotification(PayNotification payNotification);
}
