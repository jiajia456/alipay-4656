package com.ali.pay.controler;

import com.ali.pay.config.DistributedLockUtil;
import com.ali.pay.dto.PayDTO;
import com.ali.pay.dto.PayReturnDTO;
import com.ali.pay.dto.UrlandCode;
import com.ali.pay.entity.OrderStateEntity;
import com.ali.pay.entity.PayNotification;
import com.ali.pay.entity.RestBean;
import com.ali.pay.service.CommodityService;
import com.ali.pay.service.PayNotificationService;
import com.alibaba.fastjson.JSONObject;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.AlipayTradePrecreateRequest;
import com.alipay.api.request.AlipayTradeQueryRequest;
import com.alipay.api.response.AlipayTradePrecreateResponse;
import com.alipay.api.response.AlipayTradeQueryResponse;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * @author ThirdGoddess
 * @version 1.0.0
 * @time 2022/12/22 14:37
 * @desc AliPay当面付Demo
 */
@RestController
@RequestMapping("pay")
public class AliPayController {


    //模拟一个用户的支付状态
    private boolean userPayState = false;

    private static String shop_name = "无商品";

    //==================================================================================================================
    //这里都是固定的

    //支付宝网关地址
    private static final String SERVER_URL = "https://openapi.alipay.com/gateway.do";

    //charset
    private static final String CHARSET = "utf-8";

    //format
    private static final String FORMAT = "json";

    //sign type
    private static final String SIGN_TYPE = "RSA2";

    //==================================================================================================================
    //下面这三个是需要配置的

    //APPID，即创建应用的那个ID,在应用详情中的左上角可以看到
    private static final String APPID = "2021003187607407";

    //应用私钥，注意是应用私钥！！！应用私钥！！！应用私钥！！！
    private static final String APP_PRIVATE_KEY = "MIIEvAIBADANBgkqhkiG9w0BAQEFAASCBKYwggSiAgEAAoIBAQCUbFma/awt977DbP71r08s77KT+W+8d9j2y/rDzA1uUAaEFfRcbQRinrZa3kJpdR/QqkE41DsTjBDLmrWOSAlJLS9BgZhHvepTY94u7QkERwtFs8lc9VRs6Pp7Sinb3YZ/Pl3muD+DGtUfd8Hp/c2o3MkcFvL1xBwzQ4WvvXMp9XcIYJQzJ4agYMsnQHyyAsKo8MpQrixxfTB1PHih+NGxfr047rXk6/6ZH/UPADzs1W/8hkSmSRt6AITfIhCd0Ydz6+UaUx8iAp5+JMu39tl36xBRJ6ucsTNi+RCwZzXaWYQrzwSL77GAOnY+wE/0FuBMdgQsMdzn/fgU4Q+XA0VdAgMBAAECggEAMzW/JO7pfWLBewQcZAB8vPBnGQ8zMj/XqC9w8iVxbOJHnUNF8K3cmXpRSMDvARvcQk67+jOfvnrRQK68fb71FT5105A3TmmEdAk9G8vFFKi+huolS+GJo5ou8mMAVk/0m+qU04o2v7F+X0EesvFTCDRFX1+7rHhhkn51ndEr7wmvpp12dAScYI9yyA6ecPuJgo9GchECxATJ4SaVZcBnffwyz3NrmlesQEFPtzyOMhWYcba3D2zS6f7JacROtLQa+Pt8uufcJIcxSWIYwcy5QheZSWwzx0Cv4bkTarpVg5D/8jqLeAnfFT49PhfgOWpp1ZC3IUoWF8jKHebIYq/BMQKBgQDPb/QjAwqc4R+XkyVknqFJ5C/fQ3cKPhhnhXa5erPj8EJx49OdSMdhs78Mm92Se4v6ro+eOFIrT49KFeIu23af3zqK4dOXjX4vjuU4UscRuXJX4C/ur50Bv/Zt4Jt+1+Z2b6u4QWBdeDqMFktT8tYaSl+ib5Gb6ku7gYglymeitwKBgQC3K5bodCSEP6Q/rOgS9oLo30g4bf8929DzcBtaY5HDs+E3OsUEzNVVZAZh2r75zGd0Obr0brxbdgE+rKegQ7/wzopAYXmboybxcP+y3rd3gt2rV5dEEJPdMmtS1uLDNxaV9oc5bFPsYeB1UTOQzI5PobDMUQ8Hl8VTXn0drhV0iwKBgCPn7QpibGPbU+yYu9A+5Qw1Q1QSCXjsZq0EnRhd9OVvoRC1rtt6zYhL5LX6BNBmMkSiEpoAhbiZfOQGBbC2rEHgEfIYRNfm1gDFa0fkjDC1OOWdGK7oFvVGlRFP35qFJI87pS2vo06KkRfOhBtz3TuPxB4J0dp7I8DBGx/veSDhAoGALtIFkQWfD+70nRIlgDNi4wOwAjybscQudQuC8mfhKpUgTG3JTqDNJzaaKwkvJ8sH5r2a/EASBY70YoDfmCe5OGSTEkHYQ+BAZBbuRVqIqxPYdaO+FrfmE/R/Wn1zGVNLgqmxhwwZp6KdLV/EPo8LEW/TMWbG4Ln6xoSwlpWZBmECgYBAptL9DoPcgNAfKvhSsX16qQ3CnvDLDyuNPVRViN5nEyWeMckdpp3+9XHtU7KPY3Ay3K9fyyn3L3Lfx+JCm2B7lozIFWjoRIFdZQTe63QA2K72RwQp7KfDiDQygG9HUkKMb9QcMaFhEntmSUs+w4tuPO+/eHTWJ3fiuYoCRAjS5w==";

