package com.ali.pay.mapper;

import com.ali.pay.entity.Account;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserMapper {
    //查询用户
    @Select("select * from user where username = #{text}")
    Account findAccountUsername(String text);
}
