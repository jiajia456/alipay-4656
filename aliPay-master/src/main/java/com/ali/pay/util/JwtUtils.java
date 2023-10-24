package com.ali.pay.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtUtils {
    private String secret = "alipay"; // 密钥

    private Long expiration = 3600L; // 过期时间（秒）

    public String generateToken(String username,String avatar) {
        // 获取当前时间
        Date now = new Date();

        // 计算过期时间
        Date expiryDate = new Date(now.getTime() + expiration * 1000);

        // 生成 JWT 令牌
        String token = Jwts.builder()
                .setHeaderParam("typ","JWT")//设置类型
                .setHeaderParam("alg","HS256")//设置签名算法
                .claim("username",username)//设置载荷
                .claim("avatar",avatar)//设置载荷
                .setSubject(username) // 设置令牌的主题为用户名,验证JWT令牌时，可以使用主题来确定令牌所代表的用户
                .setIssuedAt(now) // 设置令牌的签发时间为当前时间
                .setExpiration(expiryDate) // 设置令牌的过期时间
                .signWith(SignatureAlgorithm.HS256, secret) // 使用 HS256 签名算法和密钥签名令牌
                .compact(); // 构建 JWT 令牌字符串

        return token;
    }

    public Map<String, Object> getUsernameFromToken(String token) {
        // 使用密钥解析令牌并获取主题（用户名）
        Claims claims = Jwts.parser()
                .setSigningKey(secret)
                .parseClaimsJws(token)
                .getBody();

//        String username = claims.getSubject(); // 从主题中获取用户名
        Map<String, Object> data = new HashMap<>();
        data.put("username",claims.getSubject());
        String avatar = (String) claims.get("avatar"); // 从自定义声明中获取其他参数
        data.put("avatar",avatar);

        return data;
    }

//    public String removeToken(String){}

}
