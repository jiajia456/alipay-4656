package com.ali.pay.service.Impl;

import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Service;

@Service
public interface AuthorizeServiceImpl {
    String sendValidateEmail(String email, String sessionId);
    String validateAndRegister(String username,String password,String email,String code);
}
