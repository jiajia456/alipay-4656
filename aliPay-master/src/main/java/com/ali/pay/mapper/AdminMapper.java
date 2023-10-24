package com.ali.pay.mapper;

import com.ali.pay.entity.Admin;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface AdminMapper {

    @Select("select * from user where username = #{userName}")
    Admin selectByUserName(String userName);
}