    //支付宝公钥，注意是支付宝公钥！！！支付宝公钥！！！支付宝公钥！！！
    private static final String ALIPAY_PUBLIC_KEY = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAiHMOpFPbS3xWoST2yQL5Yw9SGJqG5ZQvYBbadPLKcWOaGGIjLRziPC1XFKho6c7XkuBrN98HROjdnfOIQafbsSpelBdlCKRFVp8n84jktg4RMr6Wxx2sbUxuLRNcGSQJdoZApTFqWURL3869yKfaqyA1IuxhhSHKw3tQ3Dbuenn7Li1emYirJ4VCAUNSFr0HzjdbofIeBU2mjGmSw58OvWDRgiUobhKELlfped2x7I6JylRl9W5mq4Ean9rRZKyk/ZTh/3F5SnrBZOwWt7y/qNP+vpoVAchKna5+8ptC2MqcFDkzSNp7X3B2B5RzrvsgsVsrwvzM5qgHhLC6AnCD7QIDAQAB";

    private final PayNotificationService payNotificationService;
    private final CommodityService commodityService;

    @Autowired
    private RedisTemplate<String,Object> redisTemplate;
    private final DistributedLockUtil distributedLockUtil;

    @Autowired
    public AliPayController(DistributedLockUtil distributedLockUtil, PayNotificationService payNotificationService, CommodityService commodityService) {
        this.distributedLockUtil = distributedLockUtil;
        this.payNotificationService = payNotificationService;
        this.commodityService = commodityService;
    }

