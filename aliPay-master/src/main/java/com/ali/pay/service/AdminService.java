package com.ali.pay.service;

import com.ali.pay.config.RedisConfig;
import com.ali.pay.dto.AdminDTO;
import com.ali.pay.entity.Admin;
import com.ali.pay.entity.RestBean;
import com.ali.pay.mapper.AdminMapper;
import com.ali.pay.service.Impl.AdminServiceImpl;
import com.ali.pay.util.JwtUtils;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class AdminService implements AdminServiceImpl {

    @Autowired
    private AdminMapper adminMapper;

    @Autowired
    private JwtUtils jwtUtils;

//    @Autowired
//    RedisTemplate<String,Object> redisTemplate;



    @Override
    public Map<String, Object> login(AdminDTO adminDTO) {

        String password = adminDTO.getPassword();
        password = DigestUtils.md5DigestAsHex(password.getBytes());

        Admin admin = adminMapper.selectByUserName(adminDTO.getUsername());

        if (admin != null) {
            String token = jwtUtils.generateToken(admin.getUserName(), admin.getAvatar());
            Map<String,Object> data = new HashMap<>();
            data.put("token",token);
//            redisTemplate.opsForValue().set(token,admin.getUserName(),30,TimeUnit.MINUTES);
            return  data;
        }

        return null;
    }

    @Override
    public Map<String, Object> getUserInfo(String token) {
        Map<String,Object> data = new HashMap<>();
        try {
            data = jwtUtils.getUsernameFromToken(token);
        } catch (RuntimeException e) {
            // 令牌已过期
            log.info("令牌信息有误！");
            return null;
            // 返回错误响应或要求用户重新登录
        }
        return data;
    }
}
