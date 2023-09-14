package com.ali.pay.service;
import com.ali.pay.dto.PayDTO;
import com.ali.pay.dto.PayReturnDTO;
import com.ali.pay.entity.CardKey;
import com.ali.pay.entity.PayNotification;
import com.ali.pay.mapper.PayNotificationMapper;
import com.ali.pay.service.Impl.PayNotificationServiceImpl;
import com.ali.pay.util.RandomRedis;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class PayNotificationService implements PayNotificationServiceImpl {
    private final PayNotificationMapper payNotificationMapper;
    private final CommodityService commodityService;
    private final CardKeyService cardKeyService;

    @Autowired
    private RedisTemplate<String,Object> redisTemplate;


    @Autowired
    public PayNotificationService(PayNotificationMapper payNotificationMapper, CommodityService commodityService, CardKeyService cardKeyService) {
        this.payNotificationMapper = payNotificationMapper;
        this.commodityService = commodityService;
        this.cardKeyService = cardKeyService;


    }


    @Override
    public int insertPayNotification(PayNotification payNotification) {
        RandomRedis randomRedis = new RandomRedis();
        PayReturnDTO payReturnDTO = new PayReturnDTO();
        payReturnDTO.setCardkey(payNotification.getCardkey());
        payReturnDTO.setSubject(payNotification.getSubject());
        payReturnDTO.setInvoiceAmount(payNotification.getInvoiceAmount());
        payReturnDTO.setNotifyId(payNotification.getNotifyId());
        String card_key =  "alipay:return:"+payNotification.getNotifyId();
        redisTemplate.opsForValue().set(card_key,payReturnDTO,randomRedis.RandomRedis_second(60,180), TimeUnit.SECONDS);
        return  payNotificationMapper.insertPayNotification(payNotification);
    }

    public void setOderRedis(String orderNumber, PayDTO payDTO){
        RandomRedis randomRedis = new RandomRedis();
        // Redis存储密匙,只有支付成功才会有这个情况
        String cacheOder = "alipay:url:"+orderNumber;
        payDTO.setRequestId(orderNumber);
        redisTemplate.opsForValue().set(cacheOder, payDTO, randomRedis.RandomRedis_second(60,180), TimeUnit.SECONDS);

    }

    public String oneRedis(PayDTO payDTO){
        if(payDTO.getPassword()!=null){
            int key =  commodityService.getCommodityById(payDTO.getPassword()).getId();
            String key_password = cardKeyService.getCardKeyById(key).getKey_password();
            return key_password;
        }
        return null;
    }

    public void insertPayOder(CardKey cardKey){

    }
}