    /**
     * 获取二维码
     * 获取的是用户要扫码支付的二维码
     * 创建订单，带入自己的业务逻辑
     */
    // 这里使用json格式返回给前端
    @RequestMapping(value = "/getQr", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public RestBean getQr(@RequestBody PayDTO payDTO) {
        if(payDTO.getName()==null || payDTO.getMoney() == 0 || payDTO.getPassword() == null || payDTO.getRequestId()==null){
            return RestBean.failure(401,"请先选择商品");
        }
        if(commodityService.getCommodityById(payDTO.getPassword()) == null){
            return RestBean.failure(401,"无此购物商品");
        }
        Random random = new Random();
        String randomNumber1 = String.valueOf(random.nextInt(100)); // 生成 0 到 9 之间的随机整数
        String randomNumber2 = String.valueOf(random.nextInt(100)); // 生成 0 到 9 之间的随机整数
        //自己生成一个订单号，我这里直接用时间戳演示，正常情况下创建完订单需要存储到自己的业务数据库，做记录和支付完成后校验
        String orderNumber = randomNumber1 + System.currentTimeMillis() +  randomNumber2;
        String  lockKey= "alipay:lock:oder"+randomNumber1+randomNumber2;
        try {
            boolean lockAcquired = distributedLockUtil.acquireLock(lockKey, payDTO.getRequestId(), 5); // 5秒过期时间
            if(lockAcquired){
                try {
                    userPayState = false;
                    AlipayClient alipayClient = new DefaultAlipayClient(SERVER_URL, APPID, APP_PRIVATE_KEY, FORMAT, CHARSET, ALIPAY_PUBLIC_KEY, SIGN_TYPE);
                    AlipayTradePrecreateRequest request = new AlipayTradePrecreateRequest();
                    //配置这是一个url，下图我已经配置好了，这个意思是当用户成功后，支付宝那边会调用这个地址url，他会给你传过去一些订单信息，
                    //你处理完你的业务逻辑给支付宝响应success就行，就代表这个订单完成交易了！
                    //* 建议前期开发的时候加上内网穿透调试，不然支付宝是没有办法调到你开发的接口的
                    String Ngrok = "https://e5bf-117-187-104-112.ngrok-free.app";
                    request.setNotifyUrl(Ngrok+"/pay/payNotification");
                    JSONObject bizContent = new JSONObject();
                    bizContent.put("out_trade_no", orderNumber);//订单号
                    bizContent.put("total_amount", payDTO.getMoney());//订单金额
                    bizContent.put("subject", payDTO.getName());//支付主题，自己稍微定义一下
                    payNotificationService.setOderRedis(orderNumber,payDTO);
                    request.setBizContent(bizContent.toString());
                    try {
                        AlipayTradePrecreateResponse response = alipayClient.execute(request);
                        //获取生成的二维码，这里是一个String字符串，即二维码的内容；
                        //然后用二维码生成SDK生成一下二维码，弄成图片返回给前端就行,我这里使用Zxing生成
                        //其实也可以直接把这个字符串信息返回，让前端去生成，一样的道理，只需要关心这个二维码的内容就行
                        String qrCode = response.getQrCode();
                        String alipayUrl = "alipayqr://platformapi/startapp?saId=10000007&clientVersion=3.7.0.0718&qrcode=" + URLEncoder.encode(qrCode, "UTF-8");
                        UrlandCode urlandCode = new UrlandCode();
                        urlandCode.setUrl(alipayUrl);
                        urlandCode.setCode(qrCode);
                        urlandCode.setOrderNumber(orderNumber);
//            生成支付二维码图片
//            BufferedImage image = QrCodeUtil.createImage(qrCode);
//            ByteArrayOutputStream out = new ByteArrayOutputStream();
//            ImageIO.write(image, "jpeg", out);
//            byte[] b = out.toByteArray();
//            out.write(b);
//            out.close();
//            urlandcode.setCode(b);

                        //System.out.println(payNotificationService.oneRedis((PayDTO) redisTemplate.opsForValue().get("alipay:url:"+orderNumber)));
                        //最终返回图片
                        return RestBean.success("调用成功",urlandCode);
                    } catch (Exception e) {
                        e.printStackTrace();
                        RestBean.failure(404,"获取二维码失败");
                    }
                }finally {
                    // 5. 释放分布式锁，确保锁在处理完成后被释放
                    distributedLockUtil.releaseLock(lockKey, payDTO.getRequestId());
                }
            }else {
                // 6. 获取锁失败，可以根据业务需求进行处理，例如返回提示信息
                return RestBean.failure(404, "获取锁失败，请稍后重试");
            }
        }catch (Exception e) {
            e.printStackTrace();
            // 处理异常
            return RestBean.failure(500, "服务器内部错误");
        }
        return null;
    }

    /**
     * 支付完成后支付宝会请求这个回调
     */
    @PostMapping("payNotification")
    public String payNotification(HttpServletRequest request) {
        Map<String, String> params = new HashMap<>();
        Map<String, String[]> requestParams = request.getParameterMap();
        for (String name : requestParams.keySet()) {
            String[] values = requestParams.get(name);
            String valueStr = "";
            for (int i = 0; i < values.length; i++) {
                valueStr = (i == values.length - 1) ? valueStr + values[i] : valueStr + values[i] + ",";
            }
            params.put(name, valueStr);
        }

        for (Map.Entry<String, String> entry : params.entrySet()) {
            System.out.println("Key = " + entry.getKey() + ", Value = " + entry.getValue());
        }
        //==============================================================================================================
        try {
            //执行验签，确保结果是支付宝回调的，而不是被恶意调用，一定要做这一步
            boolean signVerified = AlipaySignature.rsaCheckV1(params, ALIPAY_PUBLIC_KEY, CHARSET, SIGN_TYPE);
            if (signVerified) {
                //验签成功，继续执行业务逻辑
                PayNotification payNotification = new PayNotification();
                //再次主动查询订单，不要只依赖支付宝回调的结果
                String orderStatus = searchOrderStatus(params.get("out_trade_no"), params.get("trade_no"));
                switch (orderStatus) {
                    case "TRADE_SUCCESS"://交易支付成功；
                    case "TRADE_FINISHED": //交易结束，不可退款；
                        //TODO 在这里继续执行用户支付成功后的业务逻辑
                        payNotification.setSellerEmail(params.get("seller_email"));
                        payNotification.setSubject(params.get("subject"));
                        payNotification.setInvoiceAmount(params.get("invoice_amount"));
                        payNotification.setNotifyId(params.get("out_trade_no"));
                        payNotification.setTime(params.get("gmt_create"));
                        String key = payNotificationService.oneRedis((PayDTO) redisTemplate.opsForValue().get("alipay:url:"+params.get("out_trade_no")));
                        payNotification.setCardkey(key);
                        payNotificationService.insertPayNotification(payNotification);
                        userPayState = true;
                        break;
                }
                return "success";
            } else {
                //验签失败（很可能接口被非法调用）
                System.out.println("验签失败");
                return "fail";
            }
        } catch (AlipayApiException e) {
            e.printStackTrace();
            return "fail";
        }
    }

    /**
     * 封装一个订单查询
     *
     * @param outTradeNo 商户订单号
     * @param tradeNo    支付宝交易号。支付宝交易凭证号
     * @return 订单状态：String
     * @throws AlipayApiException AlipayApiException
     * @desc "WAIT_BUYER_PAY":交易创建，等待买家付款；"TRADE_CLOSED":未付款交易超时关闭，或支付完成后全额退款； "TRADE_SUCCESS":交易支付成功；"TRADE_FINISHED":交易结束，不可退款；
     */
    private String searchOrderStatus(String outTradeNo, String tradeNo) throws AlipayApiException {
        AlipayClient alipayClient = new DefaultAlipayClient(SERVER_URL, APPID, APP_PRIVATE_KEY, FORMAT, CHARSET, ALIPAY_PUBLIC_KEY, SIGN_TYPE); //获得初始化的AlipayClient
        AlipayTradeQueryRequest aliRequest = new AlipayTradeQueryRequest();//创建API对应的request类
        JSONObject bizContent = new JSONObject();
        bizContent.put("out_trade_no", outTradeNo);
        bizContent.put("trade_no", tradeNo);
        aliRequest.setBizContent(bizContent.toString()); //设置业务参数
        AlipayTradeQueryResponse response = alipayClient.execute(aliRequest);//通过alipayClient调用API，获得对应的response类
        JSONObject responseObject = JSONObject.parseObject(response.getBody());
        JSONObject alipayTradeQueryResponse = responseObject.getJSONObject("alipay_trade_query_response");
        return alipayTradeQueryResponse.getString("trade_status");
    }

    /**
     * 前端轮询查询这个接口，来查询订单的支付状态
     *
     * @return OrderStateEntity
     */
    @CrossOrigin
    @GetMapping("searchOrder")
    public RestBean searchOrder(@Param("orderNumber") String orderNumber) {
        PayReturnDTO  PayReturnDTO = (PayReturnDTO) redisTemplate.opsForValue().get("alipay:return:"+orderNumber);
        if ( PayReturnDTO!=null) {
            //用户支付成功了
            return RestBean.success(PayReturnDTO);
        } else {
            //用户还没有支付
            return RestBean.failure(201,"用户未支付");
        }
    }
}
