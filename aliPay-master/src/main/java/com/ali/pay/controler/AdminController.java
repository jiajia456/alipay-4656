package com.ali.pay.controler;


import com.ali.pay.dto.AdminDTO;
import com.ali.pay.entity.RestBean;
import com.ali.pay.service.Impl.AdminServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;


@Slf4j
@RestController
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    private AdminServiceImpl adminService;
//    private AuthorizeServiceImpl authorizeService;
    @PostMapping("/login")
    public RestBean<String> adminLogin(HttpServletRequest request, @RequestBody AdminDTO adminDTO)
    {
//        authorizeService.loadUserByUsername(adminDTO.getUserName());

        Map<String,Object> data = adminService.login(adminDTO);

        if(data == null){
            return RestBean.error("用户名或密码错误");
        }

        return RestBean.success("登录成功！", data);

    }

    @GetMapping("/info")
    public RestBean<Map<String,Object>> getUserInfo(@RequestParam String token){
        Map<String, Object> data = adminService.getUserInfo(token);

        if(data!=null){
            return RestBean.success(data);
        }
        return RestBean.error("登录信息有误，请重新登录");
    }

    @PostMapping("/logout")
    public RestBean<Map<String,Object>> logout(@RequestHeader("X-Token") String token){
//        adminService.logout(token);
        return RestBean.success();
    }
}
