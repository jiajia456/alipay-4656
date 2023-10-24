package com.ali.pay.service.Impl;

import org.apache.ibatis.annotations.Select;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public interface AuthorizeServiceImpl {
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException;
    String sendValidateEmail(String email, String sessionId);
    String validateAndRegister(String username,String password,String email,String code);
}
