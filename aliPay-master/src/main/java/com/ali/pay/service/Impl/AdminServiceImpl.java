package com.ali.pay.service.Impl;

import com.ali.pay.dto.AdminDTO;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public interface AdminServiceImpl {
    Map<String, Object> login(AdminDTO adminDTO);

    Map<String, Object> getUserInfo(String token);
}
